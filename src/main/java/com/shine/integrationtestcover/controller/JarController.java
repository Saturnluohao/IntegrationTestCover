package com.shine.integrationtestcover.controller;

import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.domain.JarInfo;
import com.shine.integrationtestcover.service.jarOpt.JarInfoService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/4/17 21:44 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
@RestController
public class JarController {
    @Autowired
    private JarInfoService jarInfoService;
    @Autowired
    private BaseConfig baseConfig;

    //查看所有的 jar 包信息
    @GetMapping(value = "/showAllJars")
    public String showAll(){
        List<JarInfo> jarInfos = jarInfoService.selectAll();
        jarInfos.stream().forEach(System.out::println);
        return jarInfos.toString()+"";
    }

    //查看某一个 jar 包信息
//    @GetMapping(value = "/showOneJar")
//    public String showOne(String name){
//        JarInfo jarInfo = jarInfoService.selectByName(name);
//        return jarInfo.toString();
//    }

    //添加一个 jar 包信息
    @PostMapping(value = "/addJar")
    public ResponseEntity<String> addJar(JarInfo jarInfo, @RequestPart("file")MultipartFile file){
        jarInfo.setTime(new Date());
        System.out.println(jarInfo.toString());
        int result = jarInfoService.insert(jarInfo);
        if(result == 1){
            return ResponseEntity.ok().body("上传成功");
        }
        else{
            return ResponseEntity.badRequest().body("上传失败");
        }

    }

    //删除一个 jar 包信息
//    @DeleteMapping(value = "/deleteJar")
//    public String deleteJar(String name){
//        int result = jarInfoService.deleteByName(name);
//        return "删除的结果是："+result;
//    }

    @GetMapping(value = "/testforjxh")
    public String test(String prj_name){
        String prj_path = baseConfig.getProjectPath(prj_name);
        File file = new File(prj_path);
        if(file.exists()){
            return "存在";
        }
        return "不存在";
    }
}