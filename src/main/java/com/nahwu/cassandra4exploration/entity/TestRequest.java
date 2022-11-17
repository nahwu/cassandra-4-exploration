package com.nahwu.cassandra4exploration.entity;

import lombok.Data;

@Data
public class TestRequest {
    private String payload;

    private int insertionSize;
}
