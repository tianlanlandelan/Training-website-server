package com.example.demo.controller;

import com.example.demo.common.response.MyResponse;
import com.example.demo.common.util.StringUtils;
import com.example.demo.service.AdminService;
import com.example.demo.service.AppConfigService;
import com.example.demo.service.RateService;
import com.example.demo.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AppConfigService appConfigService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RateService rateService;

    @Resource
    private AdminService adminService;

    /**
     * 设置邀请码
     * @param value
     * @return
     */
    @PutMapping("/setInviteCode")
    public ResponseEntity setInviteCode(String value){
        if(StringUtils.isEmpty(value)){
            return MyResponse.badRequest();
        }
        appConfigService.setInviteCode(value);
        return MyResponse.ok();
    }

    /**
     * 重置用户密码
     * @param userId
     * @param password
     * @return
     */
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(Integer userId,String password){
        if(RequestUtil.notValidInteger(userId) || StringUtils.isEmpty(password)){
            return MyResponse.badRequest();
        }
        return MyResponse.ok(userInfoService.resetPassword(userId,password));
    }

    /**
     * 获取忘记密码的用户
     * @return
     */
    @GetMapping("/getFotPasswordUser")
    public ResponseEntity getFotPasswordUser(){
        return MyResponse.ok(userInfoService.getFotPasswordUser());
    }

    /**
     * 获取总用户数
     * @return
     */
    @GetMapping("/getUserCount")
    public ResponseEntity getUserCount(){
        return MyResponse.ok(userInfoService.getUserCount());
    }

    /**
     * 获取排行榜前20名
     * @return
     */
    @GetMapping("/getTopLeaderBoard")
    public ResponseEntity getTopLeaderBoard(){
        return MyResponse.ok();
    }


    /**
     * 按类型获取排行榜
     * @param type
     * @return
     */
    @GetMapping("/getLeaderBoard")
    public ResponseEntity getLeaderBoard(Integer type){

        return MyResponse.ok(rateService.getLeaderBoardByType(type));
    }

    /**
     * 按类型获取用户所有学习记录
     * @param type
     * @return
     */
    @GetMapping("/getInfoByType")
    public ResponseEntity getInfoByType(Integer type){
        return MyResponse.ok(adminService.selectInfoByType(type));
    }


}
