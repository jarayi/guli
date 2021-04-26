package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-08
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

    @Override
    public void delVideoById(String id) {
        // 根据Id查询小节信息
        EduVideo eduVideo = baseMapper.selectById(id);
        // 判断是否有视频Id
        String videoId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoId)) {
            vodClient.delVideo(videoId);
        }
        // 删除小节
        baseMapper.deleteById(id);
    }
}
