package com.jc.generator.service;

import com.jc.generator.common.req.RestBean;
import com.jc.generator.model.TableClass;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hjc
 */
public interface GenerateCodeService {
    RestBean<String> generateCode(List<TableClass> tableClassList, String realPath);
}
