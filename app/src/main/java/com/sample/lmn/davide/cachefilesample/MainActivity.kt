package com.sample.lmn.davide.cachefilesample

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.sample.lmn.davide.cachefilesample.components.DaggerDownloadManagerComponent
import com.sample.lmn.davide.cachefilesample.components.DownloadManagerComponent
import com.sample.lmn.davide.cachefilesample.manager.DownloadSoundtrackManager
import com.sample.lmn.davide.cachefilesample.manager.FileStorageManager
import com.sample.lmn.davide.cachefilesample.modules.DownloadManagerModule
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import javax.inject.Inject

open class MainActivity : AppCompatActivity(), FileStorageManager.OnCacheEntryRetrievesCallbacks, Response.Listener<Any>, Response.ErrorListener {

    lateinit var mediaPlayer: MediaPlayer

    @Inject
    lateinit var fileStorageManager: FileStorageManager
    @Inject
    lateinit var downloadSoundtrackManager: DownloadSoundtrackManager

    val component: DownloadManagerComponent by lazy {
        //building dagger component
        DaggerDownloadManagerComponent
                .builder()
                .downloadManagerModule(DownloadManagerModule(applicationContext, this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        onInitView()
    }

    private fun onInitView() {
        playButtonId.setOnClickListener {
            val soundTrackUrl = soundTrackUrlEditTextId.text.toString()
            //make coroutine
            downloadSoundtrackManager.getFileFromUrl(Uri.parse(soundTrackUrl))
            downloadSoundtrackManager.setLst(this)
            downloadSoundtrackManager.setLst2(this)
        }
        initMediaPlayer()
    }

    /**

     */
    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    /**

     */
    private fun playCachedFile(inputStream: FileInputStream) {
        try {
            showSuccess("eureka");
            mediaPlayer.reset()
            mediaPlayer.setDataSource(inputStream.fd)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
            showError(e.message?: "default")
        }

    }

    private fun showError(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }

    private fun handleResult(file: FileInputStream?, context: Context) {
        showSuccess(file.toString().length.toString())
    }

    override fun onCacheEntryRetrieved(fileInputStream: FileInputStream) {
        runOnUiThread {
            playCachedFile(fileInputStream)
            handleResult(fileInputStream, applicationContext)
        }
    }

    override fun onCacheEntryRetrieveError(message: String?) {
        showError(message?: "Error")
    }

    override fun onErrorResponse(error: VolleyError) {
        Log.e(javaClass.name, "download error - " + error.message)

    }

    override fun onResponse(response: Any) {
        onCacheEntryRetrieved(FileInputStream(response as String))
//        showSuccess((response as ByteArray).size.toString())
//        Log.e(javaClass.name, "download ok- " + response.size)
    }

    fun showSuccess(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private val FILENAME_SAMPLE = "mozart_sample.mp3"
        private val REMOTE_FILE = "http://www.amclassical.com/mp3/amclassical_moonlight_sonata_movement_1.mp3"
    }

}
