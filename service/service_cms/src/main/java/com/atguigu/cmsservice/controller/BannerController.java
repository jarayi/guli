package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.Banner;
import com.atguigu.cmsservice.service.BannerService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import feign.Body;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-04-12
 */
@Api(description = "banner管理")
@RestController
@RequestMapping("/cmsservice/banner")
@CrossOrigin
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "分页查询banner")
    @GetMapping("getBannerPage/{page}/{limit}")
    public R getBannerPage(@PathVariable long page, @PathVariable long limit) {
        Page<Banner> pageParam = new Page<>(page, limit);
        bannerService.page(pageParam, null);
        List<Banner> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("records", records).data("records", records);
    }

    @ApiOperation(value = "新增banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody Banner banner) {
        bannerService.save(banner);
        return R.ok();

    }

    @ApiOperation(value = "根据ID删除banner")
    @DeleteMapping("delBanner/{id}")
    public R delBanner(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();

    }

    @ApiOperation(value = "根据ID查询banner")
    @GetMapping("getBannerById/{id}")
    public R getBannerById(@PathVariable String id) {
        Banner banner = bannerService.getById(id);
        return R.ok().data("banner", banner);
    }

    @ApiOperation(value = "修改banner")
    @PostMapping("updateBanner")
    public R updateBanner(@RequestBody Banner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }


}

