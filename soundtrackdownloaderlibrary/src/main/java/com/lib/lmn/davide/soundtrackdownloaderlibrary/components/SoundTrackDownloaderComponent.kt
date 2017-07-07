package com.lib.lmn.davide.soundtrackdownloaderlibrary.components

import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.SoundTrackDownloaderManager
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import dagger.Component
import javax.inject.Singleton


/**
 * Created by davide-syn on 6/26/17.
 */

@Singleton
@Component(modules = arrayOf(SoundTrackDownloaderModule::class))
interface SoundTrackDownloaderComponent {
    fun inject(manager: SoundTrackDownloaderManager)
}