package com.shine.integrationtestcover.controller;


import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.service.GraphService;
import com.shine.integrationtestcover.service.ParseJarService;
import com.shine.integrationtestcover.service.ProgramInstrumentService;
import com.shine.integrationtestcover.service.codeParse.MethodVisitor;
import com.shine.integrationtestcover.service.programInstrument.JarFileInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class FileParserController {

    @Autowired
    GraphService graphService;

    @Autowired
    private ProgramInstrumentService programInstrumentService;

    @Autowired
    BaseConfig baseConfig;

    //生成调用关系图。prj_name和version为项目名和版本，packages为遍历范围，packagesToCall为生成范围。
    @GetMapping(value = "/relation")
    public HashMap<String, Object> getInvokeRelationship(@RequestParam String prj_name, @RequestParam String version,
                                                         @RequestParam String packages, @RequestParam String packagesToCall){
//        String prj_name = "project1";
//        String version = "1.0";
        ParseJarService.packageNames = packages.isEmpty()? new String[]{""}: packages.split("\n");
        MethodVisitor.packageToCallNames = packagesToCall.isEmpty()? new String[]{""}: packagesToCall.split("\n");
        JarFileInput.packageNames = packages.isEmpty()? new String[]{""}: packages.split("\n");
        graphService.setFilename("source.jar");
        graphService.setPath(baseConfig.getVersionPath(prj_name, version));
        //graphService.setFilename(name);
        //graphService.setPath(baseConfig.getUploadedFilePath());
        graphService.initiate();
        ArrayList edges=graphService.getEdges();
        ArrayList<HashMap<String, Object>> vertex=graphService.getVertex();
        HashMap<String, Object> result = new HashMap<>();
        result.put("nodes", vertex);
        result.put("links", edges);
        result.put("classes", MethodVisitor.classes.toArray());
        result.put("classMethodMap", MethodVisitor.methods);
        MethodVisitor.classes = new HashSet<>();
        MethodVisitor.methods = new HashMap<>();
        try {
            programInstrumentService.doInstrumentation(prj_name, version);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}
