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
 * @ProjectName: fud_demo
 * @Package: com.centerm.fud_demo.service.Impl
 * @ClassName: BackupServiceImpl
 * @Author: jerry
 * @Description: ${description}
 * @Date: 20-2-10 下午4:41
 * @Version: 1.0
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
    public Boolean addBackupRecord(BackupRecord backupRecord) {
        return fileDao.addBackupRecord(backupRecord);
    }

    @Override
    public Boolean deleteBackup(Long fileId) {
        BackupRecord backupRecord = fileDao.getBackupById(fileId);
        deleteLocalFile(backupRecord.getLocalUrl());
        return fileDao.deleteBackup(fileId);
    }

    private void deleteLocalFile(String localUrl) {
        try{
            System.out.println("开始删除本地文件: " + localUrl);
            File deleteFile = new File(localUrl);
            deleteFile.delete();
        }catch (Exception e){
            System.out.println("删除本地文件出错");
            e.printStackTrace();
        }
    }
}
