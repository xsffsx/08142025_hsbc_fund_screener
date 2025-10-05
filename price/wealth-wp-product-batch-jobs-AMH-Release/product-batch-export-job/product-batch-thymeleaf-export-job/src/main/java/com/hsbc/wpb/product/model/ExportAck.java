package com.dummy.wpb.product.model;

import lombok.Data;

@Data
public class ExportAck {
    private boolean generate = false;

    private String fileName = "${ctryRecCde}_${grpMembrRecCde}_${systemCde}_${date:yyyyMMddHHmmss}_${sequence}.out";
}
