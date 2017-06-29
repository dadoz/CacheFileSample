package com.sample.lmn.davide.cachefilesample.manager

import android.content.Context
import android.util.Base64
import com.sample.lmn.davide.cachefilesample.models.SoundTrack
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.save
import io.realm.Realm
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by davide-syn on 6/26/17.
 */

class FileStorageManager(context: Context?, lst: FileStorageManager.OnCacheEntryRetrievesCallbacks) {
    private val lst: WeakReference<OnCacheEntryRetrievesCallbacks> = WeakReference(lst)
    private val fileDir: File? = context?.filesDir
    init {
        Realm.init(context)
    }
    /**

     * @param key
     * *
     * @param file
     */
    fun put(key: String, file: ByteArray) {
        val encodedKey = generateEncodedKey(key)
        saveOnDb(encodedKey)
        saveFile(encodedKey, file)
    }

    private fun saveOnDb(encodedKey: String) {
        val soundTrack = SoundTrack()
        soundTrack.key = encodedKey
        soundTrack.save()
    }

    /**

     * @param key
     * *
     * @return
     */
    operator fun get(key: String): String? {
        val encodedKey = generateEncodedKey(key)
        val soundTrack = SoundTrack().query { query -> query.equalTo("key", encodedKey) }
        return soundTrack.get(0).key
    }

    /**

     * @param key
     * *
     * @return
     */
    private fun generateEncodedKey(key: String): String {
        var encoded = Base64.encodeToString(key.toByteArray(), Base64.DEFAULT)
                .replace("=", "").toLowerCase()
        if (encoded.length >= 64)
            encoded = encoded.substring(0, 63)
        return encoded
    }

    /***

     * @param key
     */
    private fun saveFile(key: String?, downloadedFile: ByteArray) {
        Thread(Runnable {
            try {
                val file = File(fileDir, key)
                val stream = FileOutputStream(file)
                stream.write(downloadedFile)
                stream.close()
                lst.get()?.onCacheEntryRetrieved(FileInputStream(file))
            } catch (e: IOException) {
                e.printStackTrace()
                lst.get()?.onCacheEntryRetrieveError(e.message)
            }
        }).start()
    }

    interface OnCacheEntryRetrievesCallbacks {
        fun onCacheEntryRetrieved(fileInputStream: FileInputStream)
        fun onCacheEntryRetrieveError(message: String?)
    }

    fun retrieveFile(key: String): ByteArray? {
        try {
            val fileInputStream = FileInputStream("$fileDir/$key")
            return fileInputStream.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getFullPath(key: String): Any? {
        val parsedKey = generateEncodedKey(key)
        return "$fileDir/$parsedKey"
    }

}
