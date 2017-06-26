package com.sample.lmn.davide.cachefilesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sample.lmn.davide.cachefilesample.components.DaggerDonwloadManagerComponent;
import com.sample.lmn.davide.cachefilesample.components.DonwloadManagerComponent;
import com.sample.lmn.davide.cachefilesample.manager.CacheManager;
import com.sample.lmn.davide.cachefilesample.modules.DownloadManagerModule;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String FILENAME_SAMPLE = "mozart_sample.mp3";
    private View putOnCacheButton;
    private View getOnCacheButton;

    @Inject
    public CacheManager cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DonwloadManagerComponent component = DaggerDonwloadManagerComponent
                .builder()
                .downloadManagerModule(new DownloadManagerModule(getApplicationContext()))
                .build();
        component.inject(this);
        onInitView();
    }

    private void onInitView() {
        putOnCacheButton = findViewById(R.id.putOnCacheButtonId);
        getOnCacheButton = findViewById(R.id.getOnCacheButtonId);
        putOnCacheButton.setOnClickListener(this);
        getOnCacheButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.putOnCacheButtonId:
                cacheManager.put(FILENAME_SAMPLE, "blalalallalallalla");
                break;
            case R.id.getOnCacheButtonId:
                String file = (String) cacheManager.get(FILENAME_SAMPLE);
                if (file != null &&
                        file.length() != 0) {
                    Toast.makeText(view.getContext(), file, Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(view.getContext(), "Empty file", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
