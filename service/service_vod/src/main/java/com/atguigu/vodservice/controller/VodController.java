package com.atguigu.vodservice.controller;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.vodservice.utils.AliyunVodSDKUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(description="视频管理")
@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
 public  class VodController {

    @ApiOperation(value = "上传视频")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        try {
            //1准备参数
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            //2创建请求对象
            UploadStreamRequest request = new UploadStreamRequest(
                    "LTAI4G1xrNTTjM7CJtUNa3GC",
                    "7hWoUbSJIlzDHNLOQ9NK9avTyoZQBd",
                    title,originalFilename,inputStream
            );
            //3创建对象发送请求
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();

            return R.ok().data("videoId",videoId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001,"上传视频失败");

        }

    }

    @ApiOperation(value = "删除单个视频")
    @DeleteMapping("delVideo/{videoId}")
    public R delVideo(@PathVariable("videoId") String videoId){
        try {
            //1创建初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI4G1xrNTTjM7CJtUNa3GC",
                    "7hWoUbSJIlzDHNLOQ9NK9avTyoZQBd");
            // 2创建请求对象、响应对象（根据操作不同，选择不同的类）
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3把参数存入请求对象
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            //4调用初始化对象方法，发送请求，获取响应
            client.getAcsResponse(request);
            return R.ok();

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除单个视频失败");
        }
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("delVideoList/{videoIdList}")
    public R delVideoList(@RequestParam("videoIdList")List<String> videoIdList){
        try {
            //1创建初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI4G1xrNTTjM7CJtUNa3GC",
                    "7hWoUbSJIlzDHNLOQ9NK9avTyoZQBd");
            // 2创建请求对象、响应对象（根据操作不同，选择不同的类）
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3把参数存入请求对象
            //支持传入多个视频ID，多个用逗号分隔
            String videoIds = StringUtils.join(videoIdList.toArray(),",");
            request.setVideoIds(videoIds);
            //4调用初始化对象方法，发送请求，获取响应
            client.getAcsResponse(request);
            return R.ok();

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除单个视频失败");
        }
    }

    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getPlayAuth/{vid}")
    public R getPlayAuth(@PathVariable String vid){
        try {
            //（1）创建初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI4G1xrNTTjM7CJtUNa3GC",
                    "7hWoUbSJIlzDHNLOQ9NK9avTyoZQBd");
            //（2）创建request、response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            //（3）向request设置视频id
            request.setVideoId(vid);
            //播放凭证有过期时间，默认值：100秒 。取值范围：100~3000。
            //request.setAuthInfoTimeout(200L);
            //（4）调用初始化方法实现功能
            response = client.getAcsResponse(request);
            //（5）调用方法返回response对象，获取内容
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            throw new GuliException(20001,"获取视频凭证失败");
        }
    }


}
