package com.sample.lmn.davide.cachefilesample

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.api.services.youtube.model.SearchResult
import com.lib.lmn.davide.soundtrackdownloaderlibrary.manager.SoundTrackDownloaderManager
import com.lib.lmn.davide.soundtrackdownloaderlibrary.modules.SoundTrackDownloaderModule
import com.sample.lmn.davide.cachefilesample.adapter.OnSoundTrackItemClickListener
import com.sample.lmn.davide.cachefilesample.adapter.SoundTrackRvAdapter
import com.sample.lmn.davide.cachefilesample.managers.YoutubeOnSearchByQueryResults
import com.sample.lmn.davide.cachefilesample.managers.YoutubeV3AuthenticatorManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.lang.ref.WeakReference

open class MainActivity : AppCompatActivity() , SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks,
        YoutubeOnSearchByQueryResults {

    lateinit var mediaPlayer: MediaPlayer

    val soundTrackDownloaderManager: SoundTrackDownloaderManager by lazy {
        SoundTrackDownloaderManager.getInstance(this, this)
    }

    val youtubeDownloadManager: YoutubeV3AuthenticatorManager by lazy {
        YoutubeV3AuthenticatorManager("bla", WeakReference(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init view
        recyclerViewId.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewId.adapter = SoundTrackRvAdapter(ArrayList(), listener = object: OnSoundTrackItemClickListener {
            override fun onItemClick(item: SearchResult) {
                soundTrackDownloaderManager.downloadAndPlaySoundTrack(item.id.videoId)
            }
        })
        onInitView()
    }

    /**
     *
     */
    private fun onInitView() {
        clearButtonId.setOnClickListener { soundTrackUrlEditTextId.setText("") }
        playButtonId.setOnClickListener {
            youtubeDownloadManager.searchByQuery(soundTrackUrlEditTextId.text.toString())
        }

        initMediaPlayer()
    }

    /**
     *
     */
    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    /**
     *
     */
    private fun playCachedFile(inputStream: FileInputStream) {
        try {
            showSuccess("eureka")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(inputStream.fd)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
            showError(e.message?: "default")
        }
    }

    /**
     *
     */
    override fun onSoundTrackRetrieveSuccess(fileInputStream: FileInputStream) {
        runOnUiThread {
            playCachedFile(fileInputStream)
            showSuccess("sound track successfully downloaded")
        }
    }

    /**
     *
     */
    override fun onSoundTrackRetrieveError(message: String?) {
        showError("error" + message)
    }

    /**
     *
     */
    override fun youtubeSearchResultSuccess(list: List<SearchResult>, query: String?) {
        showSuccess("$query --- ${list.size}")
        (recyclerViewId.adapter as SoundTrackRvAdapter).list = list
        recyclerViewId.adapter.notifyDataSetChanged()
    }

    /**
     *
     */
    override fun youtubeSearchResultError(message: String, query: String?) {
        showError("error" + message)
    }

    /**
     *
     */
    fun showSuccess(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }

    /**
     *
     */
    fun showError(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }

    /**
     *
     */
    companion object {
        private val FILENAME_SAMPLE = "mozart_sample.mp3"
        private val REMOTE_FILE = "http://www.amclassical.com/mp3/amclassical_moonlight_sonata_movement_1.mp3"
    }

}
