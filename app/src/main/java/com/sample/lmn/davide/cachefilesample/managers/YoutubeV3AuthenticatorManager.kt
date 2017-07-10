package com.sample.lmn.davide.cachefilesample.managers

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.sample.lmn.davide.cachefilesample.BuildConfig
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by davide-syn on 7/10/17.
 */
class YoutubeV3AuthenticatorManager(applicationName: String, val lst: WeakReference<YoutubeOnSearchByQueryResults>) {
    val properties = Properties()
    val youtube: YouTube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpRequestInitializer { })
            .setApplicationName(applicationName).build()
    val search: YouTube.Search.List = youtube.search().list("id,snippet")

    init {
        try {
            initSearch()
        } catch (e : IOException) {
            lst.get()?.youtubeSearchResultError(e.message?: "generic error", null)
            e.printStackTrace()
        } catch (e : Throwable) {
            lst.get()?.youtubeSearchResultError(e.message?: "generic error", null)
            e.printStackTrace()
        } catch (e : GoogleJsonResponseException) {
            lst.get()?.youtubeSearchResultError(e.message?: "generic error", null)
            e.printStackTrace()
        }
    }

    /**
     * init search
     */
    @Throws(GoogleJsonResponseException::class, IOException::class, Throwable::class)
    private fun initSearch() {
        // Define the API request for retrieving search results.
        search.key = BuildConfig.YOUTUBE_API_KEY
        search.type = "video"
        search.fields = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)"
        search.maxResults = NUMBER_OF_VIDEOS_RETURNED
    }

    /**
     * optional - init property file
     */
    fun initPropertyFile() {
        // Read the developer key from the properties file.
//        properties.load(YouTube.Search::class.java.getResourceAsStream("/" + PROPERTIES_FILENAME))
//        properties.getProperty(BuildConfig.YOUTUBE_API_KEY)
    }

    /**
     * find search result
     */
    fun searchByQuery(queryTerm: String) {
        //set query terms
        search.q = queryTerm

        // Call the API and print results.
        doAsync {
            val list = search.execute().items
            onComplete {
                lst.get()?.youtubeSearchResultSuccess(list, queryTerm)
            }
        }
    }


    companion object {
        private val PROPERTIES_FILENAME = "youtube.properties"
        private val NUMBER_OF_VIDEOS_RETURNED: Long = 25

        /**
         * Define a global instance of the HTTP transport.
         */
        val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()

        /**
         * Define a global instance of the JSON factory.
         */
        val JSON_FACTORY: JsonFactory = JacksonFactory()
    }
}

interface YoutubeOnSearchByQueryResults {
    fun youtubeSearchResultSuccess(lst: List<SearchResult>,  query: String?)
    fun youtubeSearchResultError(message: String,  query: String?)
}
