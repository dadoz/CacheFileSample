package com.lib.lmn.davide.soundtrackdownloaderlibrary.manager

import android.content.Context
import com.lib.lmn.davide.soundtrackdownloaderlibrary.models.SoundTrack
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
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

class FileStorageManager(context: Context?, lst: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks?) {
    val lst: WeakReference<SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks?> = WeakReference(lst)
    val fileDir: File? = context?.filesDir
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
    operator fun get(name: String): String? {
        val key = generateEncodedKey(name)
        val soundTrack = SoundTrack().query { query -> query.equalTo("key", key) }

        if (soundTrack.isEmpty())
            return null
        return soundTrack[0].key
    }

    /**

     * @param key
     * *
     * @return
     */
    private fun generateEncodedKey(key: String): String {
//        var encoded = Base64.encodeToString(key.toByteArray(), Base64.DEFAULT)
//                .replace("=", "").toLowerCase()
        var encoded = key
                .replace("http:\\\\", "")
                .replace("/", "")
                .replace("=", "")
                .toLowerCase()
        if (encoded.length >= 64)
            encoded = encoded.substring(0, 63)
        return encoded
    }

    //new algorithm to handle file name
//    StringBuilder sb = new StringBuilder(len);
//    for (int i = 0; i < len; i++) {
//        char ch = s.charAt(i);
//        if (ch < ' ' || ch >= 0x7F || ch == fileSep || ... // add other illegal chars
//                || (ch == '.' && i == 0) // we don't want to collide with "." or ".."!
//                || ch == escape) {
//            sb.append(escape);
//            if (ch < 0x10) {
//                sb.append('0');
//            }
//            sb.append(Integer.toHexString(ch));
//        } else {
//            sb.append(ch);
//        }
//    }

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
                lst.get()?.onSoundTrackRetrieveSuccess(FileInputStream(file))
            } catch (e: IOException) {
                e.printStackTrace()
                lst.get()?.onSoundTrackRetrieveError(e.message)
            }
        }).start()
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

    /**
     * get full path
     */
    fun getFullPath(name: String): Any? {
        val key = generateEncodedKey(name)
        return "$fileDir/$key"
    }

}
