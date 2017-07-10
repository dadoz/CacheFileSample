package com.lib.lmn.davide.soundtrackdownloaderlibrary.modules

import android.content.Context
import com.android.volley.Response
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileDownloaderManager
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager
import java.io.FileInputStream

/**
 * Created by davide-syn on 6/26/17.
 */
open class SoundTrackDownloaderModule(val context: Context?, lst: OnSoundTrackRetrievesCallbacks?) {
    val fileStorageManager: FileStorageManager = FileStorageManager(context, lst)

    fun getFileDownloaderManager(): FileDownloaderManager {
        val lst1 = Response.Listener<Any> { result -> println(result.toString().length) }
        val lst2 = Response.ErrorListener { error -> println(error.message) }
        return FileDownloaderManager(context, fileStorageManager, lst1, lst2)
    }

    interface OnSoundTrackRetrievesCallbacks {
        fun onSoundTrackRetrieveSuccess(fileInputStream: FileInputStream)
        fun onSoundTrackRetrieveError(message: String?)
    }
}
