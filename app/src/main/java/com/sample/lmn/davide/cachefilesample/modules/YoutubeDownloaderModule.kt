package com.sample.lmn.davide.cachefilesample.modules

import android.content.Context
import com.sample.lmn.davide.cachefilesample.BuildConfig
import com.sample.lmn.davide.cachefilesample.components.SoundTrackDownloaderComponent
import com.sample.lmn.davide.cachefilesample.manager.YoutubeDownloaderManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by davide-syn on 6/29/17.
 */
@Module
open class YoutubeDownloaderModule(context: Context, lst: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks) {
    var youtubeDownloaderAp: YoutubeDownloaderService

    @Inject
    lateinit var provider: Provider<SoundTrackDownloaderComponent.Builder>

    //component injection
    val component: SoundTrackDownloaderComponent? = null

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

//        //inject subcomponent
//        component = provider.get()
//                .soundTrackDownloaderModule(SoundTrackDownloaderModule(context, lst))
//                .build()
    }

    @Provides
    fun provideYoutubeDownloaderService(): YoutubeDownloaderService {
        return youtubeDownloaderAp
    }

    @Provides
    fun provideYoutubeDownloadManager(): YoutubeDownloaderManager {
        return YoutubeDownloaderManager(youtubeDownloaderAp, component?.downloadSoundTrackManager())
    }

    /**
     * interface
     */
    interface YoutubeDownloaderService {
        @GET("fetch")
        fun fetchUrlByVideoId(@Query("format") format: String, @Query("video") video: String): Observable<String>
    }
}
