package com.sample.lmn.davide.cachefilesample.components

import com.sample.lmn.davide.cachefilesample.MainActivity
import com.sample.lmn.davide.cachefilesample.modules.DownloadManagerModule

import javax.inject.Singleton

import dagger.Component

/**
 * Created by davide-syn on 6/26/17.
 */

@Singleton
@Component(modules = arrayOf(DownloadManagerModule::class))
interface DownloadManagerComponent {
    fun inject(activity: MainActivity)
}