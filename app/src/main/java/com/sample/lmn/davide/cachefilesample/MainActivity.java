package com.sample.lmn.davide.cachefilesample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sample.lmn.davide.cachefilesample.components.DaggerDownloadManagerComponent;
import com.sample.lmn.davide.cachefilesample.manager.CacheManager;
import com.sample.lmn.davide.cachefilesample.manager.DownloadSoundtrackManager;
import com.sample.lmn.davide.cachefilesample.modules.DownloadManagerModule;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CacheManager.OnCacheEntryRetrievesCallbacks,
        Response.Listener, Response.ErrorListener {
    private static final String FILENAME_SAMPLE = "mozart_sample.mp3";
    private static final String REMOTE_FILE = "http://www.amclassical.com/mp3/amclassical_moonlight_sonata_movement_1.mp3";
    private View putOnCacheButton;
    private View getOnCacheButton;
    private View requestFileButton;

    @Inject
    public CacheManager cacheManager;
    @Inject
    public DownloadSoundtrackManager downloadSoundtrackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //building dagger component
        DaggerDownloadManagerComponent
                .builder()
                .downloadManagerModule(new DownloadManagerModule(getApplicationContext(), this))
                .build()
                .inject(this);

        //init view
        onInitView();
    }

    private void onInitView() {
        putOnCacheButton = findViewById(R.id.putOnCacheButtonId);
        getOnCacheButton = findViewById(R.id.getOnCacheButtonId);
        requestFileButton = findViewById(R.id.requestFileButtonId);
        putOnCacheButton.setOnClickListener(this);
        getOnCacheButton.setOnClickListener(this);
        requestFileButton.setOnClickListener(this);
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.putOnCacheButtonId:
                cacheManager.put(FILENAME_SAMPLE);
                break;
            case R.id.getOnCacheButtonId:
                cacheManager.getAsync(REMOTE_FILE);
                break;
            case R.id.requestFileButtonId:
                downloadSoundtrackManager
                        .getFileFromUrl(Uri.parse(REMOTE_FILE));
                downloadSoundtrackManager.setLst(new WeakReference<Response.Listener<byte[]>>(this));
                downloadSoundtrackManager.setLst2(new WeakReference<Response.ErrorListener>(this));
                break;
        }
    }

    private void handleResult(Object file, Context context) {
        if (file instanceof byte[]) {
            if (((byte[]) file).length != 0) {
                Toast.makeText(context, "file size" + ((byte[]) file).length, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(context, "Empty file", Toast.LENGTH_SHORT).show();
            return;
        }


        if (file != null &&
                file.toString().length() != 0) {
            Toast.makeText(context, file.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(context, "Empty file", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCacheEntryRetrieved(final Object file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleResult(file, getApplicationContext());
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(getClass().getName(), "download error - " + error.getMessage());

    }

    @Override
    public void onResponse(Object response) {
        Toast.makeText(this, "Download with success " + ((byte[]) response).length, Toast.LENGTH_SHORT).show();
        Log.e(getClass().getName(), "download ok- " + ((byte[]) response).length);
    }

}
