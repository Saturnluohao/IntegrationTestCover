package com.shine.integrationtestcover.config;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author: Shine
 * @Date: 2019/3/18
 */
@Component
public class BaseConfig {
    private String uploadedFilePath = "";
    private String instrumentationPath = "";
    private String regressionTestPath = "";

    public String getUploadedFilePath() {
        if(this.uploadedFilePath.isEmpty()) {
            this.uploadedFilePath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "uploadedJar/";
            try{
                this.uploadedFilePath = java.net.URLDecoder.decode(this.uploadedFilePath, "UTF-8");
            }catch(Exception e)
            {
                e.printStackTrace();
            };
            File directory=new File(this.uploadedFilePath);
            if(!directory.exists())
            {
                try{
                    directory.mkdirs();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return this.uploadedFilePath;
    }

    /*
     * 以下三个路径在 uploadjar 文件夹里
     */
    public String getProjectPath(String prj_name){
        return getUploadedFilePath() + prj_name + '/';
    }

    public String getVersionPath(String prj_name, String version){
        return getProjectPath(prj_name) + version + '/';
    }

    public String getDependencyPath(String prj_name, String version){
        return getVersionPath(prj_name,version) + "dependency/";
    }

    /*
     * 以下两个路径在 instrumentation 文件夹里
     */
    public String getInstrumentationProjectPath(String prj_name){
        return getInstrumentationPath() + prj_name + '/';
    }

    public String getInstrumentationVersionPath(String prj_name, String version){
        return getInstrumentationProjectPath(prj_name) + version + '/';
    }

    /*
     * 以下三个路径在 uploadedTestCase 文件夹里
     */
    public String getTestCasePath(){
        return getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "uploadedTestCase/";
    }

    public String getTestCaseProjectPath(String prj_name){
        return getTestCasePath() + prj_name + "/" ;
    }

    public String getTestCaseVersionPath(String prj_name, String version){
        return getTestCaseProjectPath(prj_name) + version + '/';
    }


    public String getRegressionFilePath() {
        if(this.regressionTestPath.isEmpty()) {
            this.regressionTestPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "regressionTest/";
            try{
                this.regressionTestPath = java.net.URLDecoder.decode(this.regressionTestPath, "UTF-8");
            }catch(Exception e)
            {
                e.printStackTrace();
            };
            File directory=new File(this.regressionTestPath);
            if(!directory.exists())
            {
                try{
                    directory.mkdirs();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return this.regressionTestPath;
    }

    public String getInstrumentationPath() {
        if(this.instrumentationPath.isEmpty()) {
            this.instrumentationPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "instrumentation/";
            try{
                this.instrumentationPath = java.net.URLDecoder.decode(this.instrumentationPath, "UTF-8");
            }catch(Exception e)
            {
                e.printStackTrace();
            };
            File directory=new File(this.instrumentationPath);
            if(!directory.exists())
            {
                try{
                    directory.mkdirs();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return this.instrumentationPath;
    }

    public String getUploadedTestPath(String projectName) {
        String newname=projectName.replace(".jar","");
        String uploadedTestPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "uploadedTestCase/" + newname + "/" ;
        try{
            uploadedTestPath = java.net.URLDecoder.decode(uploadedTestPath, "UTF-8");
        }catch(Exception e)
        {
            e.printStackTrace();
        };
        File directory=new File(uploadedTestPath);
        if(!directory.exists())
        {
            try{
                directory.mkdirs();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return uploadedTestPath;
    }

    public String getRunTestProjectPath(String projectName) {
        String newname = projectName.replace(".jar","");
        String runTestPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "runTestCase/" + newname + "/" ;
        try{
            runTestPath = java.net.URLDecoder.decode(runTestPath, "UTF-8");
        }catch(Exception e)
        {
            e.printStackTrace();
        };
        File directory=new File(runTestPath);
        if(!directory.exists())
        {
            try{
                directory.mkdirs();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return runTestPath;
    }

    public String getRunTestVersionPath(String prj_name, String version){
        String runTestPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile()+ "runTestCase/" + prj_name + "/" + version + "/";
        try{
            runTestPath = java.net.URLDecoder.decode(runTestPath, "UTF-8");
        }catch(Exception e)
        {
            e.printStackTrace();
        };
        File directory=new File(runTestPath);
        if(!directory.exists())
        {
            try{
                directory.mkdirs();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return runTestPath;
    }
}
