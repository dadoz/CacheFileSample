package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.content.Context
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule

/**
 * Created by davide-syn on 7/7/17.
 */

class SoundTrackDownloaderManager private constructor() {
    var context: Context? = null
    var listener: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks? = null
    val fileDownloaderManager: FileDownloaderManager = SoundTrackDownloaderModule(context, listener).getFileDownloaderManager()
    var youtubeDownloaderManager: YoutubeDownloaderManager = YoutubeDownloaderModule(fileDownloaderManager).getYoutubeDownloadManager()

    init {
        instance = this
    }

    fun downloadAndPlaySoundTrack(videoId: String) = youtubeDownloaderManager.fetchSoundTrackUrlByVideoId(videoId)

    companion object {
        lateinit var instance: SoundTrackDownloaderManager
            private set
    }
}
