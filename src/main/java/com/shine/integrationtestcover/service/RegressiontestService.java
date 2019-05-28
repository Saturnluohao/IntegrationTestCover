package com.shine.integrationtestcover.service;

import com.shine.integrationtestcover.regressiontest.*;
import com.shine.integrationtestcover.config.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegressiontestService {
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    RunTestService runTestService;
    public List<String> regressiontest(String oldJarName,String newJarName,String packageName) throws Exception {
        Graph graph=new Graph();
        graph.Graph(oldJarName, newJarName, packageName);
        List<String> dangerousList=new ArrayList<String>();
        List<String> result=new ArrayList<String>();
        System.out.println("dangerous edge");
        for(int i=0;i<graph.getDangerousList().size();i++){
           System.out.println(graph.getDangerousList().get(i).toString());
        }
        System.out.println("dangerous node");
        for(int i=0;i<graph.getDifferentNodeKey().size();i++){
            System.out.println(graph.getDifferentNodeKey().get(i));
        }
        System.out.println("new node");
        for(int i=0;i<graph.getNewNodeKey().size();i++){
            System.out.println(graph.getNewNodeKey().get(i));
        }
        System.out.println("delete node");
        for(int i=0;i<graph.getDeleteNodeKey().size();i++){
            System.out.println(graph.getDeleteNodeKey().get(i));
        }
        System.out.println("regressiveTest");
//        HashMap<String,List<String>> TestMap = runTestService.regressionCompare(oldJarName.split("\\.")[0]);
        HashMap<String,List<String>> TestMap=new HashMap<String,List<String>>();
        System.out.println(oldJarName.split("\\.")[0]);
        try{
            TestMap=runTestService.regressionCompare(oldJarName.split("\\.")[0]);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("regressiveTestEnd");
        for(Map.Entry<String,List<String>> entry:TestMap.entrySet()){
            List<String> temp=entry.getValue();
            for(int i=0;i<temp.size();i++){
                if(graph.getDangerousList().contains(temp.get(i))){
                    result.add(entry.getKey());
                    break;
                }
                for(String node: temp.get(i).split(" ")){
                    if(node!="CALL"){
                        if(graph.getDifferentNodeKey().contains(node)){
                            result.add(entry.getKey());
                            break;
                        }
                        if(graph.getNewNodeKey().contains(node)){
                            result.add(entry.getKey());
                            break;
                        }
                        if(graph.getDeleteNodeKey().contains(node)){
                            result.add(entry.getKey());
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
}
