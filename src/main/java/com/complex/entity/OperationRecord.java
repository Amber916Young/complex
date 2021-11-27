package com.complex.entity;

import lombok.Data;

@Data
public class OperationRecord {
    private int id;
    private String method;
    private String operateTime;
    private String user;
    private String behavior;
}
