package com.sample.lmn.davide.cachefilesample.modules;

import android.content.Context;

import com.sample.lmn.davide.cachefilesample.manager.CacheManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davide-syn on 6/26/17.
 */
@Module
public class DownloadManagerModule {
    private final Context context;

    public DownloadManagerModule(Context context) {
        this.context = context;
    }

    @Provides
    public CacheManager provideCacheManager() {
        return new CacheManager(context);
    }
}
