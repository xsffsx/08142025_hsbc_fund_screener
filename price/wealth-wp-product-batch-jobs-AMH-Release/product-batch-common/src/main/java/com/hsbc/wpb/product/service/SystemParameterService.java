package com.dummy.wpb.product.service;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.SystemParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SystemParameterService {

    private final MongoOperations mongoOperations;

    public SystemParameterService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Get the next successful file sequence for outgoing batch file & update into DB. Create a new one if system parameter is empty
     * @param ctryRecCde e.g. HK
     * @param grpMembrRecCde e.g. HBAP
     * @param parmCde e.g. EGRESS.SRBP
     * @return the next sequence number for batch file
     */
    public String getNextSequence(String ctryRecCde, String grpMembrRecCde, String parmCde) {
        LockService lockService = new LockService(mongoOperations);
        String lockName = ctryRecCde + "." + grpMembrRecCde + "." + parmCde + ".LOCK";
        String token = lockService.lock(lockName);
        try {
            long nextSeq;
            SystemParameter systemParameter = getSystemParameter(ctryRecCde, grpMembrRecCde, parmCde);
            if (systemParameter == null) {
                // create a new system parameter if query result is empty
                nextSeq = 1L;
                createSystemParameter(ctryRecCde, grpMembrRecCde, parmCde, Long.toString(nextSeq));
            } else {
               long lastSeq = Long.parseLong(systemParameter.getParmValueText());
               nextSeq = lastSeq + 1;
               // update new seq no. into db
               systemParameter.setParmValueText(Long.toString(nextSeq));
               updateSystemParameter(systemParameter);
            }
            log.info("Batch {} sequence = {}", parmCde, nextSeq);
            return Long.toString(nextSeq);
        } finally {
            lockService.unLock(lockName, token);
        }
    }

    public String getLastSuccessfulDateTime(String ctryRecCde, String grpMembrRecCde, String parmCde) {
        String lastSuccessfulDateTime;
        SystemParameter systemParameter = getSystemParameter(ctryRecCde, grpMembrRecCde, parmCde);
        if (systemParameter == null) {
            lastSuccessfulDateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
            createSystemParameter(ctryRecCde, grpMembrRecCde, parmCde, lastSuccessfulDateTime);
        } else {
            lastSuccessfulDateTime = systemParameter.getParmValueText();
            // update new timestamp into db
            systemParameter.setParmValueText(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
            updateSystemParameter(systemParameter);
        }
        log.info("Batch export file last successful time = {}", lastSuccessfulDateTime);
        return lastSuccessfulDateTime;
    }

    /**
     * Get system parameter
     * @param ctryRecCde e.g. HK
     * @param grpMembrRecCde e.g. HBAP
     * @param parmCde e.g. EGRESS.SRBP
     */
    public SystemParameter getSystemParameter(String ctryRecCde, String grpMembrRecCde, String parmCde) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.parmCde).is(parmCde));
        return mongoOperations.findOne(query, SystemParameter.class);
    }

    /**
     * Create system parameter
     * @param ctryRecCde e.g. HK
     * @param grpMembrRecCde e.g. HBAP
     * @param parmCde e.g. EGRESS.SRBP
     * @param parmValueText e.g. 1234
     */
    public void createSystemParameter(String ctryRecCde, String grpMembrRecCde, String parmCde, String parmValueText) {
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setCtryRecCde(ctryRecCde);
        systemParameter.setGrpMembrRecCde(grpMembrRecCde);
        systemParameter.setParmCde(parmCde);
        systemParameter.setParmValueText(parmValueText);
        systemParameter.setRecCreatDtTm(LocalDateTime.now());
        systemParameter.setRecUpdtDtTm(LocalDateTime.now());
        mongoOperations.save(systemParameter);
    }

    /**
     * Update system parameter
     */
    public void updateSystemParameter(SystemParameter systemParameter) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(systemParameter.getId()));
        systemParameter.setRecUpdtDtTm(LocalDateTime.now());
        mongoOperations.findAndReplace(query, systemParameter);
    }
}
