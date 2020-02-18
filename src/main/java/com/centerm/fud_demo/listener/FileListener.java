package com.centerm.fud_demo.listener;

import com.centerm.fud_demo.constant.Constants;
import com.centerm.fud_demo.dao.FileDao;
import com.centerm.fud_demo.entity.FileRecord;
import com.centerm.fud_demo.service.BackupService;
import com.centerm.fud_demo.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/2/15 上午11:01
 */
@Component
@Slf4j
public class FileListener extends FileAlterationListenerAdaptor {

    @Value("${filePath}")
    private String filePath;

    @Value("${backupPath}")
    private String backupPath;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private BackupService backupService;

    @Override
    public void onStart(FileAlterationObserver observer) {
        //log.info("Observer start...");
    }



    @Override
    public void onFileCreate(File file) {
        String fileName = file.getName();
        if (file.renameTo(file)){
            String finalName = fileName.substring(0, fileName.lastIndexOf("."));
            String fileSize = FileUtil.getFormatSize(file.length());
            String fileMd5 = FileUtil.getFileMd5(file.getPath());
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            if (fileDao.getFileIdByUrl(file.getPath()) != null){
                log.info("File exists in database...");
                backupService.backupFile(file.getPath(), backupPath + fileName + fileMd5, fileName);
                return;
            }
            FileRecord fileRecord = new FileRecord(finalName, file.getPath(), fileSize, Constants.SUPERVIPID, fileMd5, suffix, backupPath + fileName + fileMd5);
            fileDao.addFileRecord(fileRecord);
            backupService.backupFile(file.getPath(), backupPath + fileName + fileMd5, fileName);
            log.info("file added into database...");
        }
        log.info("(onFileCreate)File created: " + fileName);
    }

    @Override
    public void onFileDelete(File file) {
        String filePath = file.getPath();
        String backupUrl = backupService.getFileBackupPath(filePath);
        FileUtil.deleteDirectory(backupUrl);
        fileDao.deleteFileByPath(filePath);
        log.info("File deleted: " + filePath);
    }

    @Override
    public void onFileChange(File file) {
        String fileName = file.getName();
        if (file.renameTo(file)){
            String fileMd5 = FileUtil.getFileMd5(filePath + "real" + File.separator + fileName);
            String fileSize = FileUtil.getFormatSize(file.length());
            Long fileId = fileDao.getFileIdByUrl(file.getPath());
            String backupUrl = backupService.getFileBackupPath(file.getPath());
            fileDao.updateFileRecord(fileId, fileSize, fileMd5);
            FileUtil.deleteDirectory(backupUrl);
            backupService.backupFile(file.getPath(), backupPath + fileName + fileMd5, fileName);
            log.info("(onFileChange)file update successfully...");
        }
        log.info("File changed: " + file.getName());
    }



    @Override
    public void onStop(FileAlterationObserver observer) {
        //log.info("Observer finished...");
    }

}
