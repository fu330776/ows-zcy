package com.goodsogood.ows.controller;

import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.PaymentEntity;
import com.goodsogood.ows.model.vo.CreateOrderForm;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.model.vo.UserMenusVo;
import com.goodsogood.ows.service.PayService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v-Pay")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "支付", tags = {"Pay manager"})
public class PayController {
        private  final PayService service; private final Errors errors;
        @Autowired
        public  PayController(PayService service,Errors errors)
        {
            this.service=service;
            this.errors=errors;
        }

    /**
     *  创建订单
     * @param form
     * @param bindingResult
     * @return
     */
        public ResponseEntity<Result<PaymentEntity>>  CreateOrder(@Valid @RequestBody CreateOrderForm form, BindingResult bindingResult ){
            if (bindingResult.hasFieldErrors()) {
                throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
            }
            PaymentEntity entity=new PaymentEntity();
            switch (form.getType())
            {
                case  1:
                    entity= this.service.InsertPatents(form.getGid());
                    break;
                case 2:
                    entity=this.service.InsertTasks(form.getGid());
                    break;
            }
            Result<PaymentEntity> result = new Result<>(entity, errors);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }



}
