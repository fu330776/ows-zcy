package com.goodsogood.ows.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v-excel")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "导出Execl", tags = {"Excel demo"})
public class ExcelController {

//    @RequestMapping("/test2")
//    public void test2(HttpServletResponse response){
//        int rowIndex = 0;
//        List<UserInfo> list = userInfoService.selectAlla(0, 0);
//        ExcelData data = new ExcelData();
//        data.setName("hello");
//        List<String> titles = new ArrayList();
//        titles.add("ID");
//        titles.add("userName");
//        titles.add("password");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for(int i = 0, length = list.size();i<length;i++){
//            UserInfo userInfo = list.get(i);
//            List<Object> row = new ArrayList();
//            row.add(userInfo.getId());
//            row.add(userInfo.getUserName());
//            row.add(userInfo.getPassword());
//            rows.add(row);
//        }
//        data.setRows(rows);
//        try{
//            ExcelUtils.exportExcel(response,"test2",data);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
