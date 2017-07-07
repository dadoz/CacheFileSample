package com.lib.lmn.davide.soundtrackdownloaderlibrary.components

import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.SoundTrackDownloaderManager
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.YoutubeDownloaderModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by davide-syn on 6/29/17.
 */
@Singleton
@Component(modules = arrayOf(YoutubeDownloaderModule::class, SoundTrackDownloaderModule::class))
interface YoutubeDownloaderComponent {
    fun inject(manager: SoundTrackDownloaderManager)
}