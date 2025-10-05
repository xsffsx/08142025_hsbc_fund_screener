package com.dummy.wmd.wpc.graphql.service;


import com.dummy.wmd.wpc.graphql.ApplicationContextConfig;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.RecStatCde;
import com.dummy.wmd.wpc.graphql.constant.Sequence;
import com.dummy.wmd.wpc.graphql.error.ErrorCode;
import com.dummy.wmd.wpc.graphql.error.Errors;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
@Service
public class FileUploadService {
    @Autowired private FileService fileService;
    @Autowired private SequenceService sequenceService;
    private MongoCollection<Document> colFileUpload;
    private MongoCollection<Document> colFileChunk;

    public FileUploadService(MongoDatabase mongoDatabase) {
        this.colFileUpload = mongoDatabase.getCollection(CollectionName.file_upload.toString());
        this.colFileChunk = mongoDatabase.getCollection(CollectionName.file_chunk.toString());
    }

    public Document getUploadFile(String fileMd5) {
        return this.colFileUpload.find(eq(Field.fileMd5, fileMd5)).first();
    }

    public Document approve(String approvedBy, Long uploadId, String approvalAction, String comment, String uploadType) {
        Bson filter = eq(Field._id, uploadId);
        Document docUpload = colFileUpload.find(filter).first();
        if (null == docUpload) {
            String message = String.format("file upload with id %s not found", uploadId);
            throw new productErrorException(productErrors.DocumentNotFound, message);
        }

        if(StringUtils.hasText(approvedBy) && approvedBy.equals(docUpload.getString(Field.emplyNum))) {
            throw new productErrorException(productErrors.ApproveByCreator, "Upload file is not allow to be approved by creator");
        }

        String recStatCde = docUpload.getString(Field.recStatCde);
        if (!recStatCde.equals("pending")) {
            String message = String.format("file upload in wrong status to be approval: id=%s, recStatCde=%s", uploadId, recStatCde);
            throw new productErrorException(productErrors.DocumentStatusError, message);
        }
        docUpload.put(Field.approvedBy, approvedBy);
        docUpload.put(Field.recStatCde, approvalAction);
        docUpload.put(Field.approvalComment, comment);
        docUpload.put(Field.recUpdtDtTm, new Date());

        //copy the file to batch ingress
        if (RecStatCde.approved.toString().equals(approvalAction)) {
            String ctryRecCde = docUpload.getString(Field.ctryRecCde);
            String grpMembrRecCde = docUpload.getString(Field.grpMembrRecCde);
            String ingressPath = ApplicationContextConfig.getConfig("batch.ingress.path")+ ctryRecCde + grpMembrRecCde + File.separator;
            String msutPath = ApplicationContextConfig.getConfig("batch.start-ingress.path");
            log.info("msutPath" + msutPath);
            String filename = newFilename(docUpload.getString(Field.fileName), uploadId);
            log.info("filename" + filename);
            String filePath = ingressPath + filename;
            if ("MSUT".equals(uploadType)) {
                filePath = msutPath + filename;
            }
            log.info("filePath" + filePath);
            String fileMd5 = docUpload.getString(Field.fileMd5);
            transferFileToBatch(filePath, fileMd5);
        }

        colFileUpload.replaceOne(filter, docUpload);
        return colFileUpload.find(filter).first();
    }

    /**
     * Build a new filename with the id appended right before the ext name, to make every upload with a new name, so that we can exactly match the upload result afterward
     * eg. GBHBEU_REFDATA_20210921151111.xlsx --> GBHBEU_REFDATA_20210921151111259.xlsx, in witch the 259 is the id of the file upload record
     *
     * @param filename
     * @param id
     * @return
     */
    public static String newFilename(String filename, Object id) {
        StringBuilder sb = new StringBuilder(filename);
        int idx = sb.lastIndexOf(".");
        if(-1 != idx) {
            sb.insert(idx, id);
        }
        return sb.toString();
    }

    public Document uploadRequestApproval(String ctryRecCde, String grpMembrRecCde, String emplyNum, String uploadType, MultipartFile file, String comments) {
        String fileName = getFilename(file);

        String md5 = fileService.saveFile(file);

        Document fileUpload = new Document();
        fileUpload.put(Field._id, sequenceService.nextId(Sequence.fileAmendmentId));
        fileUpload.put(Field.ctryRecCde, ctryRecCde);
        fileUpload.put(Field.grpMembrRecCde, grpMembrRecCde);
        fileUpload.put(Field.emplyNum, emplyNum);
        fileUpload.put(Field.recStatCde, RecStatCde.pending.toString());
        fileUpload.put(Field.uploadType, uploadType);
        fileUpload.put(Field.fileName, fileName);
        fileUpload.put(Field.fileMd5, md5);
        fileUpload.put(Field.requestComment, comments);
        fileUpload.put(Field.recCreatDtTm, new Date());
        fileUpload.put(Field.recUpdtDtTm, new Date());
        colFileUpload.insertOne(fileUpload);
        return fileUpload;
    }

    public void transferFileToBatch(String filePath, String fileId) {
        try (FileOutputStream out = new FileOutputStream(CommonUtils.canonicalPath(filePath))) {
            FindIterable<Document> result = this.getChunks(fileId);
            for (Document chunk : result) {
                Binary binary = chunk.get("data", Binary.class);
                out.write(binary.getData());
                out.flush();
            }
        } catch (IOException e) {
        	Errors.log(ErrorCode.OTPSERR_EGQ102, e.getMessage(), e, log);
            throw new productErrorException(productErrors.RuntimeException, "Can not write file chunks data:" + e.getMessage());
        }
    }

    /**
     * Get file chunk documents from db
     *
     * @param fileMd5
     * @return
     */
    public FindIterable<Document> getChunks(String fileMd5) {
        Bson filter = eq(Field.md5, fileMd5);
        BsonDocument sortBson = BsonDocument.parse("{seqNum: 1}");
        return colFileChunk.find(filter).sort(sortBson);
    }

    /**
     * It's found that getOriginalFilename() response full path in IE requests
     *
     * @param file
     * @return
     */
    public String getFilename(MultipartFile file) {
        String name = file.getOriginalFilename();
        Assert.notNull(name, "fileName can not be null");
        int idx = name.lastIndexOf('/');
        if(-1 != idx) {
            log.warn("get filename from the full path: {}", name);
            return name.substring(idx + 1);
        }
        idx = name.lastIndexOf('\\');
        if(-1 != idx) {
            log.warn("get filename from the full path: {}", name);
            return name.substring(idx + 1);
        }
        return name;
    }
}
