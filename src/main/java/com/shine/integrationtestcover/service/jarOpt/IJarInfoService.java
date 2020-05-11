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
    //查看所有的项目的所有版本
    List<JarInfo> selectAll();
    //查看某个项目的所有版本
    List<JarInfo> selectByProject(String prj_name);
    //查看某个项目的某个版本
    JarInfo selectByPK(String prj_name, String version);
    //上传某个项目的某个版本
    int insert(JarInfo jarInfo);
    //删除某项目的某个版本
    int deleteByPK(String prj_name, String version);
    //删除一个项目
    int deleteProject(String prj_name);
    //更改某个 jar 包信息
    int update(JarInfo jarInfo);
}
