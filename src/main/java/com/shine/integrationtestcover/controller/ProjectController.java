package com.shine.integrationtestcover.controller;

import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.service.DirService;
import com.shine.integrationtestcover.service.jarOpt.JarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/5/10 13:56 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
@RestController
public class ProjectController {
    @Autowired
    private BaseConfig baseConfig;
    @Autowired
    private DirService dirService;
    @Autowired
    private JarInfoService jarInfoService;

    //创建一个项目
    @GetMapping("/createProject")
    ResponseEntity<String> createProject(String prj_name){
        if(prj_name != null) {
            File file = new File(baseConfig.getProjectPath(prj_name));
            if (file.exists()) { //文件夹已存在
                return ResponseEntity.badRequest().body("该项目已存在！");
            }
            if (file.mkdirs()) {
                //将项目信息添加至数据库

                return ResponseEntity.ok().body("创建项目成功");
            }
            return ResponseEntity.status(500).body("创建项目失败！");
        }
        return ResponseEntity.badRequest().body("项目名不能为空！");
    }

    //查看所有的项目
    @GetMapping("/allProject")
    ResponseEntity<String[]> allProjects(){
        return ResponseEntity.ok().body(dirService.getSubDir(baseConfig.getUploadedFilePath()));
    }

    //删除一个项目
    @DeleteMapping("/deleteProject")
    ResponseEntity<String> deleteProject(String prj_name){
        //判断项目是否存在
        String project_path = baseConfig.getProjectPath(prj_name);
        File f = new File(project_path);
        if(!f.exists()){
            return ResponseEntity.badRequest().body("项目不存在！");
        }

        //删除Version文件夹
        dirService.deleteDir(project_path);
        //在数据库中删除JarInfo
        jarInfoService.deleteProject(prj_name);

        return ResponseEntity.ok().body("删除成功");
    }
}
