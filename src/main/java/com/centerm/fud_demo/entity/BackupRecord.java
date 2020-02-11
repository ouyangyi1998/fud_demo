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

    public BackupRecord(){}

    public BackupRecord(Long fileId,String name,String localUrl,Long userId)
    {
        this.fileId = fileId;
        this.name = name;
        this.localUrl = localUrl;
        this.userId = userId;
    }
}
