package com.shine.integrationtestcover.domain;

import lombok.Data;

import java.util.Date;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/4/17 20:47 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
@Data
public class JarInfo {
    private String prj_name;
    private String version;
    private String author;
    private Date time;
    private String description;
}
