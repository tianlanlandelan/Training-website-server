package com.example.demo.controller;

import com.example.demo.common.response.MyResponse;
import com.example.demo.common.util.StringUtils;
import com.example.demo.entity.TopicInfo;
import com.example.demo.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yangkaile
 * @date 2019-10-17 14:49:21
 */
@RestController
@RequestMapping("/topicInfo")
public class TopicController {
    @Resource
    private TopicService topicService;

    @PostMapping
    public ResponseEntity saveTopic(Integer sort,String title,
                                    String content,String videoTitle,String videoUrl){
        if(StringUtils.isEmpty(title)){
            return MyResponse.badRequest();
        }
        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setSort(sort);
        topicInfo.setTitle(title);
        topicInfo.setContent(content);
        topicInfo.setVideoTitle(videoTitle);
        topicInfo.setVideoUrl(videoUrl);

        return MyResponse.ok(topicService.save(topicInfo));
    }

    @GetMapping
    public ResponseEntity getAll(){
        return MyResponse.ok(topicService.getAll());
    }

}