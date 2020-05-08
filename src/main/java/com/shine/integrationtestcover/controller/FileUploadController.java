package com.shine.integrationtestcover.controller;

import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.service.ProgramInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: Shine
 * @Date: 2019/3/17
 */
@RestController
public class FileUploadController {
    @Autowired
    private BaseConfig baseConfig;


    @RequestMapping(value = "/uploadJar")
    @ResponseBody
    public String uploadJar(@RequestParam("file") MultipartFile file){
        String result = "";
        if (!file.isEmpty()) {
            try {
                //新建一个空文件，并创建输出流
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(baseConfig.getUploadedFilePath() + file.getOriginalFilename())));
                //将前端传来的文件写入新建的文件
                System.out.println(file.getName());
                out.write(file.getBytes());
                //清空并关闭输入流
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
    @RequestMapping(value = "/uploadRegressiveJar")
    @ResponseBody
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
    @RequestMapping(value = "/fileList", method = RequestMethod.GET)
    @ResponseBody
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



}
