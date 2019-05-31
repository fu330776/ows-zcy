package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.DataMapper;
import com.goodsogood.ows.model.db.DataEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class DataService {

    private DataMapper mapper;

    @Autowired
    public DataService(DataMapper dataMapper) {
        this.mapper = dataMapper;
    }

    /**
     * 修改变量
     *
     * @param type
     * @return
     */
    public Integer Update(Integer type) {
        DataEntity entity = this.mapper.Get(type);
        Integer num = null;
        if (entity == null) {
            num = this.mapper.UpdateInit(type, new Date());
        } else {
            num = this.mapper.Update(type);
        }

        return num;
    }

    /**
     * 查询 单个实体(当天)
     *
     * @param type
     * @return
     */
    public DataEntity Get(Integer type) {
        return this.mapper.Get(type);
    }

    /**
     * 查询 所有 （当天）
     *
     * @return
     */
    public List<DataEntity> GetAll() {
        return this.mapper.GetAll();
    }


}
