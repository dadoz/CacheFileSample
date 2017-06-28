package com.sample.lmn.davide.cachefilesample.manager

import android.content.Context
import android.net.Uri
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.Volley
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by davide-syn on 6/26/17.
 */

class DownloadSoundtrackManager(context: Context?, private val fileStorageManager: FileStorageManager) : Response.ErrorListener, Response.Listener<ByteArray> {
    private val context: WeakReference<Context?> = WeakReference(context)
    private var lst: WeakReference<Response.Listener<Any>>? = null
    private var lst2: WeakReference<Response.ErrorListener>? = null
    private var url: Uri? = null

    /**
     * @param url
     */
    fun getFileFromUrl(url: Uri) {
        //replace
        this.url = url
        //add request on queue
        Volley.newRequestQueue(context.get())
                .add(InputStreamVolleyRequest(Request.Method.GET, url, this, this, HashMap()))
    }

    /**
     *
     */
    override fun onErrorResponse(error: VolleyError) {
        lst2?.get()?.onErrorResponse(error)
    }

    /**
     *
     */
    override fun onResponse(response: ByteArray) {
        fileStorageManager.put(url.toString(), String(response))
        lst?.get()?.onResponse(response)
    }

    /**
     *
     */
    fun setLst(lst: WeakReference<Response.Listener<Any>>) {
        this.lst = lst
    }

    /**
     *
     */
    fun setLst2(lst2: WeakReference<Response.ErrorListener>) {
        this.lst2 = lst2
    }

    /**
     *
     */
    internal inner class InputStreamVolleyRequest(method: Int, mUrl: Uri, private val mListener: Response.Listener<ByteArray>,
                                                  errorListener: Response.ErrorListener, params: HashMap<String, String>) :
            Request<ByteArray>(method, mUrl.toString(), errorListener) {
        private val mParams: Map<String, String>

        init {
            setShouldCache(false)
            mParams = params
        }

        @Throws(com.android.volley.AuthFailureError::class)
        override fun getParams(): Map<String, String> {
            return mParams
        }

        override fun deliverResponse(response: ByteArray) {
            mListener.onResponse(response)
        }

        override fun parseNetworkResponse(response: NetworkResponse): Response<ByteArray> {
            //Pass the response data here
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
        }
    }
}
