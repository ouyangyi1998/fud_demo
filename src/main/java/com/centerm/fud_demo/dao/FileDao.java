package com.centerm.fud_demo.dao;

import com.centerm.fud_demo.entity.BackupRecord;
import com.centerm.fud_demo.entity.DownloadRecord;
import com.centerm.fud_demo.entity.FileRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * File相关操作映射
 * @author sheva
 */
@Component
public interface FileDao {

   /**
    * 根据用户id获取上传文件
    * @param userId
    * @return
    */
   List<FileRecord> getFileByUserId(Long userId);
   /**
    * 添加文件
    * @param file 文件实体
    * @return 文件
    */
   Boolean addFile(FileRecord file);
   /**
    * 根据id获取文件
    * @param id  文件id
    * @return
    */
   FileRecord getFileById(Long id);
   /**
    * 获取所有文件
    * @return
    */
   List<FileRecord> getAllFile();
   /**
    * 添加下载记录
    * @param downloadRecord
    * @return
    */
   Boolean addDownloadRecord(DownloadRecord downloadRecord);

   /**
    * 获取用户最热门下载
    * @param userId 用户id
    * @return fileRecord集合
    */
   List<FileRecord> getMostDownloadRecordById(Long userId);
   /**
    * 获取最热门下载
    * @return fileRecord集合
    */
    List<FileRecord> getMostDownloadRecord();
   /**
    * 获取某个用户上传的文件的总下载次数
    * @param userId 用户id
    * @return 总下载次数
    */
   Long getDownloadTimesByUserId(Long userId);

   /**
    * 获取下载次数
    * @return 下载次数
    */
   Long getDownloadTimes();

   /**
    * 获取上传次数
    * @return
    */
   Long getUploadTimes();

   /**
    * 根据用户id与文件id删除文件
    * @param userId
    * @param fileId
    * @return
    */
   Boolean deleteFileById(Long userId, Long fileId);

   /**
    * 根据文件id删除文件（管理员操作）
    * @param fileId
    * @return
    */
   Boolean deleteFile(Long fileId);


   /**
    * 更新文件信息（下载次数）
    * @param fileId 文件id
    * @return
    */
   Boolean updateFile(Long fileId);

   /**
    * 删除下载信息
    * @param fileId 文件id
    * @return
    */
   Boolean deleteDownloadRecord(Long fileId);

   /**
    * 获取最新上传的前五个文件
    * @return 文件集合
    */
   List<FileRecord> getLatestUploaded(Long userId);

   /**
    * 获取最新下载的前五个文件
    * @return 文件集合
    */
   List<FileRecord> getLatestDownloaded(Long userId);

   /**
    * 搜索文件
    * @param contents 关键词
    * @param userId 用户id
    * @return
    */
   List<FileRecord> getFileLikeContents(String contents,Long userId);

   /**
    * 给折线图传送用户上传信息
    * @param userId 用户id
    * @return
    */

   List<Map<String,Object>> getUploadToMorrisJs(Long userId);

   /**
    * 给折线图传送用户下载信息
    * @param userId 用户id
    * @return
    */

    List<Map<String,Object>> getDownloadToMorrisJs(Long userId);

   /**
    * 根据用户id搜索上传次数
    * @param userId 用户id
    * @return
    */

    Long getUploadTimesByCurrUser(Long userId);

   /**
    * 获取服务器所有备份文件
    * @return
    */
   List<BackupRecord> getAllBackup();

   /**
    * 添加备份文件记录
    * @param backupRecord
    * @return
    */
   Boolean addBackupRecord(BackupRecord backupRecord);

   /**
    * 获取某一条备份文件
    * @param fileId
    * @return
    */
   BackupRecord getBackupById(Long fileId);

   /**
    * 删除备份记录
    * @param fileId
    * @return
    */
   Boolean deleteBackupRecord(Long fileId);

   /**
    * 通过文件id获取文件名
    * @param fileName
    * @return
    */
   Long getFileIdByFileName(String fileName);

   /**
    * 根据md5查找备份记录
    * @param md5
    * @return
    */
   BackupRecord getBackupIdByMd5(String md5);
}
