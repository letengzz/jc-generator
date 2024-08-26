package com.jc.generator.controller;

import com.jc.generator.common.req.RestBean;
import com.jc.generator.model.TableClass;
import com.jc.generator.service.GenerateCodeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 生成代码控制器
 * @author hjc
 */
@RestController
public class GenerateCodeController {
    @Resource
    private GenerateCodeService generateCodeService;

    @PostMapping("/generate")
    public RestBean<String> generateCode(@RequestBody List<TableClass> tableClassList, HttpServletRequest req) {
        return generateCodeService.generateCode(tableClassList,req.getServletContext().getRealPath("/"));
    }
}
