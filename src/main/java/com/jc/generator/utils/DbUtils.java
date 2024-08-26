package com.jc.generator.utils;

import com.jc.generator.model.Vo.DbVo;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库工具类
 * @author hjc
 */
public class DbUtils {
    @Getter
    private static Connection connection;

    public static Connection initDb(DbVo dbVo){
        if (connection == null){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dbVo.getDbUrl(),dbVo.getUsername(),dbVo.getPassword());
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
