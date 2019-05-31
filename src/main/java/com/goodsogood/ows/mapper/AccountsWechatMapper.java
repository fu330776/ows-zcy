package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.AccountsWechatEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountsWechatMapper extends MyMapper<AccountsWechatEntity> {


    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts_wechat (",
            "<if test = 'wechatId !=null' > wechat_id ,</if>",
            "account_id,wechart_open_id,wechat_name,wechat_name,addtime)",
            "VALUES(",
            "<if test = 'wechatId !=null' > #{wechatId,jdbcType=BIGINT},</if>",
            "#{accountId,jdbcType=BIGINT},#{wechatOpennId,jdbcType=VARCHAR},",
            "#{wechatName,jdbcType=VARCHAR},#{addtime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "wechatId", keyColumn = "wechat_id")
    int Insert(AccountsWechatEntity entity);
}
