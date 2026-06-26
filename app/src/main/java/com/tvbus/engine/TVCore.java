package com.tvbus.engine;

import android.content.Context;

public class TVCore {
    
    // 单例模式，确保全局只有一个 TVBus 引擎实例
    private static TVCore instance;

    // 静态代码块：在类加载时自动加载 tvcore.so
    static {
        try {
            System.loadLibrary("tvcore");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            System.err.println("加载 tvcore.so 失败，请检查 jniLibs 目录架构是否匹配！");
        }
    }

    public static TVCore getInstance() {
        if (instance == null) {
            instance = new TVCore();
        }
        return instance;
    }

    // ----------------------------------------------------
    // 下面这些 native 方法的声明，必须与 tvcore.so 内部的符号一一对应
    // ----------------------------------------------------

    // 1. 基础生命周期控制
    public native void init(Context context);
    public native int start(String url); // 启动 P2P 引擎，传 null 即可
    public native void stop();
    public native void quit();

    // 2. 核心鉴权配置 (这里的参数将从你的 live2.json 中读取)
    public native void setMKBroker(String broker);
    public native void setAuthUrl(String url);
    public native void setUsername(String username);
    public native void setPassword(String password);

    // 3. 端口与网络配置
    public native void setPlayPort(int port); // 本地播放器请求的端口，比如 8902
    public native void setServPort(int port); // P2P 服务端口，通常随便设个 8901
    
    // 4. 状态监听回调
    public native void setListener(TVListener listener);
    
    // 5. 其他辅助功能
    public native String getVersion();
    public native void setDomainSuffix(String suffix);
}
