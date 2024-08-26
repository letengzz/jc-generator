package com.jc.generator.controller;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Table;
import com.jc.generator.common.req.RestBean;
import com.jc.generator.model.TableClass;
import com.jc.generator.model.Vo.DbVo;
import com.jc.generator.utils.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

/**
 * 数据库操作
 * @author hjc
 */
@Slf4j
@RestController
@RequestMapping("/db")
public class DbController {
    @PostMapping("connect")
    public RestBean<String> connect(@RequestBody DbVo dbVo) {
        log.info("数据库连接信息:{}",dbVo);
        dbVo.setDbUrl("jdbc:mysql://"+dbVo.getDbUrl()+"/"+dbVo.getDbName());
        Connection connection = DbUtils.initDb(dbVo);
        if (connection != null) {
            return RestBean.success("数据库连接成功",null);
        } else {
            return RestBean.failure(400,"数据库连接失败");
        }
    }

    @PostMapping("config")
    public RestBean<Object> config(@RequestBody Map<String,String> map) {
        String packageName = map.get("packageName");
        try {
            Connection connection = DbUtils.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, null);
            ArrayList<TableClass> tableClasses = new ArrayList<>();
            while (tables.next()) {
                TableClass tableClass = new TableClass();
                tableClass.setPackageName(packageName);
                String tableName = tables.getString("TABLE_NAME");
                String modelName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
                tableClass.setModelName(modelName);
                tableClass.setTableName(tableName);
                tableClass.setControllerName(modelName + "Controller");
                tableClass.setServiceName(modelName + "Service");
                tableClass.setMapperName(modelName + "Mapper");
                tableClasses.add(tableClass);
            }
            return RestBean.success("数据库信息读取成功",tableClasses);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
