package me.mingshan.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

/**
 * 可以动态修改载入jvm的字节码数据
 *
 * https://www.cnblogs.com/javammc/p/12543267.html
 * https://blog.csdn.net/hosaos/article/details/102931887
 *
 * @author hanjuntao walkerhan@126.com
 * @date 2021/7/10
 */
public class MyClassFileTransformer implements ClassFileTransformer {

    /**
     * 修改载入jvm中的字节码数据
     *
     * @param loader 加载字节码的类加载器，如果是BootStrapLoader，为null
     * @param className 类名，为jvm规范中的全限定名：如java/util/List
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer 加载的类的字节码文件，注意此处的数组内容有可能是被修改过的
     * @return 如果为null，代表字节码未被修改；
     * 非空则jvm会利用jvm进入下一个流程：创建Class对象，继续执行下一个transform方法等
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        // 确定拦截范围
        if (!"me/mingshan/app/Test".equals(className)) {
            return null;
        }

        System.out.println("要修改类的全限定名：" + className);

        return modifyClass(className);
    }

    private byte[] modifyClass(String className) {
        try {
            System.out.println("获取ClassPool");
            // 获取ClassPool
            ClassPool classPool = ClassPool.getDefault();
            // 注意此处
            // https://ask.csdn.net/questions/3535641

            System.out.println("获取ClassPool成功");

            System.out.println("获取池中的class对象");
            // 获取池中的class对象
            CtClass ctClass = classPool.getCtClass(className.replace("/", "."));
            System.out.println("找到main方法");
            // 找到main方法
            CtMethod main = ctClass.getDeclaredMethod("main");

            System.out.println("增加一个本地变量");
            // 增加一个本地变量
            main.addLocalVariable("beginTime", CtClass.longType);

            System.out.println("在main方法执行之前给beignTime 赋值");
            //System.out.println("在main方法执行之前给beignTime 赋值"); 在main方法执行之前给beignTime 赋值
            main.insertBefore("long beginTime = System.currentTimeMillis();" );

            System.out.println("在main方法执行之后打印耗时");
            // 在main方法执行之后打印耗时
            // System.out.println("总耗时：" + (System.currentTimeMillis() - beginTime));
            main.insertAfter("System.out.println(\"总耗时：\" + (System.currentTimeMillis() - beginTime));");

            byte[] bytes = ctClass.toBytecode();
            System.out.println("返回修改过后的字节码数组");
            // 返回修改过后的字节码数组
            System.out.println("返回的字节码：" + Arrays.toString(bytes));
            return bytes;
        } catch (Throwable e) {
            System.out.println("发生异常：" + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("未修改字节码，直接返回");
        return null;
    }
}
