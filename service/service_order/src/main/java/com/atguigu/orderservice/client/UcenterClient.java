package com.atguigu.orderservice.client;

        import com.atguigu.servicebase.vo.UcenterMemberForPay;
        import io.swagger.annotations.ApiOperation;
        import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.stereotype.Component;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = UcenterClientimpl.class)
public interface UcenterClient {
    @ApiOperation(value = "根据用户Id获取用户信息远程调用")
    @GetMapping("/uceservice/member/getUcenterForPay/{memberId}")
    public UcenterMemberForPay getUcenterForPay(
            @PathVariable("memberId")String memberId);

}
