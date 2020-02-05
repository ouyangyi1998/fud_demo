package com.centerm.fud_demo.dao;

import com.centerm.fud_demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 管理员映射
 * @author jerry
 */
@Component
public interface AdminDao {
    /**
     * 获取所有用户
     * @return
     */
    List<User> getAllUser();

    /**
     * 获取用户状态
     * @param userId
     * @return
     */
    int getUserState(Long userId);

    /**
     * ban用户
     * @param userId 用户id
     * @return
     */
    Boolean banUser(Long userId);

    /**
     * 释放用户
     * @param userId 用户id
     * @return
     */
    Boolean releaseUser(Long userId);

    /**
     * 获取用户（除了管理员和超级管理员）
     * @param userId 用户id
     * @return
     */
    List<User> getUserExceptAdminAndSuperVIP(Long userId);



    List<User> getUserLikeContents(String contents);


    List<Map<String,Object>> getAllUploadToMorrisJs();



    List<Map<String,Object>> getAllDownloadToMorrisJs();

    Integer getAdminNumber();

    Integer getAllUserNumber();
}
