package com.shine.integrationtestcover.service;

import com.shine.integrationtestcover.service.codeParse.MethodVisitor;
import com.shine.integrationtestcover.service.graphCustom.GraphAlo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    @Autowired
    private ParseJarService parseJarService;

    private String path;
    private String filename;
    private ArrayList<String> vertexResult;
    private ArrayList<String> result;
    private List<String> invokeString;
    private HashSet<String> classes;
    private HashMap<String, ArrayList<String>> methods;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ArrayList<HashMap<String, Object>> getVertex() {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        //System.out.println("vertex的个数为 ："+vertexResult.size());
        for (String s : this.vertexResult) {
            HashMap<String, Object> node = new HashMap<>();
            node.put("name", s);
            node.put("type", (new Random()).nextInt(3));
            result.add(node);
        }
        return result;
    }

    public ArrayList<String> getVertexResult() {
        return vertexResult;
    }

    public void setVertexResult(ArrayList<String> vertexResult) {
        this.vertexResult = vertexResult;
    }

    //获得调用string
    public void parse() {
        parseJarService.setFilename(filename);
        parseJarService.setPath(path);
        this.invokeString = parseJarService.getInvoking();
        //System.out.println("invokeString的长度为："+invokeString.size());
        // System.out.println("invokeString" +invokeString.size());
    }

    public void initiate() {
        MethodVisitor.setallMethodsEmpty();
        vertexResult = new ArrayList<String>();
        parse();
        result = new ArrayList<String>();
        ArrayList<String> vertex = new ArrayList<String>();
        result.addAll(invokeString);
        //System.out.println("result的长度为："+result.size());
        for (String temp : result) {
            String[] tempList = temp.split(" ");
            vertex.add(tempList[0]);
            vertex.add(tempList[2]);
        }
        vertex.addAll(MethodVisitor.allMethods);

        vertexResult = new ArrayList<String>(new HashSet<String>(vertex));//
        System.out.println("vertexResult:" + vertexResult.size());


    }

    //获取所有边
    public ArrayList<HashMap> getEdges() {

        int numOfEdge = 0;

        int vertexNum = vertexResult.size();
        GraphAlo graph = new GraphAlo(vertexNum);

        for (String s : vertexResult) {
            graph.insertVertex(s);
        }


        for (String temp : result) {
            String[] tempList = temp.split(" ");
            int startVertex = vertexResult.indexOf(tempList[0]);
            int endVertex = vertexResult.indexOf(tempList[2]);
            graph.insertEdge(startVertex, endVertex, 1);
        }
        ArrayList<HashMap> edges = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            for (int j = 0; j < vertexNum; j++) {
                if (graph.getWeight(i, j) == 1) {
                    numOfEdge++;
                    HashMap<String, Integer> content = new HashMap();
                    content.put("source", i);
                    content.put("target", j);
                    edges.add(content);

                }
            }
        }
        System.out.println("num" + numOfEdge);
        return edges;

    }

    public HashSet<String> getClasses() {
        return classes;
    }

    public void setClasses(HashSet<String> classes) {
        this.classes = classes;
    }

    public HashMap<String, ArrayList<String>> getMethods() {
        return methods;
    }

    public void setMethods(HashMap<String, ArrayList<String>> methods) {
        this.methods = methods;
    }


}
