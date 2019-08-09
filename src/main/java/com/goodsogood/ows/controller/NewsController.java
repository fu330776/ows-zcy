package com.goodsogood.ows.controller;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.NewsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.LoginResult;
import com.goodsogood.ows.model.vo.NewsForm;
import com.goodsogood.ows.model.vo.NewsVo;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v-news")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "最新科研项目", tags = {"news manager"})
public class NewsController {
    private final Errors errors;
    private  final NewsService service;
    @Autowired
    public  NewsController(Errors errors,NewsService newsService) {
        this.errors=errors;
        this.service=newsService;
    }

    /**
     *  添加
     * @param news
     * @param bindingResult
     * @return
     */
    @ApiModelProperty(value = "添加最新科研项目")
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> Add(@Valid @RequestBody NewsForm news, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(news.getContent().isEmpty() || news.getDescribe().isEmpty() || news.getTitle().isEmpty())
        {
            throw new ApiException("必填项不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        NewsEntity entity=new NewsEntity();
        entity.setTitle(news.getTitle());
        entity.setContent(news.getContent());
        entity.setDescribes(news.getDescribe());
        entity.setTime(new Date());
        Boolean bl=this.service.Add(entity);
        Result<Boolean> result=new Result<>(bl,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  修改
     * @param news
     * @param bindingResult
     * @return
     */
    @ApiModelProperty(value = "修改最新科研项目")
    @PostMapping("/edit")
    public ResponseEntity<Result<Boolean>> Edit(@Valid @RequestBody NewsForm news,BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(news.getContent().isEmpty() || news.getDescribe().isEmpty() || news.getTitle().isEmpty())
        {
            throw new ApiException("必填项不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if(news.getNewsId().longValue() <=0)
        {
            throw new ApiException("请选择需修改的项目", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        NewsEntity entity=new NewsEntity();
        entity.setTitle(news.getTitle());
        entity.setContent(news.getContent());
        entity.setDescribes(news.getDescribe());
        entity.setTime(new Date());
        entity.setNewsId(news.getNewsId());
        Boolean isb=this.service.Edit(entity);
        Result<Boolean> result=new Result<>(isb,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  查询
     * @param title
     * @param page
     * @param pageSize
     * @return
     */
    @ApiModelProperty(value = "查询最新科研项目")
    @GetMapping("/get")
    public  ResponseEntity<Result<PageInfo<NewsVo>>> Get(String title, Integer page, Integer pageSize)
    {
        if(page==null) {
            page=1;
            pageSize=20;
        }
        PageNumber pageNumber=new PageNumber(page,pageSize);
        PageInfo<NewsVo> newsEntityPageInfo= this.service.Get(title,pageNumber);
        Result< PageInfo<NewsVo>> result=new Result<>(newsEntityPageInfo,errors);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  精确插许小年
     * @param id
     * @return
     */
    @ApiModelProperty(value = "精确查询最新科研项目")
    @GetMapping("/getFind/{id}")
    public  ResponseEntity<Result<NewsEntity>> GetFind(@ApiParam(value = "id", required = true)
                                                       @PathVariable Long id){
        Result<NewsEntity> result;
        if(id ==null) {
            result=new Result<>(null,errors);
            return  new ResponseEntity<>(result,HttpStatus.OK);
        }
        NewsEntity entity=this.service.GetFind(id);
        result=new Result<>(entity,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *  根据唯一标识删除，未审核
     * @param newId
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping(value = "/del")
    public  ResponseEntity<Result<LoginResult>> Del(@Valid @RequestBody Long newId, BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors() || newId==null || newId<=0) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=this.service.Del(newId);
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }
}
