package com.centerm.fud_demo.controller;
import com.centerm.fud_demo.entity.User;
import com.centerm.fud_demo.entity.ajax.AjaxReturnMsg;
import com.centerm.fud_demo.service.FileService;
import com.centerm.fud_demo.service.UploadService;
import com.centerm.fud_demo.utils.GetDateUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分片上传Controller
 * @author sheva
 */
@Controller
@RequestMapping("upload")
public class UploadController {

    User currUser = null;

    @Autowired
    UploadService uploadService;
    @Autowired
    FileService fileService;

    /**
     * 跳转到上传界面
     * @return
     */
    @GetMapping("index")
    public String toUpload() {
        return "uploadbackup";
    }

    /**
     * 查看当前分片是否上传
     *
     * @param request
     * @param response
     */
    @PostMapping("checkblock")
    @ResponseBody
    public void checkMd5(HttpServletRequest request, HttpServletResponse response) {
        uploadService.checkMd5(request, response);
    }

    /**
     * 上传分片
     * @param file 文件
     * @param chunk　块
     * @param guid　md5标识
     * @throws IOException
     */
    @PostMapping("save")
    @ResponseBody
    public void upload(@RequestParam MultipartFile file, Integer chunk, String guid, HttpServletRequest request) throws Exception {
        currUser = (User) request.getSession().getAttribute("user");
        uploadService.upload(file, chunk, guid, currUser.getId());
    }
    /**
     * 合并文件
     * @param guid　md5
     * @param fileName 文件名
     */
    @PostMapping("combine")
    @ResponseBody
    public void combineBlock(String guid, String fileName) {
        uploadService.combineBlock(guid, fileName);
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
        currUser = (User) request.getSession().getAttribute("user");
        System.out.println("当前用户id为：" +currUser.getId());
        Boolean isSuccess= fileService.deleteFileById(currUser.getId(), fileId);
        if (isSuccess==false)
        {
            msg.setFlag(0);
            msg.setMsg("Delete Fail");
            return msg;
        }
        msg.setFlag(1);
        return msg;
    }
    @PostMapping("getChart")
    @ResponseBody
    public List<Map<String,Object>> getChart(HttpServletRequest request)
    {
        Long userId=((User)request.getSession().getAttribute("user")).getId();
        List<Map<String,Object>> uploadList= fileService.getUploadToMorrisJs(userId);
        List<Map<String,Object>> downloadList= fileService.getDownloadToMorrisJs(userId);


        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();map1.put("days",GetDateUtil.getDate(7));map1.put("upload",0);map1.put("download",0);
        Map<String,Object> map2=new HashMap<>();map2.put("days",GetDateUtil.getDate(6));map2.put("upload",0);map2.put("download",0);
        Map<String,Object> map3=new HashMap<>();map3.put("days",GetDateUtil.getDate(5));map3.put("upload",0);map3.put("download",0);
        Map<String,Object> map4=new HashMap<>();map4.put("days",GetDateUtil.getDate(4));map4.put("upload",0);map4.put("download",0);
        Map<String,Object> map5=new HashMap<>();map5.put("days",GetDateUtil.getDate(3));map5.put("upload",0);map5.put("download",0);
        Map<String,Object> map6=new HashMap<>();map6.put("days",GetDateUtil.getDate(2));map6.put("upload",0);map6.put("download",0);
        Map<String,Object> map7=new HashMap<>();map7.put("days",GetDateUtil.getDate(1));map7.put("upload",0);map7.put("download",0);

        for (Map<String, Object> m : uploadList)
        {
               if(m.get("days").equals(map1.get("days"))){
                 map1.put("upload",m.get("upload"));
               }
               if(m.get("days").equals(map2.get("days"))){
                   map2.put("upload",m.get("upload"));
               }
               if(m.get("days").equals(map3.get("days"))){
                   map3.put("upload",m.get("upload"));
               }
               if(m.get("days").equals(map4.get("days"))){
                   map4.put("upload",m.get("upload"));
               }
               if(m.get("days").equals(map5.get("days"))){
                   map5.put("upload",m.get("upload"));
               }
               if(m.get("days").equals(map6.get("days"))){
                   map6.put("upload",m.get("upload"));
               }
               if(m.get("days").equals(map7.get("days"))){
                   map7.put("upload",m.get("upload"));
               }
        }
        for (Map<String, Object> m : downloadList)
        {
            if(m.get("days").equals(map1.get("days"))){
                map1.put("download",m.get("download"));
            }
            if(m.get("days").equals(map2.get("days"))){
                map2.put("download",m.get("download"));
            }
            if(m.get("days").equals(map3.get("days"))){
                map3.put("download",m.get("download"));
            }
            if(m.get("days").equals(map4.get("days"))){
                map4.put("download",m.get("download"));
            }
            if(m.get("days").equals(map5.get("days"))){
                map5.put("download",m.get("download"));
            }
            if(m.get("days").equals(map6.get("days"))){
                map6.put("download",m.get("download"));
            }
            if(m.get("days").equals(map7.get("days"))){
                map7.put("download",m.get("download"));
            }
        }
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        list.add(map7);
        return list;
    }
}