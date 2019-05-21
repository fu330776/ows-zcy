package com.goodsogood.ows.service;


import com.goodsogood.ows.helper.MD5Utils;
import com.goodsogood.ows.helper.RandomUtils;
import com.goodsogood.ows.mapper.*;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.UserForm;
import com.goodsogood.ows.model.vo.UserInfoVo;
import com.goodsogood.ows.model.vo.UsersForm;
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
    public Long AccountRegistration(AccountsEntity accountsEntity) {
        return this.amapper.Insert(accountsEntity);
    }

    @Transactional
    public Boolean AdminRegister(UsersForm user) {
        //查询账号是否已经注册
        AccountsEntity accountsEntity = this.amapper.GetByPhone(user.Phone);
        Long AccountId;
        if (accountsEntity != null) {
            AccountId = accountsEntity.getAccountId();
        } else {
            accountsEntity = new AccountsEntity();
            accountsEntity.setAddtime(new Date());
            accountsEntity.setPhone(user.Phone);
            accountsEntity.setPassWordLaws(user.PassWord);
            accountsEntity.setPassWord(MD5Utils.MD5(user.PassWord));
            AccountId = this.amapper.Insert(accountsEntity);
        }

        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setUserName(user.userName);
        entity.setAddtime(new Date());
        entity.setCode(GetCode());
        entity.setCompanyCode(user.companyCode);
        entity.setCompanyName(user.companyName);
        entity.setEnable(1);
        entity.setIsReferrer(user.isReferrer);
        entity.setOrganizationCode(user.organizationCode);
        entity.setOrganizationName(user.organizationName);
        entity.setReferrer(user.referrer);
        entity.setReview(2);
        entity.setUpdatetime(new Date());
        entity.setUserBankCardNumber(user.userBankCardNumber);
        entity.setUserCardholderIdcard(user.userCardholderIdcard);
        entity.setUserCardholderName(user.userCardholderName);
        entity.setUserCardholderPhone(user.userCardholderPhone);
        entity.setUserDepartment(user.userDepartment);
        entity.setUserEmail(user.userEmail);
        entity.setUserHospital(user.userHospital);
        entity.setUserPosition(user.userPosition);
        Long userid = this.mapper.Insert(entity);
        if (userid <= 0L) {
            return false;
        }
        //添加关联
        AccountsUsersRolesEntity accountsUsersRolesEntity = new AccountsUsersRolesEntity();
        accountsUsersRolesEntity.setAccountId(AccountId);
        accountsUsersRolesEntity.setRoleId(user.roleId);
        accountsUsersRolesEntity.setUserId(userid);
        Long aurid = this.aurmapper.RewriteInsert(accountsUsersRolesEntity);
        if (aurid <= 0L) {
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
        SmssEntity sms = this.smssMapper.GetByPhone(user.Phone, new Date(), 1);
        if (sms == null) {
            return false;
        }
        if (sms.getSmsCode() != user.phoneCode) {
            return false;
        }
        this.smssMapper.Update(user.Phone, new Date());
        //查询账号是否已经注册
        AccountsEntity accountsEntity = this.amapper.GetByPhone(user.Phone);
        Long AccountId;
        if (accountsEntity != null) {
            AccountId = accountsEntity.getAccountId();
        } else {
            accountsEntity = new AccountsEntity();
            accountsEntity.setAddtime(new Date());
            accountsEntity.setPhone(user.Phone);
            accountsEntity.setPassWordLaws(user.PassWord);
            accountsEntity.setPassWord(MD5Utils.MD5(user.PassWord));
            AccountId = this.amapper.Insert(accountsEntity);
        }

        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setUserName(user.userName);
        entity.setAddtime(new Date());
        entity.setCode(GetCode());
        entity.setCompanyCode(user.companyCode);
        entity.setCompanyName(user.companyName);
        entity.setEnable(1);
        entity.setIsReferrer(user.isReferrer);
        entity.setOrganizationCode(user.organizationCode);
        entity.setOrganizationName(user.organizationName);
        entity.setReferrer(user.referrer);
        entity.setReview(1);
        entity.setUpdatetime(new Date());
        entity.setUserBankCardNumber(user.userBankCardNumber);
        entity.setUserCardholderIdcard(user.userCardholderIdcard);
        entity.setUserCardholderName(user.userCardholderName);
        entity.setUserCardholderPhone(user.userCardholderPhone);
        entity.setUserDepartment(user.userDepartment);
        entity.setUserEmail(user.userEmail);
        entity.setUserHospital(user.userHospital);
        entity.setUserPosition(user.userPosition);
        Long userid = this.mapper.Insert(entity);
        if (userid <= 0L) {
            return false;
        }
        //添加关联
        AccountsUsersRolesEntity accountsUsersRolesEntity = new AccountsUsersRolesEntity();
        accountsUsersRolesEntity.setAccountId(AccountId);
        accountsUsersRolesEntity.setRoleId(user.roleId);
        accountsUsersRolesEntity.setUserId(userid);
        Long aurid = this.aurmapper.RewriteInsert(accountsUsersRolesEntity);
        if (aurid <= 0L) {
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
    public Integer UpdatePassWord(Long userid, String pwd) {
        return this.amapper.UpdatePassword(userid, pwd, MD5Utils.MD5(pwd));
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
    public List<UserInfoVo> GetAll() {
        return this.mapper.GetByAll();
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
