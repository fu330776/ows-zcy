package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.DataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface DataMapper extends MyMapper<DataEntity> {

    @Update({
            "<script>",
            "UPDATE ",
            "zcy_data ",
            "SET ",
            "total=total+1,day_count=day_count+1 ",
            "where",
            "type_type=#{type,jdbcType=BIT}",
            "and ",
            "DATE_FORMAT(current_times,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d')",
            "</script>"
    })
    int Update(@Param(value = "type") Integer type);


    @Update({
            "<script>",
            "UPDATE ",
            "zcy_data ",
            "SET ",
            "total=total+1,day_count=1,current_times=#{time,jdbcType=TIMESTAMP}",
            "where",
            "type_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    int UpdateInit(@Param(value = "type") Integer type, @Param(value = "time") Date time);

    @Select({
            "<script>",
            "SELECT",
            "id,type_name as typeName,type_type as typeType,total,day_count as dayCount,current_times as currentTime,add_time as addTime ",
            "FROM zcy_data",
            "where DATE_FORMAT(current_times,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d')",
            "and type_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    DataEntity Get(@Param(value = "type") Integer type);

    @Select({
            "<script>",
            "SELECT",
            "id,type_name as typeName,type_type as typeType,total,(CASE when DATE_FORMAT(current_times,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') then day_count else 0 END) as dayCount,current_times as currentTime,add_time as addTime ",
            "FROM zcy_data",
            //"",
            "</script>"
    })
    List<DataEntity> GetAll();
}
