package com.sample.lmn.davide.cachefilesample.manager;

import android.content.Context;
import android.net.Uri;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davide-syn on 6/26/17.
 */

public class DownloadSoundtrackManager {
    private final WeakReference<Context> context;
    private WeakReference<Response.Listener<byte[]>> lst;
    private WeakReference<Response.ErrorListener> lst2;

    public DownloadSoundtrackManager(Context context,
                                     Response.Listener<byte[]> lst,
                                     Response.ErrorListener lst2) {
        this.context = new WeakReference<>(context);
        // Instantiate the RequestQueue.
        this.lst = new WeakReference<Response.Listener<byte[]>>(lst);
        this.lst2 = new WeakReference<Response.ErrorListener>(lst2);
    }

    /**
     *
     * @param url
     */
    public void getFileFromUrl(Uri url) {
        //input stream
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        if (lst != null)
                            lst.get().onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (lst2 != null)
                            lst2.get().onErrorResponse(error);
                    }
                }
                , null);

        //add request on queui
        Volley.newRequestQueue(context.get()).add(request);
    }

    /**
     *
     */
    class InputStreamVolleyRequest extends Request<byte[]> {
        private final Response.Listener<byte[]> mListener;
        private Map<String, String> mParams;

        //create a static map for directly accessing headers
        public Map<String, String> responseHeaders;

        public InputStreamVolleyRequest(int method, Uri mUrl , Response.Listener<byte[]> listener,
                                        Response.ErrorListener errorListener, HashMap<String, String> params) {
            super(method, mUrl.toString(), errorListener);
            setShouldCache(false);
            mListener = listener;
            mParams=params;
        }

        @Override
        protected Map<String, String> getParams()
                throws com.android.volley.AuthFailureError {
            return mParams;
        }

        @Override
        protected void deliverResponse(byte[] response) {
            mListener.onResponse(response);
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

            //Initialise local responseHeaders map with response headers received
            responseHeaders = response.headers;

            //Pass the response data here
            return Response.success( response.data, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

}
