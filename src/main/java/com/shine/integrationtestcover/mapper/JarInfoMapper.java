package com.shine.integrationtestcover.mapper;

import com.shine.integrationtestcover.domain.JarInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * XXXXXXXXX科技有限公司 版权所有 © Copyright 2018<br>
 *
 * @Description: <br>
 * @Project: hades <br>
 * @CreateDate: Created in 2020/4/17 20:49 <br>
 * @Author: <a href="abc@qq.com">abc</a>
 */
@Mapper
public interface JarInfoMapper {
    //查看所有的 jar 包
    @Select("Select * from jar_info")
    List<JarInfo> selectAll();

    //根据 name 查看某个 jar 包信息
    @Select("select * from jar_info where name = #{name}")
    JarInfo selectByName(String name);

    //上传一个 jar 包信息
    @Insert("insert into jar_info(name,time,version,author,description) " +
                "values(#{name},#{time},#{version},#{author},#{description});")
    int insert(JarInfo jarInfo);

    //根据 name 删除一个 jar 包信息
    @Delete("delete from jar_info where name = #{name}")
    int deleteByName(String name);

    //更新某个 jar 包信息
    @Update("update jar_info " +
            "set time = #{time}, version = #{version}, author = #{author}, description = #{description} " +
            "where name = #{name}")
    int update(JarInfo jarInfo);
}
