package com.shine.integrationtestcover.service.programInstrument;

import sun.tools.jar.resources.jar;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class JarFileInput {
    public static String[] packageNames = {};
    //按照jar包内路径生成文件夹
    public static  boolean mkDirectory(String path){
        File file = null;
        try {

           file = new File(path);
            if (!file.exists()) {
                return file.mkdirs();
            }
            else{
                return false;
            }
        } catch (Exception e) {
        } finally {
            file = null;
        }
        return false;
    }
//    public static void  execCommand(ArrayList<String> l,String path){
//        BufferedReader br = null;
//        BufferedReader br2 = null;
//        StringBuilder sb = new StringBuilder();
//        StringBuilder sb2 = new StringBuilder();
//        File dir=new File(path);
//        for(String j:l){
//            System.out.println(j);
//            try {
//                Process p = Runtime.getRuntime().exec(j,null,dir);
//                br = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                br2 = new BufferedReader(new InputStreamReader(p.getErrorStream(),"GBK"));
//                String line2 = null;
//                while ((line2 = br2.readLine()) != null) {
//                    sb2.append(line2 + "\n");
//                }
//               int i= p.waitFor();
//                System.out.println(i);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            finally
//            {
//                if (br != null)
//                {
//                    try {
//                        br.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        System.out.println(sb.toString());
//        System.out.println(sb2.toString());
//
//    }
    public static void jarFileInput(String path,String filename) throws IOException {


        ArrayList<String> alist=new ArrayList<String>();
        try {
            //获取jar包内class路径
            File f = new File(path+ File.separator +filename);
            System.out.println(f);

            if (!f.exists()) {
                System.err.println("Jar file " + filename + " does not exist");
            }
            JarFile jar = new JarFile(f);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory())
                    continue;
                if (!entry.getName().endsWith(".class"))
                    continue;
                if(!entry.getName().startsWith("java")) {
                    for (String packageName : packageNames) {
                        if (entry.getName().startsWith(packageName.replace(".", "/"))) {
                            InputStream i = jar.getInputStream(entry);
                            String name = entry.getName();
                            System.out.println(name);
                            int index = name.lastIndexOf('/');
                            int index1 = name.indexOf('/');
                            if (index != -1) {
                                String md = name.substring(0, index);
                                mkDirectory(path + File.separator + md);
                            }
                            String sub = name.substring(index + 1);
                            if (index1 != -1) {
                                String s = name.substring(0, index1);
                                System.out.println(s);
                                if (s.equals("META-INF"))
                                    continue;
                            }
                            //插桩
                            ProgramInstrument.BytechaZhuang(i, path, name);
                            System.out.println("success!");
                            String command = "cmd /c " + "jar uvf " + "\"" + path + "\\" + filename + "\" " + name;
                            alist.add(command);
                        }
                    }
                }

            }
            jar.close();
            System.out.println("success!");
        } catch (IOException e) {
            System.err.println("Error while processing jar: " + e.getMessage());
            e.printStackTrace();
        }
        jar.clearCache();
        //execCommand(alist,path);

    }
}
