package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-19
 */
@Api(description = "支付管理")
@RestController

@RequestMapping("/orderservice/paylog")
@CrossOrigin
public class TPayLogController {
@Autowired
    private TPayLogService payLogService;





    @ApiOperation(value = "生成二维码")
    @GetMapping("/createNative/{orderNo}")
    public R creatNative(@PathVariable String orderNo){
        Map map =payLogService.createNative(orderNo);
        return R.ok().data(map);
    }
    @ApiOperation(value = "查询订单状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
      Map<String,String> map=  payLogService.queryPayStatus(orderNo);
      if(map==null){
          return R.error().message("支付出错");
      }
      if("success".equals(map.get("trade_state"))){
          // 更新订单状态，插入支付日志
         payLogService.updataOrderStatus(map);
          return  R.ok().message("支付成功");
      }
      return R.ok().code(20005).message("支付中");
    }

}

