package com.centerm.fud_demo.service.Impl;
import com.centerm.fud_demo.dao.FileDao;
import com.centerm.fud_demo.entity.FileForm;
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
import java.util.Map;

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
    public Map<String, Object> findByFileMd5(String md5) {
        //todo
        return null;
    }

    @Override
    public Map<String, Object> realUpload(FileForm form, MultipartFile multipartFile) throws Exception {
        //TODO
        return null;
    }

    @Override
    public Map<String, Object> check(FileForm form) throws Exception {
        String fileId = form.getUuid();
        int index = Integer.valueOf(form.getIndex());
        String partMd5 = form.getPartMd5();
        int total = Integer.valueOf(form.getTotal());
        //TODO
        return null;
    }

}
