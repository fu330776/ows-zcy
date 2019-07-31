package com.goodsogood.ows.controller;


import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.DataEntity;
import com.goodsogood.ows.model.db.InstructionsEntity;
import com.goodsogood.ows.model.vo.AccountUsersVo;
import com.goodsogood.ows.model.vo.InstructionsForm;
import com.goodsogood.ows.model.vo.LoginResult;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.AccountsUsersRolesService;
import com.goodsogood.ows.service.DataService;
import com.goodsogood.ows.service.InstructionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.RecursiveTask;

@RestController
@RequestMapping("/v-home")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "首页", tags = {"Home"})
public class HomeController {

    private final Errors errors;
    private final DataService service;
    private final AccountsUsersRolesService accountsUsersRolesService;
    private  final InstructionsService   instructionsService;

    @Autowired
    public HomeController(Errors errors, DataService dataService, AccountsUsersRolesService accountsUsersRolesService,InstructionsService   instructionsService) {
        this.errors = errors;
        this.service = dataService;
        this.accountsUsersRolesService = accountsUsersRolesService;
        this.instructionsService=instructionsService;
    }

    /**
     * 根据类型查询统计数
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "根据类型查询统计数")
    @GetMapping("/get/{type}")
    public ResponseEntity<Result<DataEntity>> get(@ApiParam(value = "type", required = true)
                                                  @PathVariable Integer type) {
        DataEntity entity = this.service.Get(type);
        Result<DataEntity> result = new Result<>(entity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 查询 所有统计
     *
     * @return
     */
    @ApiOperation(value = "查询所有统计")
    @GetMapping("/getAll")
    public ResponseEntity<Result<List<DataEntity>>> getAll() {
        List<DataEntity> entity = this.service.GetAll();
        Result<List<DataEntity>> result = new Result<>(entity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  用户统计
     * @return
     */
    @ApiOperation(value = "查询统计用户")
    @GetMapping("/getUserAll")
    public ResponseEntity<Result<List<AccountUsersVo>>> GetUserAll() {
        List<AccountUsersVo> entity = this.accountsUsersRolesService.GetCount();
        Result<List<AccountUsersVo>> result = new Result<>(entity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  修改操作说明
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改操作说明")
    @PostMapping("/Edit")
    public  ResponseEntity<Result<LoginResult>> Edit(@Valid @RequestBody InstructionsForm form, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        InstructionsEntity entity=new InstructionsEntity();
        entity.setId(1);
        entity.setInstructions(form.getText());
        LoginResult loginResult=this.instructionsService.Update(entity);
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  查询操作说明
     * @return
     */
    @ApiOperation(value = "查询操作说明")
    @GetMapping("/GetExplain")
    public  ResponseEntity<Result<LoginResult>> GetExplain()
    {
        LoginResult loginResult=this.instructionsService.Get();
        Result<LoginResult> result = new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }


}
