package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.content.Context
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule

/**
 * Created by davide-syn on 7/7/17.
 */

class SoundTrackDownloaderManager private constructor(context: Context, listener: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks) {
    val fileDownloaderManager: FileDownloaderManager = SoundTrackDownloaderModule(context, listener).getFileDownloaderManager()
    var youtubeDownloaderManager: YoutubeDownloaderManager = YoutubeDownloaderModule(fileDownloaderManager).getYoutubeDownloadManager()

    init {
        instance = this
    }

    fun downloadAndPlaySoundTrack(videoId: String) = youtubeDownloaderManager.fetchSoundTrackUrlByVideoId(videoId)

    companion object {
        lateinit var instance: SoundTrackDownloaderManager

        fun getInstance(context: Context, listener: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks): SoundTrackDownloaderManager {
            instance = SoundTrackDownloaderManager(context, listener)
            return instance
        }
    }
}
