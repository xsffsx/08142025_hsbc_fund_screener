/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		FinDocProdRL
 *
 * Creation Date	Mar 8, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 8, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.component;


import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.DuplicateKeyException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.model.FinDocHistPo;
import com.dummy.wpb.product.model.FinDocPo;
import com.dummy.wpb.product.model.FinDocULReqPo;
import com.dummy.wpb.product.model.SystemParmPo;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.dummy.wpb.product.FinDocReleaseJobApplication.JOB_NAME;
import static com.dummy.wpb.product.constant.FinDocConstants.RECORD_NOT_FOUND;


@Repository
@Slf4j
public class FinDocReleaseDao {
	
	@Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public MongoDatabase mongoDatabase;

    private MongoCollection<Document> colFinDocUpld;
    
    private MongoCollection<Document> colFinDocHist;

	private MongoCollection<Document> colProdTypeFinDoc;

	private MongoCollection<Document> colProduct;



	public FinDocReleaseDao(MongoDatabase mongoDatabase){
        this.colFinDocUpld = mongoDatabase.getCollection(CollectionName.fin_doc_upld.toString());
        this.colFinDocHist = mongoDatabase.getCollection(CollectionName.fin_doc_hist.toString());
		this.colProdTypeFinDoc = mongoDatabase.getCollection(CollectionName.prod_type_fin_doc.toString());
		this.colProduct = mongoDatabase.getCollection(CollectionName.product.toString());
    }

	public FinDocULReqPo[] retrieveByStatCde(String ctryCde, String orgnCde, String statusCde) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryCde));
		query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(orgnCde));
		query.addCriteria(Criteria.where(Field.docStatCde).is(statusCde));

		List<FinDocULReqPo> rs = mongoTemplate.find(query, FinDocULReqPo.class);
		return rs.toArray(new FinDocULReqPo[rs.size()]);
	}

	public void update(FinDocULReqPo finDocULReqPo) throws RecordNotFoundException {
		Bson filter = Filters.and(Filters.eq(Field.ctryRecCde, finDocULReqPo.getCtryRecCde()),
                Filters.eq(Field.grpMembrRecCde, finDocULReqPo.getGrpMembrRecCde()),
                Filters.eq(Field.fileRqstName, finDocULReqPo.getFileRqstName()),
                Filters.eq(Field.docUpldSeqNum, finDocULReqPo.getDocUpldSeqNum()));
		
		FindIterable<Document> findIterable =  colFinDocUpld.find(filter);
		if (!findIterable.iterator().hasNext()) {
			throw new RecordNotFoundException(RECORD_NOT_FOUND + finDocULReqPo.getCtryRecCde() + ":"
					+ finDocULReqPo.getGrpMembrRecCde() + ":" + finDocULReqPo.getFileRqstName() + ":"
					+ finDocULReqPo.getDocUpldSeqNum());
		}
		
		Document finDocUpld = new Document();
		finDocUpld.put(Field.urlLclSysText, finDocULReqPo.getUrlLclSysText());
		finDocUpld.put(Field.docStatCde, finDocULReqPo.getDocStatCde());
		Date today = new Date();
		finDocUpld.put(Field.recUpdtDtTm, today);
		if(!finDocULReqPo.getDocStatCde().equalsIgnoreCase(FinDocConstants.REJECT)&&finDocULReqPo.getDocServrStatCde().equalsIgnoreCase(FinDocConstants.CONFIRM)){
			finDocUpld.put(Field.fileDocName, finDocULReqPo.getFileDocName());
			finDocUpld.put(Field.urlFileServrText, finDocULReqPo.getUrlFileServrText());
			finDocUpld.put(Field.docServrStatCde, finDocULReqPo.getDocServrStatCde());
			finDocUpld.put(Field.docRleasStartDtTm, today);
			finDocUpld.put(Field.docRleasEndDtTm, today);

		}
        colFinDocUpld.updateOne(filter, new Document().append("$set", finDocUpld));
	}

	public void updateBatchFinDocULReq(List<FinDocULReqPo> finDocULReqPos) {
		List<WriteModel<Document>> bulkOperations = new ArrayList<>();

		for (FinDocULReqPo finDocULReqPo : finDocULReqPos) {
			Bson filter = Filters.and(
					Filters.eq(Field.ctryRecCde, finDocULReqPo.getCtryRecCde()),
					Filters.eq(Field.grpMembrRecCde, finDocULReqPo.getGrpMembrRecCde()),
					Filters.eq(Field.fileRqstName, finDocULReqPo.getFileRqstName()),
					Filters.eq(Field.docUpldSeqNum, finDocULReqPo.getDocUpldSeqNum())
			);

			Document finDocUpld = new Document();
			finDocUpld.put(Field.urlLclSysText, finDocULReqPo.getUrlLclSysText());
			finDocUpld.put(Field.docStatCde, finDocULReqPo.getDocStatCde());
			Date today = new Date();
			finDocUpld.put(Field.recUpdtDtTm, today);

			if(!finDocULReqPo.getDocStatCde().equalsIgnoreCase(FinDocConstants.REJECT) &&
					finDocULReqPo.getDocServrStatCde().equalsIgnoreCase(FinDocConstants.CONFIRM)){
				finDocUpld.put(Field.fileDocName, finDocULReqPo.getFileDocName());
				finDocUpld.put(Field.urlFileServrText, finDocULReqPo.getUrlFileServrText());
				finDocUpld.put(Field.docServrStatCde, finDocULReqPo.getDocServrStatCde());
				finDocUpld.put(Field.docRleasStartDtTm, today);
				finDocUpld.put(Field.docRleasEndDtTm, today);
			}

			UpdateOneModel<Document> updateModel = new UpdateOneModel<>(
					filter,
					new Document().append("$set", finDocUpld),
					new UpdateOptions().upsert(false)
			);
			bulkOperations.add(updateModel);
		}

		if (!bulkOperations.isEmpty()) {
			BulkWriteResult bulkWriteResult = colFinDocUpld.bulkWrite(bulkOperations);
			log.info("FinDocUL Bulk update count: {}, result: {}", bulkOperations.size(), bulkWriteResult.getModifiedCount());
		}
	}

	public FinDocHistPo[] retrieveFinDocSmryRecordByStatCde(String ctryCde, String orgnCde, String statusCde) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryCde));
		query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(orgnCde));
		query.addCriteria(Criteria.where(Field.docStatCde).is(statusCde));

		List<FinDocHistPo> rs = mongoTemplate.find(query, FinDocHistPo.class);
		return rs.toArray(new FinDocHistPo[rs.size()]);
	}

	public void updateBatchFinDocHist(List<FinDocHistPo> finDocHistPos) {
		List<WriteModel<Document>> bulkOperations = new ArrayList<>();

		for (FinDocHistPo finDocHistPo : finDocHistPos) {
			Bson filter = Filters.eq(Field.rsrcItemIdFinDoc, finDocHistPo.getRsrcItemIdFinDoc());

			Document finDocUpld = new Document();
			finDocUpld.put(Field.urlLclSysText, finDocHistPo.getUrlLclSysText());
			finDocUpld.put(Field.docStatCde, finDocHistPo.getDocStatCde());
			finDocUpld.put(Field.recUpdtDtTm, new Date());

			UpdateOneModel<Document> updateModel = new UpdateOneModel<>(
					filter,
					new Document().append("$set", finDocUpld),
					new UpdateOptions().upsert(false)
			);
			bulkOperations.add(updateModel);
		}

		if (!bulkOperations.isEmpty()) {
			BulkWriteResult bulkWriteResult = colFinDocHist.bulkWrite(bulkOperations);
			log.info("FinDocHist Bulk update count: {}, result: {}", bulkOperations.size(), bulkWriteResult.getModifiedCount());
		}
	}
	
	public void update(FinDocHistPo finDocHistPo) throws RecordNotFoundException {
		Bson filter = Filters.eq(Field.rsrcItemIdFinDoc, finDocHistPo.getRsrcItemIdFinDoc());
		
		FindIterable<Document> findIterable =  colFinDocHist.find(filter);
		if (!findIterable.iterator().hasNext()) {
			throw new RecordNotFoundException(RECORD_NOT_FOUND + finDocHistPo.getRsrcItemIdFinDoc());
		}
		
		Document finDocUpld = new Document();
		finDocUpld.put(Field.fileDocName, finDocHistPo.getFileDocName());
		finDocUpld.put(Field.urlLclSysText, finDocHistPo.getUrlLclSysText());
		finDocUpld.put(Field.urlFileServrText, finDocHistPo.getUrlFileServrText());
		finDocUpld.put(Field.docServrStatCde, finDocHistPo.getDocServrStatCde());
		Date today = new Date();
		finDocUpld.put(Field.recUpdtDtTm, today);
		finDocUpld.put(Field.docRleasStartDtTm, today);
		finDocUpld.put(Field.docRleasEndDtTm, today);
		colFinDocHist.updateOne(filter, new Document().append("$set", finDocUpld));
	}

	public FinDocULReqPo retrieveByDocSerNum(FinDocHistPo finDocHistPo) throws RecordNotFoundException {
		Query query = buildCommonFilter(finDocHistPo);
		query.addCriteria(Criteria.where(Field.docSerNum).is(finDocHistPo.getRsrcItemIdFinDoc()));

		FinDocULReqPo rs = mongoTemplate.findOne(query, FinDocULReqPo.class);
		if (rs == null) {
			throw new RecordNotFoundException(
					RECORD_NOT_FOUND + finDocHistPo.getCtryRecCde() + ":" + finDocHistPo.getGrpMembrRecCde()
							+ ":" + finDocHistPo.getDocFinTypeCde() + ":" + finDocHistPo.getDocFinCatCde() + ":"
							+ finDocHistPo.getDocFinCde() + ":" + finDocHistPo.getRsrcItemIdFinDoc());
		}
		return rs;
	}

	public FinDocHistPo[] retrieveFinDocSmryRecordByStatusFS(String ctryCde, String orgnCde, String statusFs) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryCde));
		query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(orgnCde));
		query.addCriteria(Criteria.where(Field.docStatCde).is(FinDocConstants.APPROVAL));
		query.addCriteria(Criteria.where(Field.docServrStatCde).is(statusFs));

		List<FinDocHistPo> rs = mongoTemplate.find(query, FinDocHistPo.class);
		return rs.toArray(new FinDocHistPo[rs.size()]);
	}

    public SystemParmPo retrieveFinDocSysPramByProdType(String ctryRecCde, String grpMembrRecCde, String docFinTypeCde, String docFinCatCde, String parmCde) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
		query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
		query.addCriteria(Criteria.where(Field.docFinTypeCde).is(docFinTypeCde));
		query.addCriteria(Criteria.where(Field.docFinCatCde).is(docFinCatCde));
		query.addCriteria(Criteria.where(Field.parmCde).is(parmCde));

		return mongoTemplate.findOne(query, SystemParmPo.class);
	}

	public FinDocPo retrieveFinDocSmryRecordByDocCdeNLangCdeLatest(FinDocHistPo fds) {
		Query query = buildCommonFilter(fds);
		query.addCriteria(Criteria.where(Field.langFinDocCde).is(fds.getLangFinDocCde()));
		query.addCriteria(Criteria.where(Field.docStatCde).is(FinDocConstants.APPROVAL));
		query.addCriteria(Criteria.where(Field.docServrStatCde).is(FinDocConstants.CONFIRM));

		return mongoTemplate.findOne(query, FinDocPo.class);
	}

	public void updateLatest(FinDocHistPo fds) {

		Query filter = buildCommonFilter(fds);
		filter.addCriteria(Criteria.where(Field.langFinDocCde).is(fds.getLangFinDocCde()));
		filter.addCriteria(Criteria.where(Field.docStatCde).is(fds.getDocStatCde()));
		filter.addCriteria(Criteria.where(Field.docServrStatCde).is(fds.getDocServrStatCde()));

		FinDocPo finDoc = convertToFinDoc(fds);

		mongoTemplate.findAndReplace(filter, finDoc, "fin_doc");
	}

	private FinDocPo convertToFinDoc (FinDocHistPo fds) {
		FinDocPo finDocPo = new FinDocPo();
		BeanUtils.copyProperties(fds, finDocPo, "_id");
		Date today = new Date();
		finDocPo.setDocRleasStartDtTm(today);
		finDocPo.setDocRleasEndDtTm(today);
		finDocPo.setRecUpdtDtTm(today);
		return finDocPo;

	}

	private Query buildCommonFilter(FinDocHistPo fds) {
		Query filter = new Query();
		filter.addCriteria(Criteria.where(Field.ctryRecCde).is(fds.getCtryRecCde()));
		filter.addCriteria(Criteria.where(Field.grpMembrRecCde).is(fds.getGrpMembrRecCde()));
		filter.addCriteria(Criteria.where(Field.docFinTypeCde).is(fds.getDocFinTypeCde()));
		filter.addCriteria(Criteria.where(Field.docFinCatCde).is(fds.getDocFinCatCde()));
		filter.addCriteria(Criteria.where(Field.docFinCde).is(fds.getDocFinCde()));
		return filter;
	}

	public void updateProdRLByDocSerNum(Long oldDocSerNum, Long newDocSerNum) {
		log.info("Update prod_type_fin_doc and product.finDoc--oldDocSerNum: " + oldDocSerNum + " newDocSerNum: " + newDocSerNum);
		if (!ObjectUtils.equals(oldDocSerNum, newDocSerNum)) {
			Bson filter = Filters.eq(Field.rsrcItemIdFinDoc, oldDocSerNum);
			Document finDocUpld = new Document();
			finDocUpld.put(Field.rsrcItemIdFinDoc, newDocSerNum);
			finDocUpld.put(Field.recUpdtDtTm, new Date());
			colProdTypeFinDoc.updateMany(filter, new Document().append("$set", finDocUpld));

			//update product.finDoc(PROD_FIN_DOC), use arrayFilters
			Bson filterProd = Filters.eq(Field.finDoc + "." + Field.rsrcItemIdFinDoc, oldDocSerNum);
			Document finDocProdUpld = new Document();
			finDocProdUpld.put(Field.finDoc + ".$[elem]." + Field.rsrcItemIdFinDoc, newDocSerNum);
			finDocProdUpld.put(Field.finDoc + ".$[elem]." + Field.recUpdtDtTm, new Date());
			finDocProdUpld.put(Field.recUpdtDtTm, new Date());
			finDocProdUpld.put(Field.lastUpdatedBy, JOB_NAME);
			Document arrayFilters = new Document("elem" + "." + Field.rsrcItemIdFinDoc, oldDocSerNum);
			UpdateResult updateResult = colProduct.updateMany(filterProd, new Document().append("$set", finDocProdUpld), new UpdateOptions().arrayFilters(Collections.singletonList(arrayFilters)));
			log.info("Update product.finDoc result: " + updateResult);
		}
	}

	public void insertLatest(FinDocHistPo fds) throws DuplicateKeyException {
		Query query = new Query();
		query.addCriteria(Criteria.where(Field.rsrcItemIdFinDoc).is(fds.getRsrcItemIdFinDoc()));

		FinDocPo rs = mongoTemplate.findOne(query, FinDocPo.class);
		if (rs != null) {
			throw new DuplicateKeyException("Record duplicate: " + fds.getRsrcItemIdFinDoc());
		}

		FinDocPo finDoc = convertToFinDoc(fds);

		mongoTemplate.save(finDoc,"fin_doc");
	}

}