package com.sample.lmn.davide.cachefilesample.modules

import com.sample.lmn.davide.cachefilesample.BuildConfig
import com.sample.lmn.davide.cachefilesample.manager.YoutubeDownloaderManager
import com.sample.lmn.davide.cachefilesample.models.YoutubeDownloaderFile
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by davide-syn on 6/29/17.
 */
@Module(includes = arrayOf(SoundTrackDownloaderModule::class))
open class YoutubeDownloaderModule {
    var youtubeDownloaderAp: YoutubeDownloaderService

    /**
     * constructor
     */
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.YOUTUBE_DOWNLOADER_BASE_PATH)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        youtubeDownloaderAp = retrofit.create(YoutubeDownloaderService::class.java)

    }

    @Provides
    fun provideYoutubeDownloaderService(): YoutubeDownloaderService {
        return youtubeDownloaderAp
    }

    @Provides
    fun provideYoutubeDownloadManager(): YoutubeDownloaderManager {
        return YoutubeDownloaderManager(youtubeDownloaderAp)
    }

    /**
     * interface
     */
    interface YoutubeDownloaderService {
        @GET("fetch")
        fun fetchUrlByVideoId(@Query("format") format: String, @Query("video") video: String): Observable<YoutubeDownloaderFile>
    }
}
