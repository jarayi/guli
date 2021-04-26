package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-04-08
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService videoService;

    //根据课程ID查询章节小节
    @Override
    public List<ChapterVo> getChapterVideoInfo(String courseId) {
        //1 根据courseId查询章节信息
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(chapterWrapper);
        //2 根据courseId查询小节信息
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(videoWrapper);
        //3 遍历封装章节信息
        List<ChapterVo> chapterVideoList = new ArrayList<>();
        for (int i = 0; i <eduChapterList.size() ; i++) {
            //3.1获取每一个chapter
            EduChapter eduChapter = eduChapterList.get(i);
            //3.2 EduChapter转化ChapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //3.3 封装章节数据
            chapterVideoList.add(chapterVo);
            //4 遍历封装与章节有关的小节信息
            List<VideoVo> videoVos = new ArrayList<>();
            for (int m = 0; m <eduVideoList.size() ; m++) {
                //4.1获取每一个video
                EduVideo eduVideo = eduVideoList.get(m);
                //4.2判断与章节关系
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    //4.3   EduVideo转化成VideoVo
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    //4.4封装小节数据
                    videoVos.add(videoVo);

                }
            }
            chapterVo.setChildren(videoVos);
        }
        return chapterVideoList;
    }
}
