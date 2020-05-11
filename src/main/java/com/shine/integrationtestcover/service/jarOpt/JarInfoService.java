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
    public List<JarInfo> selectByProject(String prj_name) {
        return jarInfoMapper.selectByProject(prj_name);
    }

    @Override
    public JarInfo selectByPK(String prj_name, String version) {
        return jarInfoMapper.selectByPK(prj_name, version);
    }

    @Override
    public int insert(JarInfo jarInfo) {
        return jarInfoMapper.insert(jarInfo);
    }

    @Override
    public int deleteByPK(String prj_name, String version) {
        return jarInfoMapper.deleteByPK(prj_name, version);
    }

    @Override
    public int deleteProject(String prj_name) {
        return jarInfoMapper.deleteProject(prj_name);
    }

    @Override
    public int update(JarInfo jarInfo) {
        return jarInfoMapper.update(jarInfo);
    }
}
