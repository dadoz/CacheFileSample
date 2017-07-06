package com.sample.lmn.davide.cachefilesample.modules

import android.content.Context
import com.android.volley.Response
import com.sample.lmn.davide.cachefilesample.manager.SoundTrackDownloaderManager
import com.sample.lmn.davide.cachefilesample.manager.FileStorageManager
import dagger.Module
import dagger.Provides
import java.io.FileInputStream
import java.lang.ref.WeakReference

/**
 * Created by davide-syn on 6/26/17.
 */
@Module
open class SoundTrackDownloaderModule(context: Context, lst: OnSoundTrackRetrievesCallbacks) {
//    val lst: WeakReference<OnSoundTrackRetrievesCallbacks> = WeakReference(lst)
    val context: WeakReference<Context> = WeakReference(context)
    val fileStorageManager: FileStorageManager = FileStorageManager(context, lst)

//    @Provides
//    fun provideCacheManager(): FileStorageManager {
//        return fileStorageManager
//    }

    @Provides
    fun provideSoundtrackManager(): SoundTrackDownloaderManager {
        val lst1 = Response.Listener<Any> { result -> println(result.toString().length) }
        val lst2 = Response.ErrorListener { error -> println(error.message) }
        return SoundTrackDownloaderManager(context.get(), fileStorageManager, lst1, lst2)
    }

    interface OnSoundTrackRetrievesCallbacks {
        fun onSoundTrackRetrieveSuccess(fileInputStream: FileInputStream)
        fun onSoundTrackRetrieveError(message: String?)
    }
}
