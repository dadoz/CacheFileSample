package com.sample.lmn.davide.cachefilesample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sample.lmn.davide.cachefilesample.components.DaggerDonwloadManagerComponent;
import com.sample.lmn.davide.cachefilesample.manager.CacheManager;
import com.sample.lmn.davide.cachefilesample.modules.DownloadManagerModule;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CacheManager.OnCacheEntryRetrievesCallbacks {
    private static final String FILENAME_SAMPLE = "mozart_sample.mp3";
    private View putOnCacheButton;
    private View getOnCacheButton;

    @Inject
    public CacheManager cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //building dagger component
        DaggerDonwloadManagerComponent
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
        putOnCacheButton.setOnClickListener(this);
        getOnCacheButton.setOnClickListener(this);
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.putOnCacheButtonId:
                cacheManager.put(FILENAME_SAMPLE);
                break;
            case R.id.getOnCacheButtonId:
                cacheManager.getAsync(FILENAME_SAMPLE);
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
}
