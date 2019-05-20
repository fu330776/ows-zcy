package com.goodsogood.ows.service;

import com.github.mustachejava.Code;
import com.goodsogood.ows.helper.RandomUtils;
import com.goodsogood.ows.mapper.CodeMapper;
import com.goodsogood.ows.model.db.CodeEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CodeService {

    private CodeMapper mapper;

    @Autowired
    public CodeService(CodeMapper codeMapper) {
        this.mapper = codeMapper;
    }

    /**
     * 生成一个随机唯一邀请码
     */
    public String GetCode() {
        String code;
        while (true) {
            code = RandomUtils.generateWord();
            CodeEntity codeEntity = this.mapper.Get(code);
            if (codeEntity == null) {
                break;
            }
        }
        CodeEntity codeEntity=new CodeEntity();
        codeEntity.setCode_code(code);
        this.mapper.insert(codeEntity);
        return  code;
    }

}
