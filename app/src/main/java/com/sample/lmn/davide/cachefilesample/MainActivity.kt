package com.sample.lmn.davide.cachefilesample

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast

import com.android.volley.Response
import com.android.volley.VolleyError
import com.sample.lmn.davide.cachefilesample.components.DaggerDownloadManagerComponent
import com.sample.lmn.davide.cachefilesample.manager.FileStorageManager
import com.sample.lmn.davide.cachefilesample.manager.DownloadSoundtrackManager
import com.sample.lmn.davide.cachefilesample.modules.DownloadManagerModule

import java.io.FileInputStream
import java.lang.ref.WeakReference

import javax.inject.Inject

class MainActivity : AppCompatActivity(), View.OnClickListener, FileStorageManager.OnCacheEntryRetrievesCallbacks, Response.Listener<*>, Response.ErrorListener {
    private var putOnCacheButton: View? = null
    private var getOnCacheButton: View? = null
    private var requestFileButton: View? = null

    @Inject
    var fileStorageManager: FileStorageManager? = null
    @Inject
    var downloadSoundtrackManager: DownloadSoundtrackManager? = null
    private var mediaPlayer: MediaPlayer? = null
    private var playButton: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //building dagger component
        DaggerDownloadManagerComponent
                .builder()
                .downloadManagerModule(DownloadManagerModule(applicationContext, this))
                .build()
                .inject(this)

        //init view
        onInitView()
    }

    private fun onInitView() {
        putOnCacheButton = findViewById(R.id.putOnCacheButtonId)
        getOnCacheButton = findViewById(R.id.getOnCacheButtonId)
        requestFileButton = findViewById(R.id.requestFileButtonId)
        playButton = findViewById(R.id.playButtonId)
        putOnCacheButton!!.setOnClickListener(this)
        getOnCacheButton!!.setOnClickListener(this)
        requestFileButton!!.setOnClickListener(this)
        playButton!!.setOnClickListener(this)

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
            mediaPlayer!!.setDataSource(inputStream.fd)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
            showError(e.message)
        }

    }

    private fun showError(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }


    override fun onClick(view: View) {
        when (view.id) {
        //            case R.id.playButtonId:
        //                playCachedFile();
        //                break;
            R.id.putOnCacheButtonId -> fileStorageManager!!.put(FILENAME_SAMPLE)
            R.id.playButtonId -> fileStorageManager!!.getAsync(REMOTE_FILE)
            R.id.requestFileButtonId -> {
                downloadSoundtrackManager!!.getFileFromUrl(Uri.parse(REMOTE_FILE))
                downloadSoundtrackManager!!.setLst(WeakReference<Response.Listener<ByteArray>>(this))
                downloadSoundtrackManager!!.setLst2(WeakReference<Response.ErrorListener>(this))
            }
        }
    }

    private fun handleResult(file: FileInputStream?, context: Context) {
        if (file != null && file.toString().length != 0) {
            Toast.makeText(context, file.toString(), Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "Empty file", Toast.LENGTH_SHORT).show()

    }

    override fun onCacheEntryRetrieved(fileInputStream: FileInputStream) {
        runOnUiThread {
            playCachedFile(fileInputStream)
            handleResult(fileInputStream, applicationContext)
        }
    }

    override fun onErrorResponse(error: VolleyError) {
        Log.e(javaClass.name, "download error - " + error.message)

    }

    override fun onResponse(response: Any) {
        Toast.makeText(this, "Download with success " + (response as ByteArray).size, Toast.LENGTH_SHORT).show()
        Log.e(javaClass.name, "download ok- " + response.size)
    }

    companion object {
        private val FILENAME_SAMPLE = "mozart_sample.mp3"
        private val REMOTE_FILE = "http://www.amclassical.com/mp3/amclassical_moonlight_sonata_movement_1.mp3"
    }

}
