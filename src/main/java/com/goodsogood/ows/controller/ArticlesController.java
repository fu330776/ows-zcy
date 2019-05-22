package com.goodsogood.ows.controller;


import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.ArticlesEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.ArticlesForm;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.ArticlesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v-article")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders = {"_captcha"})
@Api(value = "文章", tags = "Articles")
public class ArticlesController {

    private final ArticlesService service;
    private final Errors errors;

    public ArticlesController(ArticlesService articlesService, Errors errors) {
        this.service = articlesService;
        this.errors = errors;
    }


    /**
     * 添加文章
     *
     * @param form          行业动态文章实体
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加（行业动态）文章")
    @PostMapping("/addTrends")
    public ResponseEntity<Result<Boolean>> addTrends(@Valid @RequestBody ArticlesForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        ArticlesEntity entity = new ArticlesEntity();
        entity.setAddtime(new Date());
        entity.setContent(form.getContent());
        entity.setTitle(form.getTitle());
        entity.setType(1);
        Boolean bool = this.service.AddArticle(entity);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 添加（新技术新产品）文章
     *
     * @param form          新技术新产品文章实体
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加（新技术新产品）文章")
    @PostMapping("/addProduct")
    public ResponseEntity<Result<Boolean>> addProduct(@Valid @RequestBody ArticlesForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        ArticlesEntity entity = new ArticlesEntity();
        entity.setTitle(form.getTitle());
        entity.setContent(form.getContent());
        entity.setType(2);
        entity.setAddtime(new Date());
        Boolean bool = this.service.AddArticle(entity);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * （行业动态）文章
     *
     * @return
     */
    @ApiOperation(value = "查询（行业动态）文章")
    @GetMapping("/getTrends")
    public ResponseEntity<Result<PageInfo<ArticlesEntity>>> getTrends(@ApiParam(value = "page", required = true)
                                                                      @PathVariable Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        PageInfo<ArticlesEntity> entities = this.service.GetByType(1, new PageNumber(page, pageSize));
        Result<PageInfo<ArticlesEntity>> result = new Result<>(entities, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 查询（新技术新产品）文章
     *
     * @return
     */
    @ApiOperation(value = "查询（新技术新产品）文章")
    @GetMapping("/getProduct")
    public ResponseEntity<Result<PageInfo<ArticlesEntity>>> getProduct(@ApiParam(value = "page", required = true)
                                                                       @PathVariable Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        PageInfo<ArticlesEntity> entities = this.service.GetByType(2,new PageNumber(page, pageSize));
        Result<PageInfo<ArticlesEntity>> result = new Result<>(entities, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
