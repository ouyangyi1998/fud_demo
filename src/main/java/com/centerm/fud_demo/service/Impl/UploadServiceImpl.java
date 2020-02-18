package com.centerm.fud_demo.service.Impl;
import com.centerm.fud_demo.dao.FileDao;
import com.centerm.fud_demo.entity.FileRecord;
import com.centerm.fud_demo.service.UploadService;
import com.centerm.fud_demo.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sheva
 * @version 1.0
 * @date 2020/1/30 下午2:20
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${filePath}")
    private String uploadPath;
    @Value("${backupPath}")
    private String backupPath;
    private Long userId = null;

    @Autowired
    FileDao fileDao;

    @Override
    public Long getUploadTimes() {
        return fileDao.getUploadTimes();
    }

    @Override
    public List<FileRecord> getLatestUploaded(Long userId) {
        return fileDao.getLatestUploaded(userId);
    }

    @Override
    public Long getUploadTimesByCurrUser(Long userId) {
        return fileDao.getUploadTimesByCurrUser(userId);
    }

    @Override
    public void upload(MultipartFile file, Integer chunk, String guid, Long uploaderId) throws Exception {
        String filePath = uploadPath + "temp" + File.separator + guid;
        File tempFile = new File(filePath);
        userId = uploaderId;
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream = null;
        if (chunk == null) {
            chunk = 0;
        }
        try {
            log.info("Uploading....");
            File dirFile = new File(filePath, String.valueOf(chunk));
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(file.getInputStream());
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        } catch (Exception e) {
            log.error("(upload)Exception: " + e.getMessage());
        } finally {
            if (null != inputStream) {
                try{
                    inputStream.close();
                }catch (Exception e){
                    log.error("(upload)inputStream: " + e.getMessage());
                }
            }
            if (null != raFile) {
                try{
                    raFile.close();
                }catch (Exception e){
                    log.error("(upload)raFile: " + e.getMessage());
                }
            }
        }
    }
    @Override
    public void combineBlock(String guid, String fileName) {
        //分片文件临时目录
        File tempPath = new File(uploadPath + "temp" + File.separator + guid);
        String prefix = fileName.substring(0, fileName.lastIndexOf("."));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String finalName = prefix + "." + userId + guid;
        //真实上传路径
        String filePath = uploadPath + "real";
        File realPath = new File(filePath);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        File realFile = new File(uploadPath + "real" + File.separator + finalName + suffix);
        // 文件追加写入
        FileOutputStream os = null;
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            log.info("Combining....");
            Long fileId = fileDao.getFileIdByFileName(finalName);
            if (null == fileId){
                log.info("File didn't exist in database...");
                log.info("Combine block start...");
                log.info("File name is " + fileName + ", MD5: " + guid);
                os = new FileOutputStream(realFile, true);
                fcout = os.getChannel();
                if (tempPath.exists()) {
                    //获取临时目录下的所有文件
                    File[] tempFiles = tempPath.listFiles();
                    //按名称排序
                    Arrays.sort(tempFiles, (o1, o2) -> {
                        if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                            return -1;
                        }
                        if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName())) {
                            return 0;
                        }
                        return 1;
                    });
                    //每次读取10MB大小，字节读取
                    //byte[] byt = new byte[10 * 1024 * 1024];
                    //int len;
                    //设置缓冲区为10MB
                    ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
                    for (int i = 0; i < tempFiles.length; i++) {
                        FileInputStream fis = new FileInputStream(tempFiles[i]);
                        fcin = fis.getChannel();
                        if (fcin.read(buffer) != -1) {
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                fcout.write(buffer);
                            }
                        }
                        buffer.clear();
                        fis.close();
                        //删除分片
                        tempFiles[i].delete();
                    }
                    os.close();
                    //删除临时目录
                    if (tempPath.isDirectory() && tempPath.exists()) {
                        // 回收资源
                        System.gc();
                        tempPath.delete();
                    }
                    log.info("Combine finished...");
                    log.info("File name is " + fileName + ", MD5: " + guid);
                    FileRecord fileRecord = new FileRecord(finalName, uploadPath + "real" + File.separator + finalName + suffix,
                            FileUtil.getFormatSize(realFile.length()), userId, guid, fileName.substring(fileName.lastIndexOf(".")), backupPath + fileName + guid);
                    fileDao.addFileRecord(fileRecord);
                    //backupFile(filePath + File.separator, backupPath, finalName + suffix, guid);
                }
            }else{
                log.info("File already existed...");
                File[] tempFiles = tempPath.listFiles();
                for (int i = 0; i < tempFiles.length; i++) {
                    tempFiles[i].delete();
                }
                System.gc();
                tempPath.delete();
            }

        } catch (Exception e) {
            log.error("Combine failed...");
            log.error(e.getMessage());
        }
    }


    @Override
    public void checkMd5(HttpServletRequest request, HttpServletResponse response) {
        //当前分片
        String chunk = request.getParameter("chunk");
        //分片大小
        String chunkSize = request.getParameter("chunkSize");
        //当前文件的MD5值
        String guid = request.getParameter("guid");
        //分片上传路径
        String tempPath = uploadPath + "temp";
        File checkFile = new File(tempPath + File.separator + guid + File.separator + chunk);
        response.setContentType("text/html;charset=utf-8");
        try {
            //如果当前分片存在，并且长度等于上传的大小
            if (checkFile.exists() && checkFile.length() == Integer.parseInt(chunkSize)) {
                response.getWriter().write("{\"ifExist\":1}");
            } else {
                response.getWriter().write("{\"ifExist\":0}");
            }
        } catch (IOException e) {
            log.error("(checkMD5)IOException: " + e.getMessage());
        }
    }

}
