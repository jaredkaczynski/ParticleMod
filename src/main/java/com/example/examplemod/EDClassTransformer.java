package com.example.examplemod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.lwjgl.Sys;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Map;

import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.BIPUSH;

public class EDClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String arg0, String arg1, byte[] arg2) {
        if (arg0.equals("bkn") || arg0.equals("bkn/a")) {
            System.out.println("********* INSIDE OBFUSCATED Particle TRANSFORMER ABOUT TO PATCH: " + arg0);
            return patchClassASMCount(arg0, arg2, true);
        }

        if (arg0.equals("net.minecraft.client.particle.EffectRenderer")) {
            System.out.println("********* INSIDE Particle TRANSFORMER ABOUT TO PATCH: " + arg0);
            return patchClassASMCount(arg0, arg2, false);
        }
        if (arg0.equals("bkd")) {
            System.out.println("********* INSIDE OBFUSCATED Particle constructor ABOUT TO PATCH: " + arg0);
            return patchClassASMLifetime(arg0, arg2, true);
        }

        if (arg0.equals("net.minecraft.client.particle.EntityFireworkSparkFX")) {
            System.out.println("********* INSIDE Particle constructor ABOUT TO PATCH: " + arg0);
            return patchClassASMLifetime(arg0, arg2, false);
        }
        return arg2;
    }


    public byte[] patchClassASMLifetime(String name, byte[] bytes, boolean obfuscated) {

        String targetMethodName = "";
//Our target method
        targetMethodName = "init";


//set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

//Now we loop over all of the methods declared inside the Explosion class until we get to the targetMethodName "doExplosionB"

        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            int fdiv_index = -1;
            System.out.println("Particles: " + m.name);

//Check if this is doExplosionB and it's method signature is (Z)V which means that it accepts a boolean (Z) and returns a void (V)
            if (m.name.contains(targetMethodName)) {
                System.out.println("Particles: " + "********* Inside target constructor (there should only be 1 right?!");

                AbstractInsnNode currentNode = null;
                AbstractInsnNode targetNode = null;

                @SuppressWarnings("unchecked")
                Iterator<AbstractInsnNode> iter = m.instructions.iterator();

                int index = -1;

//Loop over the instruction set and find the instruction FDIV which does the division of 1/explosionSize
                while (iter.hasNext()) {
                    index++;
                    currentNode = iter.next();

//Found it! save the index location of instruction FDIV and the node for this instruction
                    if (currentNode.getOpcode() == BIPUSH) {
                        targetNode = currentNode;
                        fdiv_index = index;
                        // make new instruction list
                        InsnList toInject = new InsnList();
//add your own instruction lists: *USE THE ASM JAVADOC AS REFERENCE*
                        //Change 22000 to whatever you want your particles to be
                        toInject.add(new IntInsnNode(BIPUSH, 96));
                        System.out.println(m.instructions.get(fdiv_index-1).getOpcode());
                        System.out.println(m.instructions.get(fdiv_index).getOpcode());
                        System.out.println(m.instructions.get(fdiv_index+1).getOpcode());
                        m.instructions.insertBefore(targetNode, toInject);
                        m.instructions.remove(targetNode);
                        System.out.println(m.instructions.get(fdiv_index-1).getOpcode());
                        System.out.println(m.instructions.get(fdiv_index).getOpcode());
                        System.out.println(m.instructions.get(fdiv_index+1).getOpcode());
                    }
                }
                System.out.println("Patching Constructor Complete!");
                break;
            }
        }


//ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public byte[] patchClassASMCount(String name, byte[] bytes, boolean obfuscated) {

        String targetMethodName = "";
//Our target method
        if (obfuscated == true)
            //targetMethodName = "func_78873_a";
            targetMethodName = "a";
        else
            targetMethodName = "addEffect";


//set up ASM class manipulation stuff. Consult the ASM docs for details
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

//Now we loop over all of the methods declared inside the Explosion class until we get to the targetMethodName "doExplosionB"

        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            int fdiv_index = -1;
            System.out.println("Particles: " + m.name);

//Check if this is doExplosionB and it's method signature is (Z)V which means that it accepts a boolean (Z) and returns a void (V)
            if ((m.name.equals(targetMethodName) && (m.desc.equals("(Lbkm;)V") || m.desc.equals("(Lnet/minecraft/client/particle/EntityFX;)V")))) {
                System.out.println("Particles: " + "********* Inside target method!");

                AbstractInsnNode currentNode = null;
                AbstractInsnNode targetNode = null;

                @SuppressWarnings("unchecked")
                Iterator<AbstractInsnNode> iter = m.instructions.iterator();

                int index = -1;

//Loop over the instruction set and find the instruction FDIV which does the division of 1/explosionSize
                while (iter.hasNext()) {
                    index++;
                    currentNode = iter.next();

//Found it! save the index location of instruction FDIV and the node for this instruction
                    if (currentNode.getOpcode() == SIPUSH) {
                        targetNode = currentNode;
                        fdiv_index = index;
                        // make new instruction list
                        InsnList toInject = new InsnList();
//add your own instruction lists: *USE THE ASM JAVADOC AS REFERENCE*
                        //Change 22000 to whatever you want your particles to be
                        toInject.add(new IntInsnNode(SIPUSH, 22000));
                        m.instructions.insertBefore(targetNode, toInject);
                        m.instructions.remove(targetNode);
                    }
                }
                System.out.println("Patching Method Complete!");
                break;
            }
        }


//ASM specific for cleaning up and returning the final bytes for JVM processing.
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
