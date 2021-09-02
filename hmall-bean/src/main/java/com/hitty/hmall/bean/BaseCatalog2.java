package com.hitty.hmall.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class BaseCatalog2 implements Serializable {

    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String Catalog1Id;
}
