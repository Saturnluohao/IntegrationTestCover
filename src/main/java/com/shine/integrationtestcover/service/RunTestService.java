package com.shine.integrationtestcover.service;

import com.shine.integrationtestcover.config.BaseConfig;
import com.shine.integrationtestcover.utils.CommonUtils;
import javafx.util.Pair;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WHQ
 * @Date: 2019/3/23 19:36
 */
@Service
public class RunTestService {

    @Autowired
    BaseConfig baseConfig;

    @Autowired
    CommonUtils commonUtils;

    private static int task = 0;//完成的方法数

    private List<String> allmethods = new LinkedList<>();

    private List runresults = new LinkedList();

    private List runprocess = new LinkedList();

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());//测试异步

    //project jar
    private String jarpath = "";
    private String jarname = "";//不带jar

    //test .java
    private String javafilepath = "";

    //test way(JUnit)
    // private String testwaypath = "C://Users//22831//Desktop//target";
    private String testwaypath = "";
    private String testwayname = "junit-4.10";

    public List<String> getAllmethods() {
        return allmethods;
    }

    public int getTask() {
        return task;
    }


    private String prj_name;
    private String version;

    //初始化，接收项目名称

//    public void initate(String projectname, boolean needWait) {
//        System.out.println(projectname+".jar");
//        if(needWait) {
//            while (!ProgramInstrumentService.situation.containsKey(projectname + ".jar") || ProgramInstrumentService.situation.get(projectname + ".jar") != 2) {
//                try {
//                    System.out.println("why");
//                    Thread.sleep(50);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        commonUtils.deleteDir(new File(baseConfig.getRunTestProjectPath(projectname)));
//        commonUtils.copyDic(baseConfig.getUploadedTestPath(projectname), baseConfig.getRunTestProjectPath(projectname));
//        commonUtils.copyFile(projectname + ".jar", baseConfig.getInstrumentationPath(), baseConfig.getRunTestProjectPath(projectname));
//        this.runprocess = new LinkedList();
//        this.setTestwaypath(baseConfig.getUploadedFilePath().replaceFirst("/", ""));
//        this.setJarpath(baseConfig.getRunTestProjectPath(projectname).replaceFirst("/", ""));//插桩后的位置
//        this.setJarname(projectname);
//        this.setJavafilepath(baseConfig.getRunTestProjectPath(projectname).replaceFirst("/", ""));//测试文件位置
//    }

    public void initate(String prj_name, String version, boolean needWait) {
        if(needWait) {
            Pair<String, String> key = new Pair<>(prj_name, version);
            //如果待测试版本还没有插桩，那么等待插桩完成
            while (!ProgramInstrumentService.situation.containsKey(key) || ProgramInstrumentService.situation.get(key) != 2) {
                try {
                    System.out.println("why");
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        commonUtils.deleteDir(new File(baseConfig.getRunTestVersionPath(prj_name, version)));
        commonUtils.copyDic(baseConfig.getTestCaseVersionPath(prj_name, version), baseConfig.getRunTestVersionPath(prj_name, version));
//        commonUtils.copyFile("source.jar", baseConfig.getInstrumentationVersionPath(prj_name, version), baseConfig.getRunTestVersionPath(prj_name, version));
        this.runprocess = new LinkedList();
        this.setTestwaypath(baseConfig.getUploadedFilePath().replaceFirst("/", ""));
        this.setJarpath(baseConfig.getRunTestVersionPath(prj_name, version).replaceFirst("/", ""));//插桩后的位置
        this.setJarname("source");
        this.setJavafilepath(baseConfig.getRunTestVersionPath(prj_name, version).replaceFirst("/", ""));//测试文件位置

        this.prj_name = prj_name;
        this.version = version;
    }

    /*
    读出一个testfile目录下的的java文件的包名，默认第一行是包名，匹配,返回包名,形式为'com.example.demo.controller'
     */
    public String getPackagename(String javafilename) {
        String packagename = "";
        try {
            File file = new File(javafilepath + "//" + javafilename + ".java");
            if (!file.exists()) {
                System.out.println("javafile:" + javafilename + "read package name failed");
            }
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            packagename = br.readLine();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packagename.replace("package ", "").replace(";", "");
    }

    /*
    文件复制
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }


    /*
    在某个位置建和包名一样的文件夹,并且复制java文件到对应包名下面,返回现在的java文件的path
     */
    public String handlePackageName(String javafilename) {
        String path = getPackageAbsolutePath(javafilename);
        File javaFile = new File(path + "//" + javafilename + ".java");
        File dirFile = new File(path);
        if (dirFile.exists()) {
            //System.out.println("已存在包目录");
        } else {
            //  System.out.println("不存在包目录");
            dirFile.mkdirs();

        }
        copyFile(this.javafilepath + "//" + javafilename + ".java", path + "//" + javafilename + ".java");
        return path;

    }

    /*
    获取包名目录的java文件的目录
     */
    public String getPackageAbsolutePath(String javafilename) {
        String packagename = getPackagename(javafilename);
        String[] names = packagename.split("\\.");
        String path = this.javafilepath + "//";//指定父目录(改),测试文件的位置
        for (int i = 0; i < names.length; i++) {
            path = path + names[i] + "/";
        }
        return path;
    }


    /*
    编译java文件,需要把java文件复制到新建的包目录下面，
     */
    public void compileJava(String javafilename) {
        String packagename = getPackagename(javafilename).replace(".", "//");
        String path = handlePackageName(javafilename);
        try {
            //String command=
            // "javac -cp C:\Users\22831\Desktop\lib\IntegrationTestCover.jar;C:\Users\22831\Desktop\lib\junit-4.10.jar com\shine\integrationtestcover\service\GraphServiceTest.java";
            String command = "javac -cp " +
                    getClasspath(prj_name, version) +
                    " -d " + javafilepath +
                    " " + javafilepath + javafilename + ".java";
            System.out.println(command);
            if(command.equals("javac -cp C:/Users/acer/Documents/GitHub/IntegrationTestCover/target/classes/runTestCase/bean-query/bean-query.jar;C:/Users/acer/Documents/GitHub/IntegrationTestCover/target/classes/uploadedJar/junit-4.10.jar C:/Users/acer/Documents/GitHub/IntegrationTestCover/target/classes/runTestCase/bean-query/cn//jimmyshi//beanquery//comparators//PropertyComparatorTest.java")){
                System.out.println("jhjj");
            }

//            Scanner sc = new Scanner(System.in);
//            while(true){
//                try {
//                    command = sc.nextLine();
//                    Process process = Runtime.getRuntime().exec(command);
//                    printLines(command + " stdout:", process.getInputStream());
//                    printLines(command + " stderr: ", process.getErrorStream());
//                    process.waitFor();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }


            Process process = Runtime.getRuntime().exec(command);
//            printLines(command + " stdout:", process.getInputStream());
//            printLines(command + " stderr:", process.getErrorStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }

    /*
    获得一个java文件里面的测试用例的方法名字
     */
    public List<String> getMethods(String javafilename) {
        if (!ifcompiled(javafilename)) {
            compileJava(javafilename);
        }
        String packagename = getPackagename(javafilename);
        List<String> methods = new LinkedList<>();
        try {
            File file = new File(this.getJarpath() + "//" + this.getJarname() + ".jar");//加载外部jar包
            URL url = file.toURI().toURL();
            File xFile = new File(this.getJavafilepath());
            URL url2 = xFile.toURL();
            URLClassLoader ClassLoader = new URLClassLoader(new URL[]{url2, url});
            Class xClass = ClassLoader.loadClass(packagename + "." + javafilename);//一个java文件
            // Class xClass = ClassLoader.loadClass("com.example.demo.controller.TestMethod");
            Method[] method = xClass.getDeclaredMethods();
            for (Method m : method) {
                //获得java文件的所有方法
                //System.out.println(m.getName());
                methods.add(m.getName());
            }
        } catch (ClassNotFoundException e) {
            System.out.println(javafilename + "编译失败！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methods;

    }

    /*
        invoke methods:filename是java文件的名字,invoke 一个java文件的某个方法,返回调用关系
     */
    @Async
    public List<String> invokeMethod(String javafilename, String methodname) {
        System.out.println("运行one method:" + Thread.currentThread().getName());
        List finishtask = new LinkedList();
        int sumtask = 1;

        finishtask.add(0);
        finishtask.add(sumtask);
        this.runprocess = finishtask;
        String packagename = getPackagename(javafilename);
        if (!ifcompiled(javafilename)) {
            compileJava(javafilename);
        }
        //String packagename="com.example.demo.controller";
        PrintStream old = System.out;
        try {
//            //File file = new File("C://Users//22831//Desktop//lib//demo.jar");
//            File file = new File(this.getJarpath() + "//" + this.getJarname() + ".jar");
//            //URL url = new URL("file:"+jarpath+"//"+jarname);
//            URL url = file.toURI().toURL();
//            //  System.out.println("url" + url);
//            //   URLClassLoader loader= new URLClassLoader(new URL[] { url }, Thread.currentThread().getContextClassLoader());
//            // File xFile=new File("C://Users//22831//Desktop");
//            File xFile = new File(this.javafilepath);
//            URL url2 = xFile.toURL();
//            URLClassLoader ClassLoader = new URLClassLoader(new URL[]{url2, url});
            URLClassLoader ClassLoader = new URLClassLoader(getDependencyList(prj_name,version, javafilepath));
            //Class xClass = ClassLoader.loadClass("com.example.demo.controller.TestMethod");
            Class xClass = ClassLoader.loadClass(packagename + "." + javafilename);
            FileOutputStream bos = new FileOutputStream(this.javafilepath + "//output-" + javafilename + ".txt");
            System.setOut(new PrintStream(bos));
            //Method xMethod = xClass.getDeclaredMethod("te11");//先定义跑里面的一个方法
            Method xMethod = xClass.getDeclaredMethod(methodname);
            xMethod.setAccessible(true);
            xMethod.invoke(xClass.newInstance());
            bos.close();

        } catch (ClassNotFoundException e) {
            System.out.println(javafilename + "编译失败！！！");
        } catch (InvocationTargetException e) {
            System.out.println(javafilename + "cuowu");
        } catch (Exception e) {
            e.printStackTrace();
        }

        finishtask.clear();
        finishtask.add(1);
        finishtask.add(sumtask);
        this.runprocess = finishtask;
        //重定向到控制台
        System.setOut(old);

        List<String> methodsrelationship = new LinkedList<>();

        //读文件内容
        try {
            String filepath = this.javafilepath + "//output-" + javafilename + ".txt";
            File file = new File(filepath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(".*CALL.*")) {
                    methodsrelationship.add(line.split("=>")[0].replace("/", "."));
                }
            }
            br.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.runresults = methodsrelationship;
        return methodsrelationship;
    }

    /*
    跑一个java文件下的所有的测试用例，java文件名字,异步,返回两个数值
     */
    @Async
    public void runTest(String javafilename) {
        List finishtask = new LinkedList();
        System.out.println("运行one java task:" + Thread.currentThread().getName());
        // compileJava(javafilename);
        allmethods = new LinkedList<>();
        task = 0;
        List<String> sumtask = getMethods(javafilename);//java文件里面所有的方法

        finishtask.add(0);
        finishtask.add(sumtask);
        this.runprocess = finishtask;

        try {
            File file = new File(this.jarpath + File.separator + this.jarname + ".jar");//加载外部jar包
            URL url = file.toURI().toURL();

            File xFile = new File(this.javafilepath);
            URL url2 = xFile.toURL();
            URLClassLoader ClassLoader = new URLClassLoader(new URL[]{url2, url});

            for (int i = 0; i < sumtask.size(); i++) {
                finishtask.clear();
                List<String> now = invokeMethod(javafilename, sumtask.get(i));
                if (now != null && now.size() > 0) {
                    allmethods.addAll(now);
                    this.runresults = allmethods;
                }
                task++;
                finishtask.clear();
                finishtask.add(task);
                finishtask.add(sumtask.size());
                this.runprocess = finishtask;
                logger.info("=============" + Thread.currentThread().getName() + "异步");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<File> getAllTestFileFromDic(File dic) {
        List<File> result = new ArrayList<>();
        if (!dic.exists()) {
            System.out.println("项目目录不存在");
            return new ArrayList<>();
        } else {
            for (File f : dic.listFiles()) {
                visitFile(result, f);
            }
            return result;
        }

    }

    public void visitFile(List<File> result, File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                visitFile(result, f);
            }
        } else {
            if (file.getName().contains(".java")) {
                result.add(file);
            }
        }
    }

    /*
        跑项目下面的所有test文件,返回n和m两个数
    */
    @Async
    public void runAll() throws Exception {
        List finishtask = new LinkedList();
        System.out.println("运行one project:" + Thread.currentThread().getName());
        task = 0;
        int sumtask = 0;
        List results = new LinkedList<>();
        String path = this.javafilepath;
        File file = new File(path);
        List<File> tempList = getAllTestFileFromDic(file);
        //深度优先遍历 并存下来给下次使用
        for (File f : tempList) {
            if (f.getName().contains(".java")) {
                int m = getMethods(f.getName().replace(".java", "")).size();
                sumtask += m;
            }
        }
        finishtask.add(0);
        finishtask.add(sumtask);
        this.runprocess = finishtask;

        for (File f : tempList) {
            if (f.getName().contains(".java")) {
                String filename = f.getName().replace(".java", "");
                List<String> m = getMethods(filename);
                for (String s : m) {
                    results.addAll(invokeMethod(filename, s));
                    task++;
                    finishtask.clear();
                    finishtask.add(task);
                    finishtask.add(sumtask);
                    this.runprocess = finishtask;
                    this.runresults = results;
                }
            }
        }
        logger.info("==***===========" + Thread.currentThread().getName() + "异步");
    }

    public void runAllWithMaven() throws Exception {
        String MAVEN_PATH = "";
        String FILE_PATH = "";
        File testBaseDir = new File(FILE_PATH);
        logger.info(execCmd(MAVEN_PATH + " " + "test", testBaseDir));
        // get all the files in ./target/test-output
        List<String> methodsRelationship = new LinkedList<>();
        LinkedList<File> fileList = new LinkedList<>();
        LinkedList<File> dirList = new LinkedList<>();
        dirList.add(new File(FILE_PATH + "/target/test-output"));
        while (dirList.size() != 0) {
            File dir = dirList.pop();
            File[] subs = dir.listFiles();
            if (subs == null) {return;}
            for (File file : subs) {
                if (file.isFile()) {
                    fileList.add(file);
                } else {
                    dirList.add(file);
                }
            }
        }
        for (File file : fileList) {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(".*CALL.*")) {
                    methodsRelationship.add(line.split("=>")[0].replace("/", "."));
                }
            }
            br.close();
            reader.close();
        }
        this.runresults = methodsRelationship;
        int totalTestNum = fileList.size();
        this.runprocess = new LinkedList<Integer>();
        this.runprocess.add(totalTestNum);
        this.runprocess.add(totalTestNum);
    }
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            process = Runtime.getRuntime().exec(cmd, null, dir);
            process.waitFor();
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            if (process != null) {
                process.destroy();
            }
        }
        return result.toString();
    }
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public HashMap<String, List<String>> regressionCompare(String prj_name, String oldVersion) throws Exception {
        System.out.println("reCompare");
        HashMap<String, List<String>> compare = new HashMap<>();
//        this.setJarpath(baseConfig.getRunTestProjectPath(projectname).replaceFirst("/", ""));//插桩后的位置
        this.setJarpath(baseConfig.getRunTestVersionPath(prj_name, oldVersion).replaceFirst("/", ""));
//        this.setJarname(projectname);
        this.setJarname("source.jar");
//        this.setJavafilepath(baseConfig.getRunTestProjectPath(projectname).replaceFirst("/", ""));//测试文件位置
        this.setJavafilepath(baseConfig.getRunTestVersionPath(prj_name, oldVersion).replaceFirst("/", ""));
        String path = this.javafilepath;
        File file = new File(path);
        List<File> tempList = getAllTestFileFromDic(file);
        for (File f : tempList) {
            if (f.getName().contains(".java")) {
                String filename = f.getName().replace(".java", "");
                List<String> temp = new ArrayList<String>();
                temp.addAll(invokeRegressionMethod(filename));
                compare.put(f.getName(), temp);

            }
        }
        return compare;
    }



    /*
     回归测试的解析，A call B=>desc1=>desc2=>C
     解析为A+desc1 call C + desc2
     */
    public List<String> invokeRegressionMethod(String javafilename) {
        List<String> methodsrelationship = new LinkedList<>();
        //读文件内容
        try {
            String filepath = this.javafilepath + "//output-" + javafilename + ".txt";//txt位置
            File file = new File(filepath);
            if (file == null) {
                System.out.println("txt生成失败!!!");
            }
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(".*CALL.*")) {
                    //methodsrelationship.add((!line.contains("=>")? line : line.split("=>")[0]).replace("/", "."));
                    String[] a = line.split("CALL");
                    String A = a[0].replace(" ", "");//A的空格去掉
                    String Aafter = a[1];//B=>desc1=>desc2=>C
                    String[] after = Aafter.split("=>");
                    String B = after[0].replace(" ", "");
                    String desc1 = after[1];
                    String desc2 = after[2];
                    String C="";
                    if (after.length == 3) {
                        C = B;
                    } else if (after.length == 4) {
                        C = after[3].replace("/", ".");
                    }
                    String finalline = A + desc1 + " " + "CALL" + " " + C  + desc2;
                    methodsrelationship.add(finalline);


//
                }
            }
            br.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return methodsrelationship;
    }


    /*
    是否被编译过，编译过返回true，未编译返回false
     */
    boolean ifcompiled(String javafilename) {
        String path = getPackageAbsolutePath(javafilename);
//        System.out.println("java" + javafilename);
//        System.out.println("path" + path);
        File packagepath = new File(path);
        if (packagepath.listFiles() == null) {return false;}
        for (File f : Objects.requireNonNull(packagepath.listFiles())) {
            System.out.println("file" + f.getName());
            if (f.getName().equals(javafilename + ".class")) {
                return true;
            }

        }


        return false;
    }


    //根据pom下载依赖
    public void downloadDependency(String pomPath) {
        try {
            String command = "mvn.cmd dependency:copy-dependencies -f " + pomPath;
            System.out.println(command);
            Process process = Runtime.getRuntime().exec(command);
            printLines(command + " stdout:", process.getInputStream());
            printLines(command + " stderr: ", process.getErrorStream());
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断pom文件是否存在
    boolean isPomExists(String projectName, String version){
        String pomPath = baseConfig.getDependencyPath(projectName, version) + "/pom.xml";
        File pomFile = new File(pomPath);
        if (pomFile.exists()){
            return true;
        }else {
            return false;
        }
    }

    //判断依赖是否已经下载
    boolean isDependencyDownloaded(String projectName, String version){
        String denpendencyDir = baseConfig.getDependencyPath(projectName, version) + "/target";
        File file = new File(denpendencyDir);
        if (file.exists()){
            return true;
        }else {
            return false;
        }
    }

    //获取类路径
    public String getClasspath(String projectName, String version){
        if(isPomExists(projectName, version) && !isDependencyDownloaded(projectName, version)){
            String dependencyPath = baseConfig.getDependencyPath(projectName, version);
            if (dependencyPath.startsWith("/")){
                dependencyPath = dependencyPath.substring(1);
            }
            downloadDependency(dependencyPath + "/pom.xml");
        }
        StringBuffer sb = new StringBuffer();
        List<File> dependencyFileList = getDependencyFile(projectName, version);
        for (File file :
                dependencyFileList) {
            sb.append(file.getAbsolutePath());
            sb.append(";");  //windows使用分号作为类路径分隔符
        }
        return sb.toString();
    }

    //获取所有依赖文件对象
    List<File> getDependencyFile(String projectName, String version){
        List<File> dependencyFileList = new ArrayList<>();
        String depDir = baseConfig.getDependencyPath(projectName, version);
        File pomDependencyDirFile = new File(depDir + "target/dependency");
        File commonDependencyDirFile = new File(depDir);
        dependencyFileList.add(new File(baseConfig.getInstrumentationVersionPath(projectName, version) + "source.jar"));
        if (pomDependencyDirFile.exists()){
            for(File file : pomDependencyDirFile.listFiles()){
                if(file.getName().endsWith(".jar")){
                    dependencyFileList.add(file);
                }
            }
        }
        for (File file : commonDependencyDirFile.listFiles()){
            if(file.getName().endsWith(".jar")){
                dependencyFileList.add(file);
            }
        }
        return dependencyFileList;
    }

    //获取所有依赖和测试文件的URL对象列表，以供加载
    URL[] getDependencyList(String projectName, String version, String testFilePath){
        File runTestDirFile = new File(baseConfig.getRunTestProjectPath(projectName));
        List<URL> urlList = new ArrayList<>();
        try {
            urlList.add(new File(testFilePath).toURL());
            for(File file : getDependencyFile(projectName, version)){
                urlList.add(file.toURL());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL[] urlArray = new URL[urlList.size()];
        for (int i = 0; i < urlList.size(); i++) {
            urlArray[i] = urlList.get(i);
        }
        return urlArray;
    }

    //打印命令行的输出
    private void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }

    public String getJarpath() {
        return jarpath;
    }

    public void setJarpath(String jarpath) {
        this.jarpath = jarpath;
    }

    public String getJarname() {
        return jarname;
    }

    public void setJarname(String jarname) {
        this.jarname = jarname;
    }

    public String getJavafilepath() {
        return javafilepath;
    }

    public void setJavafilepath(String javafilepath) {
        this.javafilepath = javafilepath;
    }

    public void setTestwaypath(String testwaypath) {
        this.testwaypath = testwaypath;
    }

    public void setTestwayname(String testwayname) {
        this.testwayname = testwayname;
    }

    public List getRunresults() {
        return runresults;
    }

    public void setRunresults(List runresults) {
        this.runresults = runresults;
    }

    public List getRunprocess() {
        return runprocess;
    }

    public void setRunprocess(List runprocess) {
        this.runprocess = runprocess;
    }
}
