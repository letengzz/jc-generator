package com.jc.generator.model.Vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据库Vo
 *
 * @author hjc
 */
@Data
public class DbVo {
    //数据库类型
    @NotNull(message = "数据库类型不能为空")
    private String dbType;

    //数据库名
    private String dbName;

    //数据库url
    @NotNull(message = "数据库url不能为空")
    private String dbUrl;

    //数据库用户名
    @NotNull(message = "数据库用户名不能为空")
    private String username;

    //数据库密码
    @NotNull(message = "数据库密码不能为空")
    private String password;
}
