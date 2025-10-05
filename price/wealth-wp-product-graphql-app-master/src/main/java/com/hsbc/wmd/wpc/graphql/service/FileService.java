package com.dummy.wmd.wpc.graphql.service;


import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import com.dummy.wmd.wpc.graphql.utils.MD5Utils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
@Service
public class FileService {
    private MongoCollection<Document> colFile;
    private MongoCollection<Document> colFileChunk;
    private static final int FILE_CHUNK_SIZE = 15 * 1024 * 1024;  // 15M, Document size limit to 16M in MongoDB
    private static FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);

    public FileService(MongoDatabase mongoDatabase){
        this.colFile = mongoDatabase.getCollection(CollectionName.file.toString());
        this.colFileChunk = mongoDatabase.getCollection(CollectionName.file_chunk.toString());
    }

    /**
     * Save the file content to the db
     *
     * @param file
     * @return md5 of the file content
     */
    public String saveFile(MultipartFile file) {
        byte[] bytes = null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            String message = "Error reading upload file content: " + e.getMessage();
            throw new productErrorException(productErrors.RuntimeException, message);
        }

        String md5 = MD5Utils.md5(bytes);
        Bson filter = eq(Field._id, md5);
        long count = colFile.countDocuments(filter);
        if(count > 0) {
            // the file has been there, just increase the refCount and return the md5
            colFile.findOneAndUpdate(filter, Updates.inc("refCount", 1L), options);
        } else {
            saveFileChunks(md5, bytes);

            //add new record into "file" collection
            Document docFile = new Document();
            docFile.put(Field._id, md5);
            docFile.put("refCount", 1);
            docFile.put("length", file.getSize());
            docFile.put("chunkSize", FILE_CHUNK_SIZE);
            colFile.insertOne(docFile);
        }
        return md5;
    }

    private void saveFileChunks(String md5, byte[] source) {
        int pos = 0;
        int length = FILE_CHUNK_SIZE;
        for(int n =0; pos < source.length; n++) {
            if(source.length - pos < length) {
                length = source.length - pos;
            }
            byte[] chunk = new byte[length];
            System.arraycopy(source, pos, chunk, 0, length);

            Document docChunk = new Document();
            docChunk.put(Field.md5, md5);
            docChunk.put("data", chunk);
            docChunk.put("n", n);
            colFileChunk.insertOne(docChunk);
            pos += length;
        }
    }

    /**
     * Retrieve file content by md5
     *
     * @param md5
     * @return
     */
    public void retrieveFile(String md5, OutputStream out) throws IOException {
        Bson filter = eq(Field.md5, md5);
        BsonDocument sortBson = BsonDocument.parse("{n: 1}");
        FindIterable<Document> result = colFileChunk.find(filter).sort(sortBson);
        for (Document chunk : result) {
            Binary binary = chunk.get("data", Binary.class);
            out.write(binary.getData());
            out.flush();
        }
    }
}
