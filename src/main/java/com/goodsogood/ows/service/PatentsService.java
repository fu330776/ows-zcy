package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.PatentsMapper;
import com.goodsogood.ows.model.db.PatentsEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PatentsService {
    private PatentsMapper mapper;

    public PatentsService(PatentsMapper patentsMapper) {
        this.mapper = patentsMapper;
    }

    /**
     * 申请专利
     */
    public Boolean Insert(PatentsEntity patentsEntity) {
        int num = this.mapper.Insert(patentsEntity);
        return num > 0 ? true : false;
    }





}
