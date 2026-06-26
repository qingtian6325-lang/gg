package com.tvbus.engine;

public interface TVListener {
    // 引擎初始化完成
    void onInited(String result);
    
    // 引擎开始运行
    void onStart(String result);
    
    // 引擎准备就绪 (此时代表可以通知播放器开始拉流了)
    void onPrepared(String result);
    
    // 运行时的各类信息/错误
    void onInfo(String result);
    
    // 引擎停止
    void onStop(String result);
    
    // 引擎完全退出
    void onQuit(String result);
}
