package com.sample.lmn.davide.cachefilesample.components

import com.sample.lmn.davide.cachefilesample.manager.DownloadSoundtrackManager
import com.sample.lmn.davide.cachefilesample.modules.SoundTrackDownloaderModule
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * Created by davide-syn on 6/26/17.
 */

@Singleton
@Subcomponent(modules = arrayOf(SoundTrackDownloaderModule::class))
interface SoundTrackDownloaderComponent {

    fun downloadSoundTrackManager(): DownloadSoundtrackManager

    @Subcomponent.Builder
    interface Builder {
        fun soundTrackDownloaderModule(module: SoundTrackDownloaderModule): Builder
        fun build(): SoundTrackDownloaderComponent
    }
}