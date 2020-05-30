
package com.shine.integrationtestcover.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: WHQ
 * @Date: 2019/3/23 19:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class RunServiceTest {
    @Autowired
    RunTestService runTestService=new RunTestService();

    @Before
    public void initiate(){
    }

    @Test
    public void HandleJavaTest(){
        //runTestService.handleJavaFile();
    }

    @Test
    public void GetPackageName(){
//        runTestService.initate("demo", false);
//        String Javafilename="Test2";
//        System.out.println(runTestService.getPackagename(Javafilename));
//        runTestService.handlePackageName(Javafilename);
    }

    @Test
    public void CompileJava(){
//        runTestService.initate("bean-query", false);
//        String Javafilename="PropertyComparatorTest";
//        runTestService.compileJava(Javafilename);
    }

    @Test
    public void getMethods(){
//        runTestService.initate("bean-query", false);
//        System.out.println(runTestService.getMethods("PropertyComparatorTest"));
    }
    //Test2


    @Test
    public void InvokeMethod(){//跑一个方法
//        runTestService.initate("demo", true);
//        String Javafilename="Test2";
//        List list=runTestService.invokeMethod(Javafilename,"test");
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i));
//        }
    }

    @Test
    public void InvokeJavaFile(){//跑一个java文件
//        runTestService.initate("demo", true);
//        String Javafilename="Test2";
//        runTestService.runTest(Javafilename);
    }

    @Test
    public void run() throws Exception{
//        runTestService.initate("bean-query", false);
//        runTestService.runAll();
//        System.out.println(runTestService.getRunprocess());
//        System.out.println(runTestService.getRunprocess());

    }

    @Test
    public void testasync() throws  Exception{
        runTestService.runAll();

    }
}

