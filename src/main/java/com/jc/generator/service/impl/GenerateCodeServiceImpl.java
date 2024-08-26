package com.jc.generator.service.impl;

import com.google.common.base.CaseFormat;
import com.jc.generator.common.req.RestBean;
import com.jc.generator.model.ColumnClass;
import com.jc.generator.model.TableClass;
import com.jc.generator.service.GenerateCodeService;
import com.jc.generator.utils.DbUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.DoubleStream.generate;

/**
 * @author hjc
 */
@Service
public class GenerateCodeServiceImpl implements GenerateCodeService {
    Configuration cfg = null;

    {
        cfg = new Configuration(Configuration.VERSION_2_3_33);
        //设置加载模板位置
        cfg.setTemplateLoader(new ClassTemplateLoader(GenerateCodeServiceImpl.class, "/templates"));
        //编码
        cfg.setDefaultEncoding("UTF-8");
    }

    @Override
    public RestBean<String> generateCode(List<TableClass> tableClassList, String realPath) {
        try {
            //获取模板
            Template modelTemplate = cfg.getTemplate("Model.java.ftl");
            Template serviceTemplate = cfg.getTemplate("Service.java.ftl");
            Template mapperXmlTemplate = cfg.getTemplate("Mapper.xml.ftl");
            Template mapperJavaTemplate = cfg.getTemplate("Mapper.java.ftl");
            Template controllerTemplate = cfg.getTemplate("Controller.java.ftl");
            Connection connection = DbUtils.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            for (TableClass tableClass : tableClassList) {
                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableClass.getTableName(), null);
                ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, tableClass.getTableName());
                ArrayList<ColumnClass> columnClassList = new ArrayList<>();
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    String remarks = columns.getString("REMARKS");
                    ColumnClass columnClass = new ColumnClass();
                    columnClass.setColumnName(columnName);
                    columnClass.setType(columnType);
                    columnClass.setRemark(remarks);
                    columnClass.setPropertyName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));
                    primaryKeys.first();
                    while (primaryKeys.next()) {
                        String pkName = primaryKeys.getString("COLUMN_NAME");
                        if (columnName.equals(pkName)) {
                            columnClass.setPrimary(true);
                        }
                    }
                    columnClassList.add(columnClass);
                }
                tableClass.setColumns(columnClassList);
                String path = realPath + "/" + tableClass.getPackageName().replace(".", "/");
                generate(modelTemplate,tableClass,path+"/model/");
                generate(mapperXmlTemplate,tableClass,path+"/mapper/");
                generate(mapperJavaTemplate,tableClass,path+"/mapper/");
                generate(serviceTemplate,tableClass,path+"/service/");
                generate(controllerTemplate,tableClass,path+"/controller/");
            }
            return RestBean.success("生成成功",realPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestBean.failure(400,"生成失败");
    }

    private void generate(Template template, TableClass tableClass, String path) throws IOException, TemplateException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = path + "/" + tableClass.getModelName() + template.getName().replace(".ftl", "").replace("Model", "");
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter out = new OutputStreamWriter(fos);
        template.process(tableClass, out);
        fos.close();
    }
}
