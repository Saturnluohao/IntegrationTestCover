package com.shine.integrationtestcover.controller;

import com.shine.integrationtestcover.domain.JarInfo;
import com.shine.integrationtestcover.service.jarOpt.JarInfoService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //查看所有的 jar 包信息
    @GetMapping(value = "/showAllJars")
    public String showAll(){
        List<JarInfo> jarInfos = jarInfoService.selectAll();
        jarInfos.stream().forEach(System.out::println);
        return jarInfos.toString()+"";
    }

    //查看某一个 jar 包信息
    @GetMapping(value = "/showOneJar")
    public String showOne(String name){
        JarInfo jarInfo = jarInfoService.selectByName(name);
        return jarInfo.toString();
    }

    //添加一个 jar 包信息
    @PostMapping(value = "/addJar")
    public String addJar(JarInfo jarInfo, @RequestPart("file")MultipartFile file){
        jarInfo.setTime(new Date());
        System.out.println(jarInfo.toString());
        int result = jarInfoService.insert(jarInfo);
        return "插入的结果是：" + result + "\n文件：" + file;
    }

    //删除一个 jar 包信息
    @DeleteMapping(value = "/deleteJar")
    public String deleteJar(String name){
        int result = jarInfoService.deleteByName(name);
        return "删除的结果是："+result;
    }

    //更新一个 jar 包信息
}
