package com.centerm.fud_demo.controller;
import com.centerm.fud_demo.entity.FileRecord;
import com.centerm.fud_demo.entity.User;
import com.centerm.fud_demo.entity.ajax.AjaxReturnMsg;
import com.centerm.fud_demo.exception.AccountBanException;
import com.centerm.fud_demo.listener.Listener;
import com.centerm.fud_demo.service.*;
import com.centerm.fud_demo.shiro.UserRealm;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 管理员控制类
 * @author jerry
 */
@Controller
@RequestMapping("admin")
@Slf4j
public class AdminController {

    static final Integer BAN = 1;
    @Autowired
    AdminService adminService;
    @Autowired
    FileService fileService;
    @Autowired
    UserService userService;
    @Autowired
    DownloadService downloadService;
    @Autowired
    UploadService uploadService;


    @GetMapping("file")
    @RequiresRoles(value = {"ADMIN","SUPERVIP"},logical = Logical.OR)
    public String adminDownload(HttpServletRequest request)
    {
        List<FileRecord> fileList=fileService.getAllFile();
        request.setAttribute("fileList",fileList);
        return "admin/filelist";
    }

    @GetMapping("index")
    @RequiresRoles(value = {"ADMIN","SUPERVIP"},logical = Logical.OR)
    public String adminIndex(ServletRequest request)
    {
        AtomicInteger userNum=Listener.sessionCount;
        long fileNums = uploadService.getUploadTimes();
        Long downloadTimes = downloadService.getDownloadTimes();
        List<FileRecord> fileRecordList = downloadService.getMostDownloadRecord();
        request.setAttribute("userNum",userNum);
        request.setAttribute("fileNums", fileNums);
        request.setAttribute("downloadTimes", downloadTimes);
        request.setAttribute("fileList", fileRecordList);
        return "admin/index";
    }


    @GetMapping("ban")
    @RequiresRoles(value = {"ADMIN","SUPERVIP"},logical = Logical.OR)
    public String adminBan(HttpServletRequest request) {
        User user=(User)request.getSession().getAttribute("user");
        Long userId=user.getId();
        List<User> userList = adminService.getUserExceptAdminAndSuperVIP(userId);
        for (User currUSer : userList) {
            if (currUSer.getState().equals(BAN)) {
                currUSer.setStateName("封禁");
            }else{
                currUSer.setStateName("正常");
            }
        }
        request.setAttribute("userList",userList);
        return "admin/ban";
    }

    @PostMapping("banUser")
    @RequiresRoles(value = {"ADMIN","SUPERVIP"},logical = Logical.OR)
    @ResponseBody
    public AjaxReturnMsg banUser(HttpServletRequest request) throws AccountBanException
    {
        AjaxReturnMsg msg=new AjaxReturnMsg();
        String username=request.getParameter("username");
        User target=userService.findByUsername(username);
        Integer userState = target.getState();
        Long userId=target.getId();
       if(userState.equals(0))
       {
           //执行账号封禁
           Boolean isSuccess= adminService.banUser(userId);
           if (isSuccess.equals(0))
           {
               throw new AccountBanException();
           }
           log.info("用户 "+username+"　被封禁");
       }else {
           //执行账号解锁
           Boolean isSuccess = adminService.releaseUser(userId);
           if (isSuccess.equals(0))
           {
               throw new AccountBanException();
           }
           log.info("用户 "+username+"　被解除封禁");
       }
        DefaultWebSecurityManager securityManager= (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm shiroRealm = (UserRealm) securityManager.getRealms().iterator().next();
        shiroRealm.clearAllCache();

        msg.setFlag(1);
        return msg;

    }

    /**
     * @param
     * @return
     */
    @ApiOperation("删除文件")
    @PostMapping("toDelete")
    @ResponseBody
    public AjaxReturnMsg toDelete(HttpServletRequest request) {
        AjaxReturnMsg msg=new AjaxReturnMsg();
        Long fileId=Long.parseLong(request.getParameter("fileId"));
        ModelAndView mv = new ModelAndView();
        Boolean isSuccess=fileService.deleteFile(fileId);
        if (isSuccess==false)
        {
            msg.setFlag(0);
            msg.setMsg("Delete Fail");
            return msg;
        }
        msg.setFlag(1);
        return msg;
    }
    @PostMapping("search")
    @ResponseBody
    @RequiresRoles(value = {"ADMIN","SUPERVIP"},logical = Logical.OR)
    public AjaxReturnMsg search(HttpServletRequest request)
    {
        AjaxReturnMsg msg=new AjaxReturnMsg();
        String contents=request.getParameter("contents");
       List<User> userList= adminService.getUserLikeContents(contents);
       if (userList==null||userList.isEmpty())
       {
           msg.setMsg("未搜索到数据");
           msg.setFlag(0);
           return msg;
       }
        request.getSession().setAttribute("contents",contents);
       msg.setFlag(1);
       return msg;
    }
    @GetMapping("search")
    @RequiresRoles(value = {"ADMIN","SUPERVIP"},logical = Logical.OR)
    public String Search(HttpServletRequest request)
    {
        List<User> userList= adminService.getUserLikeContents((String) request.getSession().getAttribute("contents"));
        for (User currUSer : userList) {
            if (currUSer.getState().equals(BAN)) {
                currUSer.setStateName("封禁");
            }else{
                currUSer.setStateName("正常");
            }
        }
        request.setAttribute("userList",userList);
        return "admin/search";
    }
}
