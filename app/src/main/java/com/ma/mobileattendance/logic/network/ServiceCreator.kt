package com.ma.mobileattendance.logic.network

import android.util.Log
import com.ma.mobileattendance.MyApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object ServiceCreator {
    private const val BASE_URL = "https://192.168.1.108:8433"
    private val client = getHttpsClient()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
    private fun getHttpsClient(): OkHttpClient {
        var sslContext: SSLContext? = null
        val okhttpClient = OkHttpClient().newBuilder()
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            //方法一：将证书放到assets文件夹里面然后获取
            val certificates = MyApplication.context.assets.open("xll.cer")

            //方法二：为了不将证书打包到apk里面了，可以将证书内容定义成字符串常量，再将字符串转为流的形式
            //String wenzhibinBook = "拷贝证书里的内容到此处";
            //InputStream certificates = new ByteArrayInputStream(wenzhibinBook.getBytes("UTF-8"));
            // 创建秘钥，添加证书进去
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            val certificateAlias = Integer.toString(0)
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificates))
            // 创建信任管理器工厂并初始化秘钥
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            //获取SSL上下文对象，并初始化信任管理器
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
            //okhttp设置sokect工厂，并校验主机名
            okhttpClient.sslSocketFactory(sslContext.socketFactory)
            okhttpClient.hostnameVerifier { p0, p1 ->
                Log.d("https", "hostname:$p0,session:$p1")
//                p0.equals("hostname开发先不搞")
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return okhttpClient
            .readTimeout(7676, TimeUnit.MILLISECONDS)
            .connectTimeout(7676, TimeUnit.MILLISECONDS)
            .addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("User-Agent", "android")
                    .build()
                it.proceed(request)
            }
            .build()
    }

}