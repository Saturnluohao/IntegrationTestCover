package com.shine.integrationtestcover.controller;

import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.domain.JarInfo;
import com.shine.integrationtestcover.service.DirService;
import com.shine.integrationtestcover.service.jarOpt.JarInfoService;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Shine
 * @Date: 2019/3/17
 */
@RestController
public class FileUploadController {
    @Autowired
    private BaseConfig baseConfig;
    @Autowired
    private JarInfoService jarInfoService;
    @Autowired
    private DirService dirService;

    //对某一个项目创建一个版本
    @PostMapping(value = "/createVersion")
    public ResponseEntity<String> uploadJar(JarInfo jarInfo, @RequestPart("file") MultipartFile file){
        //判断项目是否存在
        File f = new File(baseConfig.getProjectPath(jarInfo.getPrj_name()));
        if(!f.exists()){
            return ResponseEntity.badRequest().body("项目不存在！");
        }
        //判断该版本是否存在
        String version_path = baseConfig.getVersionPath(jarInfo.getPrj_name(), jarInfo.getVersion());
        f = new File(version_path);
        if(f.exists()){
            return ResponseEntity.badRequest().body("该版本已存在！");
        }

        //判断文件是否为空
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("请上传 Jar 包！");
        }

        //一切就绪
        try {
            //创建一个版本文件夹
            f.mkdirs();

            //新建一个依赖文件夹
            String dependency_path = version_path + "dependency";
            f = new File(dependency_path);
            f.mkdirs();

            //新建一个空文件，并创建输出流。
            String pathname = version_path + "source.jar";
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(pathname)));
            //将前端传来的文件写入新建的文件
            out.write(file.getBytes());
            //清空并关闭输入流
            out.flush();
            out.close();

            //将jar包信息存入数据库
            jarInfo.setTime(new Date());
            jarInfoService.insert(jarInfo);

            //在 Instrumentation 文件夹中创建版本文件夹
            String s = baseConfig.getInstrumentationVersionPath(jarInfo.getPrj_name(), jarInfo.getVersion());
            File f2 = new File(s);
            f2.mkdirs();

            //在 uploadTestCase 文件夹中创建版本文件夹
            s = baseConfig.getTestCaseVersionPath(jarInfo.getPrj_name(), jarInfo.getVersion());
            File f3 = new File(s);
            f3.mkdirs();

            return ResponseEntity.ok().body("上传成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败," + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败," + e.getMessage());
        }
    }

    //上传依赖
    @PostMapping(value = "/uploadDep")
    public ResponseEntity<String> uploadDep(String prj_name, String version, @RequestPart("file")MultipartFile file){
        //判断项目是否存在
        File f = new File(baseConfig.getProjectPath(prj_name));
        if(!f.exists()){
            return ResponseEntity.badRequest().body("项目不存在！");
        }

        //判断该版本是否存在
        String version_path = baseConfig.getVersionPath(prj_name, version);
        f = new File(version_path);
        if(!f.exists()){
            return ResponseEntity.badRequest().body("该版本不存在！");
        }

        //判断文件是否为空
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("请上传依赖文件！");
        }

        //将文件存入依赖文件夹
        try {
            String dependency_path = baseConfig.getDependencyPath(prj_name, version) + file.getOriginalFilename();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(dependency_path)));
            //将前端传来的文件写入新建的文件
            out.write(file.getBytes());
            //清空并关闭输入流
            out.flush();
            out.close();
            return ResponseEntity.ok().body("上传成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败：" + e.getMessage());
        }
    }

    @GetMapping(value = "/allVersion")
    public ResponseEntity<List<JarInfo>> allVersion(String prj_name){
        return ResponseEntity.ok().body(jarInfoService.selectByProject(prj_name));
    }

    @PostMapping(value = "/uploadRegressiveJar")
    public String uploadRegressiveJar(@RequestParam("file") MultipartFile file){
        String result = "";
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(baseConfig.getRegressionFilePath() + file.getOriginalFilename())));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result =  "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                result =  "上传失败," + e.getMessage();
            }
            result = "上传成功";
        } else {
            result =  "上传失败，因为文件是空的.";
        }
        return result;
    }

    @GetMapping(value = "/fileList")
    public HashMap<String, Object> getFileList(){
        File uploadedDirectory = new File(baseConfig.getUploadedFilePath());
        ArrayList filenames = new ArrayList();
        if(uploadedDirectory.isDirectory()) {
            File[] files = uploadedDirectory.listFiles();
            for (File file : files) {
                if(!file.isDirectory()) {
                    filenames.add(file.getName());
                }
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", filenames);
        return result;
    }

    //删除某项目的某个版本
    @DeleteMapping(value = "/deleteVersion")
    public ResponseEntity<String> deleteJar(String prj_name, String version){
        //判断项目是否存在
        File f = new File(baseConfig.getProjectPath(prj_name));
        if(!f.exists()){
            return ResponseEntity.badRequest().body("项目不存在！");
        }

        //判断该版本是否存在
        String version_path = baseConfig.getVersionPath(prj_name, version);
        f = new File(version_path);
        if(!f.exists()){
            return ResponseEntity.badRequest().body("该版本不存在！");
        }

        //删除四个Version文件夹
        dirService.deleteDir(version_path);
        String instrument_version_path = baseConfig.getInstrumentationVersionPath(prj_name, version);
        dirService.deleteDir(instrument_version_path);
        String testCase_version_path = baseConfig.getTestCaseVersionPath(prj_name, version);
        dirService.deleteDir(testCase_version_path);
        String runTest_version_path = baseConfig.getRunTestVersionPath(prj_name, version);
        dirService.deleteDir(runTest_version_path);
        //在数据库中删除JarInfo
        jarInfoService.deleteByPK(prj_name, version);

        return ResponseEntity.ok().body("删除成功");
    }
}
