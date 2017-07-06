package com.sample.lmn.davide.cachefilesample.components

import com.sample.lmn.davide.cachefilesample.MainActivity

/**
 * Created by davide-syn on 6/26/17.
 */

//@Singleton
//@Component(modules = arrayOf(SoundTrackDownloaderModule::class))
interface SoundTrackDownloaderComponent {
    fun inject(activity: MainActivity)
}