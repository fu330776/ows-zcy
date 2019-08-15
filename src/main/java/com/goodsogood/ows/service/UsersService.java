package com.goodsogood.ows.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.helper.MD5Utils;
import com.goodsogood.ows.helper.RandomUtils;
import com.goodsogood.ows.mapper.*;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.*;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Select;
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
    private  UserMenuMapper ummapper;
    @Autowired
    public UsersService(UsersMapper usersMapper, AccountsMapper accountsMapper, AccountsUsersRolesMapper accountsUsersRolesMapper,
                        CodeMapper codeMapper, SmssMapper ssMapper,UserMenuMapper userMenuMapper) {
        this.mapper = usersMapper;
        this.amapper = accountsMapper;
        this.aurmapper = accountsUsersRolesMapper;
        this.cmapper = codeMapper;
        this.smssMapper = ssMapper;
        this.ummapper = userMenuMapper;
    }

    /**
     * 注册认证资料
     */
    public Long Registration(UsersEntity usersEntity) {
        return this.mapper.Insert(usersEntity);
    }

    /**
     * 判断 邀请码是否生效
     *
     * @param code
     * @return
     */
    public int VerificationCode(String code) {
        return this.mapper.GetCodeCount(code);
    }

    /**
     * 保存用户唯一 openID
     *
     * @param id
     * @param openid
     * @return
     */
    public int updateOpen(Long id, String openid) {
        return this.mapper.updateOpenId(openid, id);
    }

    /**
     * 查询用户openid
     *
     * @param id
     * @return openid
     */
    public String getOpenId(Long id) {
        return this.mapper.getOpenId(id);
    }

    /**
     * 注册子账户
     *
     * @param user
     * @return
     */
    @Transactional
    public Boolean SonAdminRegister(SonUserForm user) {
        int nums = this.mapper.GetByPhone(user.getPhone());
        if (nums > 0) {
            return false;
        }
        Boolean isb = false;
        AccountsEntity entity = this.Get(user.getPhone());
        UsersEntity users = this.mapper.selectByPrimaryKey(user.getUserId());
        if (entity == null) {
            entity = new AccountsEntity();
            entity.setAddtime(new Date());
            entity.setPhone(user.getPhone());
            entity.setPassWordLaws(user.getPassword());
            entity.setPassWord(MD5Utils.MD5(user.getPassword()));
            entity.setEnable(1);
            this.amapper.Insert(entity);
            Long accountId = entity.getAccountId();
            if (accountId == null) {
                return false;
            }
            //注册资料
            UsersEntity entitys = new UsersEntity();
            entitys.setUserName(user.getUserName());
            entitys.setAddtime(new Date());
            entitys.setCode(GetCode());
            entitys.setCompanyCode(users.getCompanyCode());
            entitys.setCompanyName(users.getCompanyName());
            entitys.setProvinces(user.getProvinces());
            entitys.setMunicipalities(user.getMunicipalities());
            entitys.setDistricts(user.getDistricts());
            entitys.setGrade(user.getGrade());
            entitys.setNature(user.getNature());
            entitys.setTitle(user.getTitle());
            entitys.setEnable(1);
            entitys.setIsReferrer(1);
            entitys.setOrganizationCode("");
            entitys.setOrganizationName("");
            entitys.setReferrer(user.getReferrer());
            entitys.setReview(2);
            entitys.setUpdatetime(new Date());
            entitys.setUserEmail(user.getUserEmail());
            entitys.setUserHospital(user.getUserHospital());
            entitys.setUserPosition(user.getUserPosition());
            entitys.setUserBankCardNumber(user.getUserBankCardNumber());
            entitys.setUserCardholderIdcard(user.getUserCardholderIdCard());
            entitys.setUserCardholderName(user.getUserCardholderName());
            entitys.setUserCardholderPhone(user.getUserCardholderPhone());
            entitys.setUserDepartment(user.getUserDepartment());
            entitys.setPhone(user.getPhone());
            entitys.setIssub(user.getIssub() == null ? 0 : 1);
            entitys.setContact_phone(user.getContact_phone());
            entitys.setContacts(user.getContacts());
            entitys.setDetailed_address(user.getDetailed_address());
            entitys.setBusiness_license(user.getBusiness_license());

            Long userid = this.mapper.Insert(entitys);
            userid = entitys.getUserId();
            if (userid == null) {
                return false;
            }

            //添加关联
            AccountsUsersRolesEntity Entitys = new AccountsUsersRolesEntity();
            Entitys.setAccountId(accountId);
            Entitys.setRoleId(user.getRoleId());
            Entitys.setUserId(userid);
            Long aurId = this.aurmapper.RewriteInsert(Entitys);
            if (aurId == null) {
                return false;
            }
            return true;

        }

        Long userId = this.aurmapper.GetAdminFind(entity.getPhone(), entity.getPassWord());
        UsersEntity usersEntity = this.mapper.selectByPrimaryKey(userId);
        usersEntity.setCompanyCode(users.getCompanyCode());
        usersEntity.setCompanyName(users.getCompanyName());
        int num = this.mapper.updateByPrimaryKey(usersEntity);
        if (num > 0) {
            isb = true;
        }
        return isb;
    }


    /**
     * 注册账号
     */
    @Transactional
    public Boolean AdminRegister(UsersForm user) {
        int nums = this.mapper.GetByPhone(user.getPhone());
        if (nums > 0) {
            return false;
        }
        //查询账号是否已经注册
        AccountsEntity entitys = this.amapper.GetByPhone(user.getPhone());
        Long accountId;
        if (entitys != null) {
            accountId = entitys.getAccountId();
        } else {
            entitys = new AccountsEntity();
            entitys.setPassWordLaws(user.getPassword());
            entitys.setPassWord(MD5Utils.MD5(user.getPassword()));
            entitys.setEnable(1);
            entitys.setAddtime(new Date());
            entitys.setPhone(user.getPhone());
            this.amapper.Insert(entitys);
            accountId = entitys.getAccountId();
        }
        if (accountId == null) {
            return false;
        }
        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setCompanyCode(user.getCompanyCode());
        entity.setCompanyName(user.getCompanyName());
        entity.setEnable(1);
        entity.setIsReferrer(1);
        entity.setUserName(user.getUserName());
        entity.setAddtime(new Date());
        entity.setCode(GetCode());

        entity.setOrganizationCode(user.getOrganizationCode());
        entity.setOrganizationName(user.getOrganizationName());
        entity.setReferrer(user.getReferrer());
        entity.setReview(2);
        entity.setUserDepartment(user.getUserDepartment());
        entity.setUpdatetime(new Date());
        entity.setUserCardholderName(user.getUserCardholderName());
        entity.setUserCardholderPhone(user.getUserCardholderPhone());
        entity.setUserBankCardNumber(user.getUserBankCardNumber());
        entity.setUserCardholderIdcard(user.getUserCardholderIdCard());
        entity.setUserPosition(user.getUserPosition());
        entity.setPhone(user.getPhone());
        entity.setProvinces(user.getProvinces());
        entity.setUserEmail(user.getUserEmail());
        entity.setUserHospital(user.getUserHospital());
        entity.setMunicipalities(user.getMunicipalities());
        entity.setDistricts(user.getDistricts());
        entity.setGrade(user.getGrade());
        entity.setNature(user.getNature());
        entity.setTitle(user.getTitle());
        entity.setIssub(user.getIssub() == null ? 0 : user.getIssub());
        entity.setDetailed_address(user.getDetailed_address());
        entity.setBusiness_license(user.getBusiness_license());
        entity.setContact_phone(user.getContact_phone());
        entity.setContacts(user.getContacts());
        Long userid = this.mapper.Insert(entity);
        userid = entity.getUserId();
        if (userid == null) {
            return false;
        }
        //添加关联
        AccountsUsersRolesEntity usersRolesEntity = new AccountsUsersRolesEntity();
        usersRolesEntity.setAccountId(accountId);
        usersRolesEntity.setRoleId(user.getRoleId());
        usersRolesEntity.setUserId(userid);
        Long aurId = this.aurmapper.RewriteInsert(usersRolesEntity);
        if (aurId == null) {
            return false;
        }
        return true;
    }

    /**
     * 导入批量创建账户
     *
     * @param users
     * @return
     */
    @Transactional
    public Boolean ImportAdminExecl(List<UsersForm> users) {

        for (UsersForm user : users) {
            int nums = this.mapper.GetByPhone(user.getPhone());
            if (nums > 0) {
                return false;
            }
        }

        for (UsersForm user : users) {

            //查询账号是否已经注册
            AccountsEntity entitys = this.amapper.GetByPhone(user.getPhone());
            Long accountId;
            if (entitys != null) {
                accountId = entitys.getAccountId();
                int num = this.aurmapper.GetIsNum(accountId, user.getRoleId());
                if (num > 0) {
                    return false;
                }
            } else {
                entitys = new AccountsEntity();
                entitys.setPassWord(MD5Utils.MD5(user.getPassword()));
                entitys.setEnable(1);
                entitys.setAddtime(new Date());
                entitys.setPhone(user.getPhone());
                entitys.setPassWordLaws(user.getPassword());
                this.amapper.Insert(entitys);
                accountId = entitys.getAccountId();
            }


            //注册资料
            UsersEntity entity = new UsersEntity();
            entity.setReview(2);
            entity.setUserDepartment(user.getUserDepartment());
            entity.setUpdatetime(new Date());
            entity.setCompanyCode(user.getCompanyCode());
            entity.setUserName(user.getUserName());
            entity.setAddtime(new Date());
            entity.setCode(GetCode());
            entity.setCompanyName(user.getCompanyName());
            entity.setEnable(1);
            entity.setIsReferrer(1);
            entity.setOrganizationCode(user.getOrganizationCode());
            entity.setOrganizationName(user.getOrganizationName());
            entity.setReferrer(user.getReferrer());
            entity.setUserBankCardNumber(user.getUserBankCardNumber());
            entity.setUserCardholderIdcard(user.getUserCardholderIdCard());
            entity.setUserPosition(user.getUserPosition());
            entity.setPhone(user.getPhone());
            entity.setUserCardholderName(user.getUserCardholderName());
            entity.setUserCardholderPhone(user.getUserCardholderPhone());
            entity.setDistricts(user.getDistricts());
            entity.setGrade(user.getGrade());
            entity.setNature(user.getNature());
            entity.setTitle(user.getTitle());
            entity.setIssub(user.getIssub());
            entity.setProvinces(user.getProvinces());
            entity.setUserEmail(user.getUserEmail());
            entity.setUserHospital(user.getUserHospital());
            entity.setMunicipalities(user.getMunicipalities());
            entity.setDetailed_address(user.getDetailed_address());
            entity.setBusiness_license(user.getBusiness_license());
            entity.setContact_phone(user.getContact_phone());
            entity.setContacts(user.getContacts());
            Long userId = this.mapper.Insert(entity);
            userId = entity.getUserId();
            //添加关联
            AccountsUsersRolesEntity usersRolesEntity = new AccountsUsersRolesEntity();
            usersRolesEntity.setRoleId(user.getRoleId());
            usersRolesEntity.setUserId(userId);
            usersRolesEntity.setAccountId(accountId);
            Long aurId = this.aurmapper.RewriteInsert(usersRolesEntity);
        }
        return true;
    }


    @Transactional
    public LoginResult AddAdministrator(UsersForm user) {
        LoginResult result = new LoginResult();
        try {
            int num = this.mapper.GetAdminPhone(user.getPhone());
            if (num > 0) {
                result.setIsb(false);
                result.setMsg("账号已存在，不可注册为管理员");
                return result;
            }

            Long AccountId;
            AccountsEntity entitys = this.amapper.GetByPhone(user.getPhone());
            if (entitys == null) {
                entitys = new AccountsEntity();
                entitys.setAddtime(new Date());
                entitys.setPassWordLaws(user.getPassword());
                entitys.setPassWord(MD5Utils.MD5(user.getPassword()));
                entitys.setPhone(user.getPhone());
                this.amapper.Insert(entitys);
                AccountId = entitys.getAccountId();
            } else {
                AccountId = entitys.getAccountId();
            }


            if (AccountId == null) {
                result.setIsb(false);
                result.setMsg("账号创建失败");
                return result;
            }
            //注册资料
            Long userid;
            UsersEntity entity = new UsersEntity();
            entity.setCompanyCode(user.getCompanyCode());
            entity.setCompanyName(user.getCompanyName());
            entity.setDetailed_address(user.getDetailed_address());
            entity.setBusiness_license(user.getBusiness_license());
            entity.setIsReferrer(2);
            entity.setOrganizationCode(user.getOrganizationCode());
            entity.setOrganizationName(user.getOrganizationName());
            entity.setContact_phone(user.getContact_phone());
            entity.setContacts(user.getContacts());
            entity.setUserName(user.getUserName());
            entity.setAddtime(new Date());
            entity.setCode(GetCode());
            entity.setEnable(1);
            entity.setIssub(2); //子超级管理
            entity.setProvinces(user.getProvinces());
            entity.setMunicipalities(user.getMunicipalities());
            entity.setDistricts(user.getDistricts());
            entity.setGrade(user.getGrade());
            entity.setUserCardholderPhone(user.getUserCardholderPhone());
            entity.setUserDepartment(user.getUserDepartment());
            entity.setUserEmail(user.getUserEmail());
            entity.setUserHospital(user.getUserHospital());
            entity.setUserPosition(user.getUserPosition());
            entity.setPhone(user.getPhone());
            entity.setReferrer(user.getReferrer());
            entity.setReview(2);
            entity.setUpdatetime(new Date());
            entity.setUserBankCardNumber(user.getUserBankCardNumber());
            entity.setUserCardholderIdcard(user.getUserCardholderIdCard());
            entity.setUserCardholderName(user.getUserCardholderName());
            entity.setNature(user.getNature());
            entity.setTitle(user.getTitle());
            this.mapper.Insert(entity);
            userid = entity.getUserId();
            //添加关联
            AccountsUsersRolesEntity UsersRolesEntity = new AccountsUsersRolesEntity();
            UsersRolesEntity.setUserId(userid);
            UsersRolesEntity.setAccountId(AccountId);
            UsersRolesEntity.setRoleId(1L);
            Long aurid = this.aurmapper.RewriteInsert(UsersRolesEntity);
            if (aurid == null) {
                result.setMsg("注册失败");
                return result;
            }
            result.setIsb(true);
            result.setMsg("注册成功");
        } catch (Exception ex) {
            result.setIsb(false);
            result.setMsg(ex.getMessage());
        }
        return result;
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
    public LoginResult Register(UsersForm user) {
        int num = this.mapper.GetPhone(user.getPhone());
        LoginResult result = new LoginResult();
        result.setIsb(false);
        if (num > 0) {
            result.setMsg("账号已存在");
            return result;
        }
        //校验手机验证码
        SmssEntity sms = this.smssMapper.GetByPhone(user.getPhone(), new Date(), 1);
        if (sms == null) {
            result.setMsg("验证码不存在");
            return result;
        }
        if (!sms.getSmsCode().equals(user.getPhoneCode())) {
            result.setMsg("验证码错误");
            return result;
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
            this.amapper.Insert(entitys);
            AccountId = entitys.getAccountId();
        }
        //注册资料
        UsersEntity entity = new UsersEntity();
        entity.setDetailed_address(user.getDetailed_address());
        entity.setBusiness_license(user.getBusiness_license());
        entity.setContact_phone(user.getContact_phone());
        entity.setContacts(user.getContacts());
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
        entity.setPhone(user.getPhone());
        entity.setIssub(user.getIssub() == null ? 0 : user.getIssub());
        entity.setProvinces(user.getProvinces());
        entity.setMunicipalities(user.getMunicipalities());
        entity.setDistricts(user.getDistricts());
        entity.setGrade(user.getGrade());
        entity.setNature(user.getNature());
        entity.setTitle(user.getTitle());
        this.mapper.Insert(entity);
        Long userid = entity.getUserId();
        if (userid == null) {
            result.setMsg("注册错误");
            return result;
        }
        //添加关联
        AccountsUsersRolesEntity UsersRolesEntity = new AccountsUsersRolesEntity();
        UsersRolesEntity.setAccountId(AccountId);
        UsersRolesEntity.setRoleId(user.getRoleId());
        UsersRolesEntity.setUserId(userid);
        Long aurid = this.aurmapper.RewriteInsert(UsersRolesEntity);
        if (aurid == null) {
            result.setMsg("注册错误");
            return result;
        }
        result.setIsb(true);
        result.setMsg("注册成功");
        return result;
    }

    /**
     * 账号是否注册验证
     *
     * @param phone
     * @return
     */
    public LoginResult IsPhone(String phone) {
        LoginResult result = new LoginResult();
        result.setIsb(false);
        result.setMsg("账号未注册");
        int nums = this.smssMapper.NowGetCount(phone);
        if (nums > 10) {
            result.setMsg("手机号当天短信数已达到上限");
            return result;
        }
        int num = this.mapper.GetPhone(phone);
        if (num > 0) {
            result.setMsg("账号已注册");
            return result;
        }
        return result;
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
        this.cmapper.Insert(codeEntity);
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
        if (sms == null) {
            return null;
        }
        if (!sms.getSmsCode().equals(code)) {
            return null;
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
            sms.setSmsUse(1);
            this.smssMapper.updateByPrimaryKey(sms);
        } else {
            this.smssMapper.Insert(smssEntity);
        }
        return smssEntity;
    }

    /**
     * 查询
     *
     * @param phone
     * @param type
     * @return
     */
    public SmssEntity SmsGet(String phone, Integer type) {
        SmssEntity sms = this.smssMapper.GetByPhone(phone, new Date(), type);
        return sms;
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
     * @param rId        角色唯一标识
     * @param pageNumber
     * @return
     */
    public PageInfo<UserInfoVo> GetByRole(Long rId, String name, String provinces, String municipalities, String districts, String grade, String nature, String Keyword, Integer review, Integer enable, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByRoleAll(rId, name, provinces, municipalities, districts, grade, nature, Keyword, review, enable));
    }

    /**
     * 查询所有用户 不分页
     *
     * @param rid
     * @return
     */
    public List<UserInfoVo> GetByRoleNoPage(Long rid) {
        return this.mapper.GetByRoleAll(rid, null, null, null, null, null, null, null, null, null);
    }

    /***
     *  查询 子管理员
     * @param name
     * @param pageNumber
     * @return
     */
    public PageInfo<UserInfoVo> GetAdministrator(String name, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetAdministrator(name));
    }


    /**
     * 根据查询条件 导出
     *
     * @param rId
     * @param name
     * @param provinces
     * @param municipalities
     * @param districts
     * @param grade
     * @param nature
     * @param Keyword
     * @return
     */
    public List<UserInfoVo> GetByExport(Long rId, String name, String provinces, String municipalities, String districts, String grade, String nature, String Keyword, Integer review, Integer enable) {
        return this.mapper.GetByRoleAll(rId, name, provinces, municipalities, districts, grade, nature, Keyword, review, enable);
    }


    /**
     * 子管理查询本科院
     *
     * @param provinces
     * @param municipalities
     * @param districts
     * @param grade
     * @param nature
     * @param Keyword
     * @param pageNumber
     * @return
     */
    public PageInfo<UserInfoVo> GetByIssue(Long userId, String provinces, String municipalities, String districts, String grade, String nature, String Keyword, Integer review, Integer enable, PageNumber pageNumber) {
        UserInfoVo vo = this.mapper.GetUserById(userId);
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByIssue(vo.getCompanyCode(), provinces, municipalities, districts, grade, nature, Keyword, review, enable));
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

    /**
     * 账号通过审核
     *
     * @param userId
     * @return
     */
    public Boolean ExamineService(Long userId, Integer isdel) {
        Integer num = this.mapper.Examine(userId, isdel);
        if (num == null || num <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 查询该账号是否存在状态
     *
     * @param userid
     * @return
     */
    public boolean GetCount(Long userid) {
        int num = this.mapper.GetCount(userid);
        if (num == 0) {
            return false;
        }
        return true;

    }

    @Transactional
    public LoginResult DeleteHealthCare(Long userId) {
        LoginResult result = new LoginResult();
        result.setIsb(false);
        result.setMsg("删除失败");
        // 第一步 查询 该账号是否存在多个角色
        List<AccountsUsersRolesEntity> accountsUsersRolesEntityList = this.aurmapper.GET(userId);
        // 第二步 多个角色 不删除 账号
        if (accountsUsersRolesEntityList.size() > 1) {
            AccountsUsersRolesEntity accountsUsersRolesEntity;
            for (int i = 0; i < accountsUsersRolesEntityList.size(); i++) {
                accountsUsersRolesEntity = accountsUsersRolesEntityList.get(i);
                if (accountsUsersRolesEntity.getUserId().equals(userId)) {
                    int num = this.aurmapper.delete(accountsUsersRolesEntity);
                    if (num > 0) {
                        num = this.mapper.Delete(userId);
                        if (num > 0) {
                            result.setIsb(true);
                            result.setMsg("删除成功");
                            return result;
                        }
                    }
                    break;
                }
            }
        }
        //单个角色删除账号
        else {
            int num = 0;
            num = this.amapper.Delete(accountsUsersRolesEntityList.get(0).getAccountId());
            if (num > 0) {
                num = this.aurmapper.delete(accountsUsersRolesEntityList.get(0));
                if (num > 0) {
                    num = this.mapper.Delete(userId);
                    if (num > 0) {
                        result.setIsb(true);
                        result.setMsg("删除成功");
                        return result;
                    }
                }
            }

            return result;
        }
        return result;
    }


    /***
     *  医护审核人员身份 变更
     * @param userId
     * @param is  1：取消 2：医护审核人员
     * @return
     */
    @Transactional
    public  LoginResult IsAuditors(Long userId,int is){
        LoginResult result = new LoginResult();
        result.setIsb(false);
        result.setMsg("任命失败");
        Integer isb;
        if(is >0 && is < 3){if(is == 2){isb=1;}else{isb=2;}}else {return  result; }

        int num = this.mapper.IsAuditor(userId,is,isb);
        if(num > 0)
        {
            switch (is){
                case 2:
                    UserMenuEntity entity =new UserMenuEntity();
                    entity.setZcyMenuId(56L);
                    entity.setZcyUserId(userId);
                    int nums = this.ummapper.Insert(entity);
                    if(nums >0){
                        result.setIsb(true);
                        result.setMsg("任命成功");
                    }
                    break;
                case 1:
                    int _num = this.ummapper.DelIsAuditor(userId,56L);
                    if(_num >0){
                        result.setIsb(true);
                        result.setMsg("任命取消成功");
                    }
                    break;
            }
        }
        return  result;
    }

    /**
     *  根据邀请人编码查询
     * @param userID  用户唯一标识
     * @param userRoleForm 查询条件
     * @param pageNumber
     * @return
     */
    public  PageInfo<UserInfoVo> GetByInvitation(Long userID,UserRoleForm userRoleForm, PageNumber pageNumber)
    {
        UserInfoVo vo=  this.mapper.GetUserById(userID);
        if(vo == null){return  null;}
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return  new PageInfo<>(this.mapper.GetByInvitation(vo.getReferrer(),userRoleForm.getProvinces(),userRoleForm.getMunicipalities(),userRoleForm.getDistricts(),userRoleForm.getGrade(),userRoleForm.getNature(),userRoleForm.getKeyword(),userRoleForm.getReview(),userRoleForm.getEnable()));
    }

}
