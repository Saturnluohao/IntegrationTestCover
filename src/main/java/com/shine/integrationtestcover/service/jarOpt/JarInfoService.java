package com.shine.integrationtestcover.service.jarOpt;

import com.shine.integrationtestcover.domain.JarInfo;
import com.shine.integrationtestcover.mapper.JarInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/4/17 21:08 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
@Service
public class JarInfoService implements IJarInfoService{
    @Autowired
    private JarInfoMapper jarInfoMapper;

    @Override
    public List<JarInfo> selectAll(){
        return jarInfoMapper.selectAll();
    }

    @Override
    public JarInfo selectByName(String name) {
        return jarInfoMapper.selectByName(name);
    }

    @Override
    public int insert(JarInfo jarInfo) {
        return jarInfoMapper.insert(jarInfo);
    }

    @Override
    public int deleteByName(String name) {
        return jarInfoMapper.deleteByName(name);
    }

    @Override
    public int update(JarInfo jarInfo) {
        return jarInfoMapper.update(jarInfo);
    }



}
