package com.sample.lmn.davide.cachefilesample.modules

import android.content.Context

import com.sample.lmn.davide.cachefilesample.manager.FileStorageManager
import com.sample.lmn.davide.cachefilesample.manager.DownloadSoundtrackManager

import java.lang.ref.WeakReference

import dagger.Module
import dagger.Provides

import com.sample.lmn.davide.cachefilesample.manager.FileStorageManager.OnCacheEntryRetrievesCallbacks

/**
 * Created by davide-syn on 6/26/17.
 */
@Module
class DownloadManagerModule(context: Context, private val lst: OnCacheEntryRetrievesCallbacks) {
    private val context: WeakReference<Context> = WeakReference(context)

    @Provides
    fun provideCacheManager(): FileStorageManager {
        return FileStorageManager(context.get(), lst)
    }

    @Provides
    fun provideSoundtrackManager(): DownloadSoundtrackManager {
        val fileStorageManager = FileStorageManager(context.get(), lst)
        return DownloadSoundtrackManager(context.get(), fileStorageManager)
    }
}
