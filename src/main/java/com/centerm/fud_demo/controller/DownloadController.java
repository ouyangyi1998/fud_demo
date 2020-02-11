package com.centerm.fud_demo.controller;

import com.centerm.fud_demo.entity.DownloadRecord;
import com.centerm.fud_demo.entity.User;
import com.centerm.fud_demo.service.DownloadService;
import com.centerm.fud_demo.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/1/31 下午12:41
 */

@Controller
@ResponseBody
@Slf4j
@RequestMapping("download")
public class DownloadController {
    private   User currUser = null;
    @Autowired
    DownloadService downloadService;
    @Autowired
    FileService fileService;

    /**
     * @param id 文件id
     * @param response
     * @return
     */
    @ApiOperation("下载文件")
    @GetMapping("toDownload")
    public String toDownload(Long id, HttpServletResponse response, HttpServletRequest request){
        currUser = (User) request.getSession().getAttribute("user");
        log.info("用户: " + currUser.getUsername() + "  下载了文件(id)： " + id);
        downloadService.downloadFile(id, response);
        DownloadRecord downloadRecord = new DownloadRecord(currUser.getId(), id);
        downloadService.addDownloadRecord(downloadRecord);
        fileService.updateFile(id);
        return "user/download";
    }
}
