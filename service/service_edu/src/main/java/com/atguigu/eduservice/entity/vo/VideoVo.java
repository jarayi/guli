package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class VideoVo {
    private String id;
    private String title;
    //云端视频资源
    private String videoSourceId;
    //是否可以试听：0收费 1免费
    private Boolean isFree;

}
