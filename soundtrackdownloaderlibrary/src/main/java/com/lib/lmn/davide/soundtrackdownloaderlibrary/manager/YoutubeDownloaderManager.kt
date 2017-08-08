package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.net.Uri
import com.lib.lmn.davide.soundtrackdownloaderlibrary.BuildConfig
import com.lib.lmn.davide.soundtrackdownloaderlibrary.models.YoutubeDownloaderFile
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by davide-syn on 6/30/17.
 */
class YoutubeDownloaderManager(val youtubeDownloaderService: YoutubeDownloaderModule.YoutubeDownloaderService,
                               val fileDownloaderManager: FileDownloaderManager) {

    /**
     * handle sound track
     */
    fun getSoundTrack(soundTrackObservable: Observable<YoutubeDownloaderFile>) {
        soundTrackObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .doOnError { throwable -> throwable.printStackTrace() }
                .onErrorReturn { null }
                .subscribe { soundTrackUrl -> fileDownloaderManager.getSoundTrack(soundTrackUrl?.link.let { link -> Uri.parse(link) }) }
    }

    companion object {
        val FORMAT_TYPE = "JSON"
    }

    /**
     * @return
     */
    fun fetchSoundTrackUrlByVideoId(videoId: String) {
        val observable =  youtubeDownloaderService.fetchUrlByVideoId(FORMAT_TYPE,
                BuildConfig.YOUTUBE_BASE_PATH + videoId)
        getSoundTrack(observable)
    }


}
