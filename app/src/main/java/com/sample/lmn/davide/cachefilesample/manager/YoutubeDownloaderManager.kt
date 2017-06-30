package com.sample.lmn.davide.cachefilesample.manager

import android.net.Uri
import com.sample.lmn.davide.cachefilesample.BuildConfig
import com.sample.lmn.davide.cachefilesample.modules.YoutubeDownloaderModule.YoutubeDownloaderService
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by davide-syn on 6/30/17.
 */
class YoutubeDownloaderManager(val youtubeDownloaderService: YoutubeDownloaderService,
                               val downloadSoundtrackManager: DownloadSoundtrackManager?) {
    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val observable =  youtubeDownloaderService.fetchUrlByVideoId(FORMAT_TYPE,
                BuildConfig.YOUTUBE_BASE_PATH + videoId)
        getSoundTrack(observable)
    }

    /**
     * handle sound track
     */
    fun getSoundTrack(soundTrackObsservable: Observable<String>) {
        soundTrackObsservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .doOnError { throwable -> throwable.printStackTrace() }
                .onErrorReturn { throwable -> "" }
                .subscribe { soundTrackUrl -> downloadSoundtrackManager?.getSoundTrack(Uri.parse(soundTrackUrl)) }
    }

    companion object {
        val FORMAT_TYPE = "JSON"
    }

}
