package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.model.Operation;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.model.OperationPair;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.*;
import java.util.regex.Pattern;

public class AmendmentResolver {
    private Document docBase;
    private Document docChanged;
    private Document docLatest;

    public AmendmentResolver(Document docBase, Document docChanged, Document docLatest) {
        this.docBase = docBase;
        this.docChanged = docChanged;
        this.docLatest = docLatest;
    }

    /**
     * check conflict, raise exception in case of conflict otherwise handle auto merge here
     *
     * @return
     */
    public Document resolve() {
        Long revisionLatest = DocumentUtils.getLong(docLatest, Field.revision);
        Long revisionBase = DocumentUtils.getLong(docBase, Field.revision);
        if (Objects.equals(revisionBase, revisionLatest)) {
            // the docBase is still the same, simply replace with the docChanged
            return docChanged;
        }

        // figure out is there conflict, in case conflict (docChanged and docLatest has change in the same filed), raise an exception
        // otherwise, auto merge the docChange
        DocumentPatch documentPatch = new DocumentPatch();
        List<OperationInput> opList1 = documentPatch.patch(docBase, docChanged);
        List<OperationInput> opList2 = documentPatch.patch(docBase, docLatest);
        List<OperationPair> opConflict = getConflictOperations(opList1, opList2);
        if (!opConflict.isEmpty()) {
            String message = String.format("Operation conflicted from docBase(%d) to docChanged and docLatest(%d)", revisionBase, revisionLatest);
            // put the conflict map into extension
            Map<String, Object> extensions = Collections.singletonMap("opConflict", opConflict);
            throw new productErrorException(productErrors.ChangeConflict, message, extensions);
        }

        // apply the operations to docLatest
        JsonPathUtils.applyChanges(docLatest, opList1);
        return docLatest;
    }

    private List<OperationPair> getConflictOperations(List<OperationInput> opList1, List<OperationInput> opList2) {
        List<OperationPair> result = new LinkedList<>();

        opList1.forEach(op1 -> {
            if (StringUtils.endsWithAny(op1.getPath(), ".recUpdtDtTm", ".revision", ".lastUpdatedBy", ".prodPrcUpdtDtTm")) {
                //any other fields to be ignored?
                return;
            }

            for (OperationInput op2 : opList2) {
                if (isConflict(op1, op2)) {
                    result.add(new OperationPair(op1.getPath(), op1, op2));
                    break;
                }
            }
        });

        return result;
    }

    private boolean isConflict(OperationInput op1, OperationInput op2) {
        if (Objects.equals(op1.getPath(), op2.getPath()) && Objects.equals(op1.getOp(), op2.getOp())) {
            if (Operation.add == op1.getOp()) {
                return Objects.equals(
                        ((Map<String, ?>) op1.getValue()).get(Field.rowid),
                        ((Map<String, ?>) op2.getValue()).get(Field.rowid));
            }

            // is it a real conflict?
            return !Objects.equals(op1.getValue(), op2.getValue());
        }

        return isConflictForSet(op1, op2) || isConflictForSet(op2, op1);
    }

    /**
     * If ui want to update element of the list while batch was deleting the list, it is a conflict operation.
     * Like:
     * <p>
     * opSet:
     * <pre>
     *  {
     *    "op": "set",
     *    "path": "$.formReqmt[?(@.rowid=='db3aa0e4-b407-48d4-83d0-f05c2d615d54')].formReqCde",
     *    "value": "W-9"
     *  }
     * </pre>
     * <p>
     * opDelete:
     * <pre>
     *  {
     *    "op": "delete",
     *    "path": "$.formReqmt[?(@.rowid=='db3aa0e4-b407-48d4-83d0-f05c2d615d54')]",
     *    "value": {
     *      "rowid": "db3aa0e4-b407-48d4-83d0-f05c2d615d54",
     *      "formReqCde": "W-8ECI"
     *    }
     *  }
     * </pre>
     */
    private boolean isConflictForSet(OperationInput opSet, OperationInput opDelete) {
        // for $.formReqmt[?(@.rowid=='db3aa0e4-b407-48d4-83d0-f05c2d615d54')].formReqCde
        // group1: $.formReqmt[?(@.rowid=='db3aa0e4-b407-48d4-83d0-f05c2d615d54')]
        // group2: $.formReqmt
        if (Operation.set == opSet.getOp() && Operation.delete == opDelete.getOp()) {
            Pattern listSetOpPattern = Pattern.compile("((\\$\\.[a-zA-Z0-9\\.]+)\\[\\?\\(@\\.rowid=='[^']*'\\)\\]).*");
            String pathWithRowId = RegExUtils.replaceAll(opSet.getPath(), listSetOpPattern, "$1");
            String path = RegExUtils.replaceAll(opSet.getPath(), listSetOpPattern, "$2");
            return StringUtils.equalsAny(opDelete.getPath(), pathWithRowId, path);
        }
        return false;
    }
}
