package com.dummy.wpb.product.batch;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ExportRequest;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class ExportRequestParametersConverter extends DefaultJobParametersConverter {

    @Autowired
    private ExportRequest request;

    @Override
    public JobParameters getJobParameters(Properties props) {
        return new JobParametersBuilder(super.getJobParameters(props))
                .addString(Field.ctryRecCde, request.getCtryRecCde())
                .addString(Field.grpMembrRecCde, request.getGrpMembrRecCde())
                .addString("systemCde", request.getSystemCde())
                .addString("outputPath", request.getPath())
                .addString("ack", String.valueOf(request.getAck().isGenerate()))
                .toJobParameters();
    }
}
