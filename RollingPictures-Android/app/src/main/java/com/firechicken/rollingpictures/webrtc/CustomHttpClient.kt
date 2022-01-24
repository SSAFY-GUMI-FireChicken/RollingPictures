package com.firechicken.rollingpictures.webrtc

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class CustomHttpClient(baseUrl: String, basicAuth: String) {

    private var client: OkHttpClient? = null
    private val baseUrl: String = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
    private val basicAuth: String = basicAuth

    // 통신을 위한 okhttp
    @Throws(IOException::class)
    fun httpCall(
        url: String,
        method: String?,
        contentType: String?,
        body: RequestBody?,
        callback: Callback?
    ) {
        var url = url
        url = if (url.startsWith("/")) url.substring(1) else url
        val request: Request = Request.Builder()
            .url(baseUrl + url)
            .header("Authorization", basicAuth).header("Content-Type", contentType!!)
            .method(method!!, body)
            .build()
        val call = client!!.newCall(request)
        call.enqueue(callback!!)
    }

    fun dispose() {
        client!!.dispatcher.executorService.shutdown()
    }

    init {
        try {

            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return emptyArray()
                    }
                }
            )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            client = OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }).hostnameVerifier(HostnameVerifier { hostname, session -> true }).build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
