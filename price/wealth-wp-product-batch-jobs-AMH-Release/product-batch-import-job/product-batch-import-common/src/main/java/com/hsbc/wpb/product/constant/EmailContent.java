package com.dummy.wpb.product.constant;


import lombok.Data;

@Data
public class EmailContent {
    private String subject;
    private String recptAdr;
    private String content;

    public EmailContent(String subject, String recptAdr, String content) {
        this.recptAdr = recptAdr;
        this.subject = subject;
        this.content = content;
    }

    public EmailContent() {
    }
}