package com.sample.lmn.davide.cachefilesample

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.sample.lmn.davide.cachefilesample.components.DaggerYoutubeDownloaderComponent
import com.sample.lmn.davide.cachefilesample.components.YoutubeDownloaderComponent
import com.sample.lmn.davide.cachefilesample.manager.YoutubeDownloaderManager
import com.sample.lmn.davide.cachefilesample.modules.SoundTrackDownloaderModule
import com.sample.lmn.davide.cachefilesample.modules.YoutubeDownloaderModule
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import javax.inject.Inject

open class MainActivity : AppCompatActivity(),
        SoundTrackDownloaderModule.OnSoundTrackRetrievesCallbacks {
    lateinit var mediaPlayer: MediaPlayer

    @Inject
    lateinit var youtubeDownloaderManager: YoutubeDownloaderManager

    val component: YoutubeDownloaderComponent by lazy {
        DaggerYoutubeDownloaderComponent
                .builder()
                .youtubeDownloaderModule(YoutubeDownloaderModule(applicationContext, this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        onInitView()
    }

    private fun onInitView() {
        clearButtonId.setOnClickListener { soundTrackUrlEditTextId.setText("") }
        playButtonId.setOnClickListener {
            //TODO make coroutine
            val videoId = soundTrackUrlEditTextId.text.toString()
//            downloadSoundtrackManager.getSoundTrack(Uri.parse(soundTrackUrl))
            youtubeDownloaderManager.fetchSoundTrackUrlByVideoId(videoId)
        }
        initMediaPlayer()
    }

    /**

     */
    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
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

    override fun onSoundTrackRetrieveSuccess(fileInputStream: FileInputStream) {
        runOnUiThread {
            playCachedFile(fileInputStream)
            showSuccess("sound track successfully downloaded")
        }
    }

    override fun onSoundTrackRetrieveError(message: String?) {
        showError("error" + message)
    }

    fun showSuccess(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }

    fun showError(message: String) {
        Snackbar.make(findViewById(R.id.mainActivityLayoutId), message, Snackbar.LENGTH_LONG).show()
    }


    companion object {
        private val FILENAME_SAMPLE = "mozart_sample.mp3"
        private val REMOTE_FILE = "http://www.amclassical.com/mp3/amclassical_moonlight_sonata_movement_1.mp3"
    }

}
