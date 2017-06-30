package com.sample.lmn.davide.cachefilesample.components

import com.sample.lmn.davide.cachefilesample.MainActivity
import com.sample.lmn.davide.cachefilesample.modules.YoutubeDownloaderModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by davide-syn on 6/29/17.
 */
@Singleton
@Component(modules = arrayOf(YoutubeDownloaderModule::class))
interface YoutubeDownloaderComponent {
    fun inject(activity: MainActivity)
}