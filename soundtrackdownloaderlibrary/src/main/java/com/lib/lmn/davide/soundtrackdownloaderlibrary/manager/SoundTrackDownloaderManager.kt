package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.content.Context
import com.lib.lmn.davide.soundtrackdownloaderlibrary.components.DaggerYoutubeDownloaderComponent
import com.lib.lmn.davide.soundtrackdownloaderlibrary.components.YoutubeDownloaderComponent
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import javax.inject.Inject

/**
 * Created by davide-syn on 7/7/17.
 */

class SoundTrackDownloaderManager(val context: Context, val listener: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks) {
    @Inject
    lateinit var fileDownloaderManager: FileDownloaderManager
    @Inject
    lateinit var youtubeDownloaderManager: YoutubeDownloaderManager

    val component: YoutubeDownloaderComponent by lazy {
        DaggerYoutubeDownloaderComponent
                .builder()
                .soundTrackDownloaderModule(SoundTrackDownloaderModule(context, listener))
                .youtubeDownloaderModule(YoutubeDownloaderModule())
                .build()
    }

    init {
        component.inject(this)
    }

    fun downloadAndPlaySoundTrack(videoId: String) {
        youtubeDownloaderManager.fetchSoundTrackUrlByVideoId(fileDownloaderManager, videoId)
    }
}
