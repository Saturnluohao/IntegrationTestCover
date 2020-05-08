package com.shine.integrationtestcover.service.jarOpt;

import com.shine.integrationtestcover.domain.JarInfo;

import java.util.List;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/4/17 21:39 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
public interface IJarInfoService {
    //查看所有的 jar 包信息
    List<JarInfo> selectAll();
    //根据名字查看某个 jar 包信息
    JarInfo selectByName(String name);
    //上传 jar 包信息
    int insert(JarInfo jarInfo);
    //删除某个 jar 包信息
    int deleteByName(String name);
    //更改某个 jar 包信息
    int update(JarInfo jarInfo);
}
