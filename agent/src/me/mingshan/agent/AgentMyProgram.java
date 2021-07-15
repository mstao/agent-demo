package me.mingshan.agent;

import java.lang.instrument.Instrumentation;

/**
 * 在主程序运行之后的代理程序
 * @author hanjuntao walkerhan@126.com
 * @date 2021/7/15
 */
public class AgentMyProgram {
    /**
     * 该方法在main方法之后运行，与main方法运行在同一个JVM中
     * 并被同一个System ClassLoader装载
     * 被统一的安全策略(security policy)和上下文(context)管理
     */
    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("====agentmain 方法执行");
        System.out.println(agentOps);
        System.out.println(inst);
    }

    /**
     * 如果不存在 premain(String agentOps, Instrumentation inst)
     * 则会执行 premain(String agentOps)
     */
    public static void agentmain(String agentOps) {
        System.out.println("====agentmain ====");
        System.out.println(agentOps);
    }
}
