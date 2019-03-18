package com.shine.integrationtestcover.service.codeParse;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ConstantPushInstruction;
import org.apache.bcel.generic.EmptyVisitor;

import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ReturnInstruction;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


/** parse method in class and output call info
 * Created by shikun on 2017/2/24.
 */

public class MethodVisitor extends EmptyVisitor  {
    JavaClass visitedClass;
    private MethodGen mg;
    private ConstantPoolGen cp;
    private String format;
    private String DegreeClass;
    private String DegreeMethod;
    private static List<String> CallRelationship=new LinkedList<>();



    public MethodVisitor(MethodGen m, JavaClass jc) {
        visitedClass = jc;
        mg = m;
        cp = mg.getConstantPool();

    }

    public void start() {
        if (mg.isAbstract() || mg.isNative())
            return;
        for (InstructionHandle ih = mg.getInstructionList().getStart();
             ih != null; ih = ih.getNext()) {

            Instruction i = ih.getInstruction();

            if (!visitInstruction(i))
                i.accept(this);
        }
    }
    //？？？what?
    private boolean visitInstruction(Instruction i) {
        short opcode = i.getOpcode();
        return ((InstructionConstants.INSTRUCTIONS[opcode] != null)
                && !(i instanceof ConstantPushInstruction)
                && !(i instanceof ReturnInstruction));
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL i) {

        String formatInternal = "%s";
        this.DegreeClass = String.format(formatInternal,i.getReferenceType(cp));
        this.DegreeMethod = i.getMethodName(cp);
        String output=visitedClass.getClassName() + ":" + mg.getName() + " CALL " + this.DegreeClass + ":" + this.DegreeMethod;
        CallRelationship.add(output);
        System.out.println(output);


    }

   public List<String> getCallRelationship() {
        return CallRelationship;
    }
}
