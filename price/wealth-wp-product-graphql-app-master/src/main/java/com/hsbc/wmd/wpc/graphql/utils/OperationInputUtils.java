package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.model.OperationInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OperationInputUtils {
    private OperationInputUtils() {}

    public static List<String> extractAllPaths(List<OperationInput> operations) {
        List<String> paths = new ArrayList<>();
        for (OperationInput operation : operations) {
            String path = getParentPath(operation.getPath());
            Object value = operation.getValue();
            if (value instanceof Map) {
                extractPathFromMap((Map) value, path, paths);
            } else {
                paths.add(path);
            }
        }
        return paths;
    }

    private static void extractPathFromMap(Map<?,?> map, String path, List<String> changePaths) {
        map.forEach((key, value) -> {
            String currPath = path + "." + key;
            if (value instanceof Map) {
                extractPathFromMap((Map) value, currPath, changePaths);
            } else {
                changePaths.add(currPath);
            }
        });
    }

    /** Just return the parent path of list element.
     * eg:
     *  stockInstm.invstMipIncrmMinAmt => stockInstm.invstMipIncrmMinAmt
     *  $.undlAset[?(@.rowid=='9ed2ced0-8f23-4bcd-b08d-8227e9fec2d7')].asetUndlCde => undlAset
     *  $.undlAset[*].asetUndlCde => undlAset
     * @param path
     * @return
     */
    private static String getParentPath(String path) {
        if (path.startsWith("$.")) {
            path = path.substring(2);
        }

        if (path.contains("[")) {
            path = path.substring(0, path.indexOf("["));
        }
        return path;
    }
}
