package com.lib.lmn.davide.soundtrackdownloaderlibrary

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.FileStorageManager
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileInputStream

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class FileStorageManagerTest {
    lateinit var fileStorageManager: FileStorageManager
    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getTargetContext()
        fileStorageManager = FileStorageManager(appContext, object: SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks {
            override fun onSoundTrackRetrieveSuccess(filePath: String, fileInputStream: FileInputStream) {
            }
            override fun onSoundTrackRetrieveError(message: String?) {
            }
        })
    }

    @Test
    @Throws(Exception::class)
    fun cachedFile() {
        // Context of the app under test.
        Assert.assertNotNull(fileStorageManager.getCachedFile("wwww.bla.com"))
    }

    @Test
    @Throws(Exception::class)
    fun putFile() {
        val key: String = "wwww.bla.com"
        fileStorageManager.putCachedFile(key, "sample".toByteArray())

        // Context of the app under test.
        Assert.assertNotNull(fileStorageManager.getCachedFile(key))
    }

    @Test
    @Throws(Exception::class)
    fun generateEncodedKey() {
        Assert.assertNotNull(FileStorageManager.generateEncodedKey("www.bla.com"))
    }
}
