package com.example.myapp // 注意：请替换成你实际的包名，或者删除这行如果报错

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tvbus.engine.TVCore
import com.tvbus.engine.TVListener
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private val TAG = "TVBusProxy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 在屏幕上显示一段简单的提示文字
        val textView = TextView(this)
        textView.text = "TVBus 代理服务启动中...\n请查看 Logcat 日志"
        textView.textSize = 18f
        textView.setPadding(50, 50, 50, 50)
        setContentView(textView)

        // 1. 读取 live2.json
        val configStr = readAssetFile("live2.json")
        Log.d(TAG, "读取到的配置文件长度: ${configStr.length}")

        // 2. 初始化并启动 TVBus 引擎
        startTVBusEngine()
    }

    private fun startTVBusEngine() {
        val tvCore = TVCore.getInstance()
        
        // 初始化
        tvCore.init(applicationContext)
        
        // 设置回调监听
        tvCore.setListener(object : TVListener {
            override fun onInited(result: String?) {
                Log.d(TAG, "onInited: $result")
            }
            override fun onStart(result: String?) {
                Log.d(TAG, "onStart: $result")
            }
            override fun onPrepared(result: String?) {
                Log.d(TAG, "onPrepared: $result -> 引擎准备就绪！")
                // 引擎准备好后，就可以通过 http://127.0.0.1:8902 访问了
            }
            override fun onInfo(result: String?) {
                Log.d(TAG, "onInfo: $result")
            }
            override fun onStop(result: String?) {
                Log.d(TAG, "onStop: $result")
            }
            override fun onQuit(result: String?) {
                Log.d(TAG, "onQuit: $result")
            }
        })

        // 设置端口 (播放器会请求 8902 端口)
        tvCore.setPlayPort(8902)
        tvCore.setServPort(8901)

        // 启动引擎
        tvCore.start(null)
    }

    // 辅助方法：读取 assets 文件夹里的文件
    private fun readAssetFile(fileName: String): String {
        return try {
            val inputStream: InputStream = assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 退出 App 时关闭引擎
        TVCore.getInstance().stop()
        TVCore.getInstance().quit()
    }
}
