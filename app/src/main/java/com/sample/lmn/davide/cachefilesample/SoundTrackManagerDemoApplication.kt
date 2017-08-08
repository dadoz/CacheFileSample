package com.sample.lmn.davide.cachefilesample
import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor

/**
 * Created by davide-syn on 8/8/17.
 */

class SoundTrackManagerDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        okhttp3.OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

    }
}
