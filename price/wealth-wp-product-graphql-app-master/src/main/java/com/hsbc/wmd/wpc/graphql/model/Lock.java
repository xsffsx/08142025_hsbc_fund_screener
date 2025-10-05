package com.dummy.wmd.wpc.graphql.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "lock")
public class Lock {
    @Id
    private String id;

    private LocalDateTime lockTime;

    private Long timeout;

    private String lockFrom;

    private String token;
}
