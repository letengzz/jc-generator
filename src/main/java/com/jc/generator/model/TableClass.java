package com.jc.generator.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 表类
 * @author hjc
 */
@Data
public class TableClass {
    //表名
    private String tableName;
    //model类
    private  String modelName;
    //Service类
    private String serviceName;
    //Mapper类
    private String mapperName;
    //Controller类
    private String controllerName;
    //包名
    private String packageName;
    //列名
    private List<ColumnClass> columns;
}
