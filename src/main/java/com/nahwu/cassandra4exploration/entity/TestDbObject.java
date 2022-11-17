package com.nahwu.cassandra4exploration.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Data
@Table
public class TestDbObject {

    @PrimaryKeyColumn(
            name = "id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private UUID id;

    @PrimaryKeyColumn(
            name = "date",
            ordinal = 0,
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    @Column("date")
    @JsonProperty("date")
    private Date date;

    @Column
    private String item;

    @Column
    private String category;

    @Column
    private String payer;

    @Column
    private String receiver;

    @Column
    private Double amount;

    @Column
    private String imageUrl;

    public TestDbObject(UUID id, Date date, String item, String category, String payer, String receiver, Double amount, String imageUrl) {
        this.id = id;
        this.date = date;
        this.item = item;
        this.category = category;
        this.payer = payer;
        this.receiver = receiver;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }
}
