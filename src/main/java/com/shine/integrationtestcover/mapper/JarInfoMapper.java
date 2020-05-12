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
    //查看所有的项目的所有版本
    @Select("Select * from jar_info")
    List<JarInfo> selectAll();

    //查看某个项目的所有版本
    @Select("Select * from jar_info where prj_name = #{prj_name}")
    List<JarInfo> selectByProject(String prj_name);

    //查看某个项目的某个版本
    @Select("select * from jar_info where prj_name = #{prj_name} and version = #{version}")
    JarInfo selectByPK(String prj_name, String version);

    //上传某个项目的某个版本
    @Insert("insert into jar_info(prj_name,version,author,time,description) " +
                "values(#{prj_name},#{version},#{author},#{time},#{description});")
    int insert(JarInfo jarInfo);

    //删除某项目的某个版本
    @Delete("delete from jar_info where prj_name = #{prj_name} and version = #{version}")
    int deleteByPK(String prj_name, String version);

    //删除一个项目
    @Delete("delete from jar_info where prj_name = #{prj_name}")
    int deleteProject(String prj_name);

    //更新某个 jar 包信息
    @Update("update jar_info " +
            "set time = #{time}, author = #{author}, description = #{description} " +
            "where prj_name = #{prj_name} and version = #{version}")
    int update(JarInfo jarInfo);
}
