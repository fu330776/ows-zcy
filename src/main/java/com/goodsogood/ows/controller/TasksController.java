package com.goodsogood.ows.controller;

import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.UploadUtils;
import com.goodsogood.ows.model.db.TasksEntity;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.TasksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v-task")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "任务书", tags = {"Tasks manager"})
public class TasksController {

    private final Errors errors;
    private final TasksService service;

    @Autowired
    public TasksController(Errors errors, TasksService tasksService) {
        this.errors = errors;
        this.service = tasksService;
    }


    /**
     * 添加任务书
     *
     * @param vo
     * @param file
     * @param request
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加任务")
    @PostMapping("/addTasks")
    public ResponseEntity<Result<Boolean>> Add(@Valid @RequestBody TaskListForm vo, MultipartFile file, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        String url;
        TasksEntity entity = new TasksEntity();
        try {
            url = UploadUtils.importData(file, request);
            if (url.isEmpty() || url == null) {
                throw new ApiException("文件上传失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
            }
            entity.setTaskFileUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        entity.setIs_pay(1);
        entity.setIs_fulfill(1);
        entity.setSchedule(1);
        entity.setTaskType(vo.taskType);
        entity.setTaskCompletionDays(vo.taskCompletionDays);
        entity.setTaskCompletedDays(vo.taskCompletedDays);
        entity.setUserId(vo.userId);
        entity.setTaskName(vo.taskName);
        entity.setTaskMoney(vo.taskMoney);
        entity.setTaskContent(vo.taskContent);
        entity.setAddtime(new Date());
        this.service.Insert(entity);
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     * 添加委托书
     *
     * @param vo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加委托")
    @PostMapping("/AddEntrust")
    public ResponseEntity<Result<Boolean>> AddE(@Valid @RequestBody TaskListForm vo, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        TasksEntity entity = new TasksEntity();
//        entity.setIs_pay(1);
//        entity.setSchedule(1);
        entity.setTaskType(vo.getTaskType());
        entity.setTaskCompletionDays(vo.taskCompletionDays);
        entity.setTaskCompletedDays(vo.taskCompletedDays);
        entity.setUserId(vo.userId);
//        entity.setAddtime(new Date());
        entity.setTaskContent(vo.taskContent);
        entity.setTaskMoney(vo.taskMoney);
        entity.setTaskName(vo.taskName);
        entity.setIs_fulfill(1);
        Boolean bool = this.service.Insert(entity);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 修改任务 或委托 完成度
     *
     * @param task
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/putTasks")
    public ResponseEntity<Result<Boolean>> Put(@Valid @RequestBody TaskBailForm task, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.service.putByTaskId(task.taskId, task.taskCompletionDays, task.taskCompletedDays);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /***
     *  修改委托内容
     * @param entity
     * @param file
     * @param request
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改委托内容")
    @PostMapping("/Put")
    public ResponseEntity<Result<Boolean>> PutUrl(@Valid @RequestBody TasksEntity entity, MultipartFile file, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (entity.getTaskFileUrl().isEmpty()) {
            try {
                String url = UploadUtils.importData(file, request);
                if (url.isEmpty() || url == null) {
                    throw new ApiException("文件上传失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
                }
                entity.setTaskFileUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.service.putByAdmin(entity);
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 任务（委托）完成
     *
     * @param taskId        任务（委托）唯一标识
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "任务（委托）完成")
    @PostMapping("/PutFinish")
    public ResponseEntity<Result<Boolean>> PutFinish(@Valid @RequestBody Long taskId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (taskId <= 0L) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean b = this.service.putIsFullfill(taskId);
        Result<Boolean> result = new Result<>(b, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 领取任务奖励
     *
     * @param taskId
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "领取任务奖励")
    @PostMapping("/PutReceive")
    public ResponseEntity<Result<Boolean>> PutReceive(@Valid @RequestBody Long taskId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (taskId <= 0L) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }

        Boolean bool = this.service.PutReceive(taskId);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 任务（委托） 管理员确认 支付（付款）
     *
     * @param taskId
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "任务（委托） 管理员确认 支付（付款）")
    @PostMapping("/PutIsPay")
    public ResponseEntity<Result<Boolean>> PutIsPay(@Valid @RequestBody Long taskId, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (taskId <= 0L) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }

        Boolean bool = this.service.putIsPay(taskId);
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 根据用户查询
     *
     * @param userId        登录用户唯一标识
     * @param isPay
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "根据用户查询")
    @GetMapping("/get")
    public ResponseEntity<Result<List<TasksEntity>>> Get(@ApiParam(value = "userId", required = true)
                                                         @PathVariable Long userId,
                                                         @ApiParam(value = "isPay", required = true) Integer isPay,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        List<TasksEntity> entities = this.service.getByUser(userId, isPay);
        Result<List<TasksEntity>> result = new Result<>(entities, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 管理员根据类型查询
     *
     * @param type          1：任务书，2：委托书
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员根据类型查询")
    @GetMapping("/getType")
    public ResponseEntity<Result<List<TaskListForm>>> getType(@ApiParam(value = "type", required = true)
                                                            @PathVariable Integer type, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        List<TaskListForm> entities = this.service.getByType(type);
        Result<List<TaskListForm>> result = new Result<>(entities, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


//    /**
//     * 新增任务书
//     */
//    @ApiModelProperty(value = "添加任务")
//    @PostMapping("/addTasks")
//    public ResponseEntity<Result<Boolean>> AddTasks(@Valid @RequestBody TaskForm task, MultipartFile file, HttpServletRequest request, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        String url;
//        try {
//            url = UploadUtils.importData(file, request);
//            if (url.isEmpty() || url == null) {
//                throw new ApiException("文件上传失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//            }
//            task.entity.setTaskFileUrl(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Boolean bool = this.service.Insert(task.entity, task.entities);
//        if (!bool) {
//            throw new ApiException("任务添加失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Result<Boolean> result = new Result<>(true, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 任务修改
//     */
//    @ApiModelProperty(value = "修改任务")
//    @PostMapping("/alterTask")
//    public ResponseEntity<Result<Boolean>> AlterTasks(@Valid @RequestBody TaskForm task, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Integer num = this.service.TastAlter(task.entity, task.entities);
//        if (num <= 0) {
//            throw new ApiException("修改失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Result<Boolean> result = new Result<>(true, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 修改任务完成度
//     */
//    @ApiModelProperty(value = "修改任务完成度")
//    @PostMapping("/alterProgressBar")
//    public ResponseEntity<Result<Boolean>> AlterTasksProgressBar(@Valid @RequestBody ProgressBarForm bar, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        if (bar.schedule < 0 || bar.TaskId.isEmpty()) {
//            throw new ApiException("传入参数错误,请查看", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Boolean bool = this.service.UpdateTask(bar.TaskId, bar.schedule);
//        if (!bool) {
//            throw new ApiException("进度更新失败，服务器繁忙", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Result<Boolean> result = new Result<>(true, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 任务完成
//     */
//    @ApiModelProperty(value = "任务完成")
//    @PostMapping("/alterfulfil")
//    public ResponseEntity<Result<Boolean>> AlterTasksFulfil(@Valid @RequestBody Long TaskId, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        if (TaskId <= 0L) {
//            throw new ApiException("传入参数有误,请检查", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Integer num = this.service.MissionOk(TaskId);
//        if (num <= 0) {
//            throw new ApiException("任务修改失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Result<Boolean> result = new Result<>(true, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 查询任务
//     */
//    @ApiModelProperty(value = "管理员查询")
//    @GetMapping("/getByAdmin")
//    public ResponseEntity<Result<List<TasksEntity>>> GetALL(
//            @ApiParam(value = "ispay", required = true)
//            @PathVariable
//                    Integer ispay
//            , BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        if (ispay != 1 || ispay != 2) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        List<TasksEntity> tasksEntities;
//        tasksEntities = this.service.GetByAll(ispay);
//        Result<List<TasksEntity>> result = new Result<>(tasksEntities, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 查询任务
//     */
//    @ApiModelProperty(value = "用户查询")
//    @GetMapping("/getByUserId")
//    public ResponseEntity<Result<List<TasksEntity>>> GetByUserId(@ApiParam(value = "UserID", required = true)
//                                                                 @PathVariable Long UserID,
//                                                                 @ApiParam(value = "ispay", required = true)
//                                                                 @PathVariable Integer ispay,
//                                                                 BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        if (UserID <= 0L || ispay != 1 || ispay != 2) {
//            throw new ApiException("传入参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        List<TasksEntity> tasksEntities;
//        tasksEntities = this.service.GetByUserID(ispay, UserID);
//        Result<List<TasksEntity>> result = new Result<>(tasksEntities, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 查询任务周期划分
//     */
//    @ApiModelProperty(value = "用户查询")
//    @GetMapping("/getTaskDetails")
//    public ResponseEntity<Result<List<TasksSchedulesEntity>>> GetTaskDetails(@ApiParam(value = "TasksID", required = true)
//                                                                             @PathVariable Long TasksID,
//                                                                             BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        if (TasksID <= 0L) {
//            throw new ApiException("传入参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        List<TasksSchedulesEntity> entity;
//        entity = this.service.GetSchedule(TasksID);
//        Result<List<TasksSchedulesEntity>> result = new Result<>(entity, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 查询科研经济
//     */
//    @ApiModelProperty(value = "查询科研经济")
//    @GetMapping("/getFunds")
//    public ResponseEntity<Result<List<TaskOrderEntity>>> getFunds(@ApiParam(value = "userId", required = true)
//                                                                  @PathVariable Long userId, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        List<TaskOrderEntity> entities;
//        entities = this.service.GetFunds(userId);
//        Result<List<TaskOrderEntity>> result = new Result<>(entities, errors);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    /**
//     * 任务书收款
//     * */
//    @ApiModelProperty(value = "任务书收款")
//    @PostMapping("/pay")
//    public  ResponseEntity<Result<Boolean>> IsPay(@Valid @RequestBody Long taskid,BindingResult bindingResult)
//    {
//        if (bindingResult.hasFieldErrors()) {
//            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Boolean bool=this.service.TaskMoneyReceive(taskid);
//        if(!bool)
//        {
//            throw new ApiException("服务器繁忙，修改失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
//        }
//        Result<Boolean> result=new Result<>(true,errors);
//        return  new ResponseEntity<>(result,HttpStatus.OK);
//    }

}
