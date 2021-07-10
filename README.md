# agent-demo

分为两个应用
- agent 实现Java agent
- app 启动类

# Java agent 简介

启动时加载的JavaAgent（以下所说的JavaAgent均代表启动时加载的JavaAgent）是JDK 1.5之后引入的新特性，
此特性为用户提供了在JVM将字节码文件读入内存之后，使用对应的字节流在Java堆中生成一个Class对象之前，
对其字节码进行修改的能力，而JVM也会使用用户修改过的字节码进行Class对象的创建。

## 如何定义？

Java Agent是我们常见的java类，在类中需要编写premain方法，premain方法有两种定义：

```java
public static void premain(String agentArgs, Instrumentation inst)

public static void premain(String agentArgs)
```
JVM 会优先加载 带 Instrumentation 签名的方法，加载成功忽略第二种，如果第一种没有，则加载第二种方法。

会涉及两个参数:
- String agentArgs: 使用agent时，可以给agent传递参数
- Instrumentation inst: 可以添加自定义ClassFileTransformer，动态修改字节码数据

## 如何定义？
使用 javaagent 需要几个步骤：

- 定义一个 MANIFEST.MF 文件，必须包含 Premain-Class 选项，通常也会加入Can-Redefine-Classes 和 Can-Retransform-Classes 选项。
- 创建一个Premain-Class 指定的类，类中包含 premain 方法，方法逻辑由用户自己确定。
- 将 premain 的类和 MANIFEST.MF 文件打成 jar 包。

## 如何使用？
有两种方式:

- JVM 一起启动。使用参数 -javaagent: jar包路径 启动要代理的方法。
- JVM 启动之后 挂载(参考arthas)。
```
## 1、attach 到目标进程
virtualMachine = VirtualMachine.attach("" + configure.getJavaPid());

## 2、在jvm启动后就agent，第一个是agent的jar位置，第二个传递的参数
## 了解更多可以参考 java.lang.instrument.Instrumentation
virtualMachine.loadAgent(arthasAgentPath,
                    configure.getArthasCore() + ";" + configure.toString());
```

# 参考
- https://www.cnblogs.com/rickiyang/p/11368932.html
- https://zhuanlan.zhihu.com/p/141449080
