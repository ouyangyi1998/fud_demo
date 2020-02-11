package com.centerm.fud_demo.service.Impl;

import com.centerm.fud_demo.dao.FileDao;
import com.centerm.fud_demo.entity.BackupRecord;
import com.centerm.fud_demo.service.BackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 备份文件操作实现类
 * @author sheva
 */
@Service
@Slf4j
public class BackupServiceImpl implements BackupService {
    @Autowired
    private FileDao fileDao;
    @Override
    public List<BackupRecord> getAllBackup() {
        return fileDao.getAllBackup();
    }

    @Override
    public Boolean deleteBackupRecord(Long fileId) {
        BackupRecord backupRecord = fileDao.getBackupById(fileId);
        deleteLocalFile(backupRecord.getLocalUrl());
        return fileDao.deleteBackupRecord(fileId);
    }

    private void deleteLocalFile(String localUrl) {
        try{
            log.info("Start deleting local file: " + localUrl);
            File deleteFile = new File(localUrl);
            deleteFile.delete();
        }catch (Exception e){
            log.error("Delete Error...");
            e.printStackTrace();
        }
    }
}
