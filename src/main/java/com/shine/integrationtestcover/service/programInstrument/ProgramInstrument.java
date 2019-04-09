package com.shine.integrationtestcover.service.programInstrument;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class ProgramInstrument {
    public static void BytechaZhuang(InputStream i,String path,String name) throws IOException {
        //将输入流转为二进制数组
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = i.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        //读入byte[]
    ClassReader cr = new ClassReader(in2b);
    ClassNode cn = new ClassNode();
    cr.accept((ClassVisitor) cn, 0);
    //asm对二进制数组进行操作
    transform(cn);
    System.out.println("success");
    ClassWriter cw = new ClassWriter(0);
    cn.accept(cw);
    byte[] toByte = cw.toByteArray();
    File file2 = new File(path+"\\"+name);

    FileOutputStream fout = new FileOutputStream(file2);
    try {
        fout.write(cw.toByteArray());

        fout.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public static void JavachaZhuang(String name) throws IOException {
        ClassReader cr = new ClassReader(name);
        ClassNode cn = new ClassNode();
        cr.accept((ClassVisitor) cn, 0);
        transform(cn);
        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);
        byte[] toByte = cw.toByteArray();
        File file2 = new File(name);
        FileOutputStream fout = new FileOutputStream(file2);
        try {
            fout.write(cw.toByteArray());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void transform(ClassNode cn) {
        for (MethodNode mn : (List<MethodNode>) cn.methods) {
            if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
                continue;
            }
            InsnList insns = mn.instructions;
            if (insns.size() == 0) {
                continue;
            }
            Iterator<AbstractInsnNode> j = insns.iterator();
            while (j.hasNext()) {
                System.out.println();
                AbstractInsnNode in = j.next();
                int op = in.getOpcode();
                if(in instanceof MethodInsnNode){
                    MethodInsnNode methodInsnNode = (MethodInsnNode)in;
                    InsnList i2 = new InsnList();
                    i2.add(new FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;"));
                    String temp=cn.name+ ":" + mn.name+" CALL "+methodInsnNode.owner+ ":" +methodInsnNode.name;
                    i2.add(new LdcInsnNode(temp));
                    i2.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println", "(Ljava/lang/String;)V", false));
                    if(in.getPrevious()!=null)
                    insns.insert(in.getPrevious(), i2);
                }
            }
            mn.maxStack += 5;
        }
    }
}