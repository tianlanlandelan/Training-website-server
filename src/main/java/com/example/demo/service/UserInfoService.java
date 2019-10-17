package com.example.demo.service;

import com.example.demo.common.response.ResultData;
import com.example.demo.common.util.Console;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.UserInfoMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangkaile
 * @date 2019-10-09 11:10:21
 */
@Service
public class UserInfoService {
    @Resource
    UserInfoMapper userInfoMapper;

    /**
     * 添加用户，如果同一个网站下有已注册的邮箱、学号，注册失败
     * @param userInfo
     * @return
     */
    public ResultData insert(UserInfo userInfo){
        if(checkExist(userInfo)){
            return ResultData.error("uid/uemail重复注册");
        }
        userInfoMapper.baseInsertAndReturnKey(userInfo);

        Console.print("insert",userInfo.getId(),userInfo);
        return ResultData.success(userInfo.getId());
    }


    /**
     * 登录
     * 判断指定邮箱和类型的用户是否存在，
     *  如果存在，校验密码；
     *      如果密码正确，返回用户数据，返回的数据屏蔽密码
     *      否则，返回密码不正确
     *  否则，返回用户不存在
     * @param userInfo
     * @return
     */
    public ResultData login(UserInfo userInfo){
        UserInfo user = getUserByEmailAndType(userInfo);
        if(user == null){
            return ResultData.error("用户不存在");
        }
        if(userInfo.getPassword().equals(user.getPassword())){
            user.setPassword("");
            return ResultData.success(user);
        }else {
            return ResultData.error("密码不正确");
        }
    }

    private UserInfo getUserByEmailAndType(UserInfo userInfo){
        UserInfo user = new UserInfo();
        user.setEmail(userInfo.getEmail());
        user.setType(userInfo.getType());
        user.setBaseKyleUseAnd(true);

        List<UserInfo> list = userInfoMapper.baseSelectByCondition(user);
        //仅查到1个用户才算正确
        if(list == null || list.size() != 1){
            return null;
        }
        return list.get(0);
    }

    /**
     * 忘记密码
     * 设置用户状态为忘记密码，等待管理员操作
     * @param userInfo
     * @return
     */
    public ResultData forgetPassword(UserInfo userInfo){
        UserInfo user = getUserByEmailAndType(userInfo);
        if(user == null){
            return ResultData.error("用户不存在");
        }
        user.setStatus(UserInfo.FORGET_PASSWORD);
        return ResultData.success("已通知系统管理员，请耐心等待");
    }

    /**
     * 修改用户信息，只修改名称、头像
     * @param id
     * @param firstName
     * @param lastName
     * @param avatarId
     * @return
     */
    public ResultData update(int id,String firstName,String lastName,int avatarId){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo = userInfoMapper.baseSelectById(userInfo);
        if(userInfo == null){
            return ResultData.error("用户不存在");
        }
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setAvatarId(avatarId);
        userInfoMapper.baseUpdateById(userInfo);
        return ResultData.success();
    }

    /**
     * 获取用户信息，需同时指定三个条件：type/email/uid
     * @param info
     * @return
     */
    private boolean checkExist(UserInfo info){
        UserInfo userInfo = new UserInfo();
        userInfo.setSid(info.getSid());
        userInfo.setEmail(info.getEmail());
        userInfo.setBaseKyleUseAnd(false);
        List<UserInfo> list = userInfoMapper.baseSelectByCondition(userInfo);
        Console.print("getUserInfo",list);
        for(UserInfo user : list){
            if(user.getType().equals(info.getType())){
                return true;
            }
        }
        return false;
    }


    public ResultData register(UserInfo userInfo,String code){

        try{
            userInfoMapper.baseInsertAndReturnKey(userInfo);
        }catch (DuplicateKeyException e){
            Console.print("error",e.getMessage());
            return ResultData.error("uid/uemail重复注册");
        }

        Console.print("userInfo",userInfo.getId(),userInfo);
        return ResultData.success(userInfo.getId());
    }
}