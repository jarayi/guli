package com.atguigu.cmsservice.service.impl;

import com.atguigu.cmsservice.entity.Banner;
import com.atguigu.cmsservice.mapper.BannerMapper;
import com.atguigu.cmsservice.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-12
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
// 展示首页banner
    @Override
    // redis缓存数据格式key=banner::selectIndexList va
    @Cacheable(value = "banner", key = "'selectIndexList'")
    public List<Banner> getAllBanner() {
        List<Banner> bannerList = baseMapper.selectList(null);
        return bannerList;
    }
}
