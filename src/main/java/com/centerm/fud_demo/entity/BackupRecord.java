package com.centerm.fud_demo.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BackupRecord {
    private Long fileId;
    private String name;
    private String localUrl;
    private Long userId;
    private String createTime;
    private String md5;
    private String suffix;

    public BackupRecord(){}

    public BackupRecord(Long fileId, String name, String localUrl, Long userId, String md5, String suffix)
    {
        this.fileId = fileId;
        this.name = name;
        this.localUrl = localUrl;
        this.userId = userId;
        this.md5 = md5;
        this.suffix = suffix;
    }
}
