package com.shine.integrationtestcover.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/5/10 20:47 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
@Service
public class DirService {
    //删除一个文件夹
    public void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if(file.isFile())
        {
            file.delete();
        }else
        {
            File[] files = file.listFiles();
            if(files == null)
            {
                file.delete();
            }else
            {
                for (int i = 0; i < files.length; i++)
                {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }
    //获取某文件夹下所有的子文件夹名
    public String[] getSubDir(String dirPath){
        File file = new File(dirPath);
        return file.list();
    }
}
