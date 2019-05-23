package com.goodsogood.ows.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.helper.MD5Utils;
import com.goodsogood.ows.helper.RandomUtils;
import com.goodsogood.ows.mapper.*;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.UserInfoVo;
import com.goodsogood.ows.model.vo.UsersForm;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/***
 * 用户操作
 * */
@Service
@Log4j2
public class UsersService {

    private UsersMapper mapper;
    private AccountsMapper amapper;
    private AccountsUsersRolesMapper aurmapper;
    private CodeMapper cmapper;
    private SmssMapper smssMapper;

    @Autowired
    public UsersService(UsersMapper usersMapper, AccountsMapper accountsMapper, AccountsUsersRolesMapper accountsUsersRolesMapper,
                        CodeMapper codeMapper, SmssMapper ssMapper) {
        this.mapper = usersMapper;
        this.amapper = accountsMapper;
        this.aurmapper = accountsUsersRolesMapper;
        this.cmapper = codeMapper;
        this.smssMapper = ssMapper;
    }

    /**
     * 注册认证资料
     */
    public Long Registration(UsersEntity usersEntity) {
        return this.mapper.Insert(usersEntity);
    }

    /**
     * 注册账号
     */
//    public Long AccountRegistration(AccountsEntity accountsEntity) {
//        return this.amapper.Insert(accountsEntity);
//    }
    @Transactional
    public Boolean AdminRegister(UsersForm user) {

        //查询账号是否已经注册
        AccountsEntity entituy = this.amapper.GetByPhone(user.getPhone());
        Long AccountId;
        if (entituy != null) {
            AccountId = entituy.getAccountId();
        } else {
            entituy = new AccountsEntity();
            entituy.setAddtime(new Date());
            entituy.setPhone(user.getPhone());
            entituy.setPassWordLaws(user.getPassword());
            entituy.setPassWord(MD5Utils.MD5(user.getPassword()));
            entituy.setEnable(1);
            this.amapper.Insert(entituy);
            AccountId = entituy.getAccountId();
        }
        if (AccountId == null) {
            return false;
        }

        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setUserName(user.getUserName());
        entity.setAddtime(new Date());
        entity.setCode(GetCode());
        entity.setCompanyCode(user.getCompanyCode());
        entity.setCompanyName(user.getCompanyName());
        entity.setEnable(1);
        entity.setIsReferrer(user.getIsReferrer());
        entity.setOrganizationCode(user.getOrganizationCode());
        entity.setOrganizationName(user.getOrganizationName());
        entity.setReferrer(user.getReferrer());
        entity.setReview(2);
        entity.setUpdatetime(new Date());
        entity.setUserBankCardNumber(user.getUserBankCardNumber());
        entity.setUserCardholderIdcard(user.getUserCardholderIdCard());
        entity.setUserCardholderName(user.getUserCardholderName());
        entity.setUserCardholderPhone(user.getUserCardholderPhone());
        entity.setUserDepartment(user.getUserDepartment());
        entity.setUserEmail(user.getUserEmail());
        entity.setUserHospital(user.getUserHospital());
        entity.setUserPosition(user.getUserPosition());
        Long userid = this.mapper.Insert(entity);
        userid = entity.getUserId();
        if (userid == null) {
            return false;
        }
        System.out.println("AccountId:" + AccountId + ",userid:" + userid);
        //添加关联
        AccountsUsersRolesEntity usersRolesEntity = new AccountsUsersRolesEntity();
        usersRolesEntity.setAccountId(AccountId);
        usersRolesEntity.setRoleId(user.getRoleId());
        usersRolesEntity.setUserId(userid);
        Long aurid = this.aurmapper.RewriteInsert(usersRolesEntity);
        if (aurid == null) {
            return false;
        }
        return true;
    }

    /**
     * 根据账号查询
     */
    public AccountsEntity Get(String phone) {
        return this.amapper.GetByPhone(phone);
    }


    /**
     * 注册
     */
    @Transactional
    public Boolean Register(UsersForm user) {
        //校验手机验证码
        SmssEntity sms = this.smssMapper.GetByPhone(user.getPhone(), new Date(), 1);
        if (sms == null) {
            return false;
        }
        if (sms.getSmsCode() != user.getPhoneCode()) {
            return false;
        }
        this.smssMapper.Update(user.getPhone(), new Date());
        //查询账号是否已经注册
        AccountsEntity entitys = this.amapper.GetByPhone(user.getPhone());
        Long AccountId = null;
        if (entitys != null) {
            AccountId = entitys.getAccountId();
        } else {
            entitys = new AccountsEntity();
            entitys.setAddtime(new Date());
            entitys.setPhone(user.getPhone());
            entitys.setPassWordLaws(user.getPassword());
            entitys.setPassWord(MD5Utils.MD5(user.getPassword()));
            AccountId = this.amapper.Insert(entitys);
            AccountId = entitys.getAccountId();
        }
        if (AccountId == null) {
            return false;
        }
        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setUserName(user.getUserName());
        entity.setAddtime(new Date());
        entity.setCode(GetCode());
        entity.setCompanyCode(user.getCompanyCode());
        entity.setCompanyName(user.getCompanyName());
        entity.setEnable(1);
        entity.setIsReferrer(user.getIsReferrer());
        entity.setOrganizationCode(user.getOrganizationCode());
        entity.setOrganizationName(user.getOrganizationName());
        entity.setReferrer(user.getReferrer());
        entity.setReview(user.getReview());
        entity.setUpdatetime(new Date());
        entity.setUserBankCardNumber(user.getUserBankCardNumber());
        entity.setUserCardholderIdcard(user.getUserCardholderIdCard());
        entity.setUserCardholderName(user.getUserCardholderName());
        entity.setUserCardholderPhone(user.getUserCardholderPhone());
        entity.setUserDepartment(user.getUserDepartment());
        entity.setUserEmail(user.getUserEmail());
        entity.setUserHospital(user.getUserHospital());
        entity.setUserPosition(user.getUserPosition());
        Long userid = this.mapper.Insert(entity);
        userid = entity.getUserId();
        if (userid == null) {
            return false;
        }
        //添加关联
        AccountsUsersRolesEntity UsersRolesEntity = new AccountsUsersRolesEntity();
        UsersRolesEntity.setAccountId(AccountId);
        UsersRolesEntity.setRoleId(user.getRoleId());
        UsersRolesEntity.setUserId(userid);
        Long aurid = this.aurmapper.RewriteInsert(UsersRolesEntity);
        if (aurid == null) {
            return false;
        }
        return true;
    }


    /**
     * 生成一个随机唯一邀请码
     */
    private String GetCode() {
        String code;
        while (true) {
            code = RandomUtils.generateWord();
            CodeEntity codeEntity = this.cmapper.Get(code);
            if (codeEntity == null) {
                break;
            }
        }
        CodeEntity codeEntity = new CodeEntity();
        codeEntity.setCode_code(code);
        this.cmapper.insert(codeEntity);
        return code;
    }

    /**
     * 修改个人信息
     */
    public Integer Update(UsersEntity usersEntity) {
        return this.mapper.updateByPrimaryKeySelective(usersEntity);
    }

    /**
     * 修改密码
     */
    public Integer UpdatePassWord(String Phone, String pwd, String code) {

        SmssEntity sms = this.smssMapper.GetByPhone(Phone, new Date(), 2);
        if (sms.getSmsCode() != code) {
            return -1;
        }
        return this.amapper.UpdatePwd(Phone, pwd, MD5Utils.MD5(pwd));
    }

    /**
     * 登录后修改密码
     */
    public Integer UpdatePassWord(Long userId, String pwd) {
        return this.amapper.UpdatePassword(userId, pwd, MD5Utils.MD5(pwd));
    }

    /**
     * 添加发送记录
     */
    public SmssEntity SmsInsert(SmssEntity smssEntity, Integer type) {
        SmssEntity sms = this.smssMapper.GetByPhone(smssEntity.getSmsPhone(), smssEntity.getAddtime(), type);
        if (sms != null) {
            sms.setSmsSendFrequency(sms.getSmsSendFrequency() + 1);
            this.smssMapper.updateByPrimaryKey(sms);
        } else {
            this.smssMapper.Insert(smssEntity);
        }
        return smssEntity;
    }

    /**
     * 管理员查询所有账户
     *
     * @return
     */
    public PageInfo<UserInfoVo> GetAll(PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByAll());
    }

    /**
     * 根据角色查询所有用户
     *
     * @param rId 角色唯一标识
     * @param pageNumber
     * @return
     */
    public PageInfo<UserInfoVo> GetByRole(Long rId, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByRoleAll(rId));
    }


    /**
     * 查询单个用户信息
     *
     * @param userId 用户唯一标识
     * @return
     */
    public UserInfoVo GetByUser(Long userId) {
        return this.mapper.GetUserById(userId);
    }

    /**
     * 封禁账号(2)or 解封账号(1)
     *
     * @param userId 账号唯一标识
     * @return
     */
    public Boolean sealByUser(Long userId, Integer seal) {
        return this.mapper.UpdateForbidden(userId, seal) > 0;
    }

}
