package com.dummy.wpc.datadaptor.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import com.dummy.wpc.datadaptor.to.ProductKeyTO;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


public class ProdDAO implements IProdDAO {

    private MongoDatabase mongoDatabase;

    private static String ctryRecCde = "ctryRecCde";

    private static String grpMembrRecCde = "grpMembrRecCde";

    private static String prodTypeCde = "prodTypeCde";

    MongoCollection<Document> productColl;

    public void init() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoDatabase = mongoDatabase.withCodecRegistry(pojoCodecRegistry);
        productColl = mongoDatabase.getCollection("product");
    }

    @Override
    public List<ProductKeyTO> retrieveProdAltPrimNumByProdType(String ctryRecCde, String grpMemRecCde, String prodType) {
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put(ProdDAO.ctryRecCde, ctryRecCde);
        searchCriteria.put(ProdDAO.grpMembrRecCde, grpMemRecCde);
        searchCriteria.put(ProdDAO.prodTypeCde, prodType);

        return productColl
                .find(new BasicDBObject(searchCriteria), ProductKeyTO.class)
                .projection(Projections.include("prodTypeCde", "prodAltPrimNum", "prodMktPrcAmt"))
                .into(new ArrayList<>());
    }

    public void setMongoDatabase(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }
}
