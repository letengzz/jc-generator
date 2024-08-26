package com.jc.generator.model;

import lombok.Data;

@Data
public class ColumnClass {
    //属性名
    private String propertyName;
    //字段名
    private String columnName;
    //字段类型
    private String type;
    //字段注释
    private String remark;
    //是否主键
    private boolean isPrimary;
}
