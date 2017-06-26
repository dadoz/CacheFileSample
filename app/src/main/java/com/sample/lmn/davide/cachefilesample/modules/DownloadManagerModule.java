package com.sample.lmn.davide.cachefilesample.modules;

import android.content.Context;

import com.sample.lmn.davide.cachefilesample.manager.CacheManager;
import com.sample.lmn.davide.cachefilesample.manager.DownloadSoundtrackManager;

import java.lang.ref.WeakReference;

import dagger.Module;
import dagger.Provides;

import static com.sample.lmn.davide.cachefilesample.manager.CacheManager.*;

/**
 * Created by davide-syn on 6/26/17.
 */
@Module
public class DownloadManagerModule {
    private final WeakReference<Context> context;
    private final OnCacheEntryRetrievesCallbacks lst;

    public DownloadManagerModule(Context context, OnCacheEntryRetrievesCallbacks lst)  {
        this.context = new WeakReference<>(context);
        this.lst = lst;
    }

    @Provides
    public CacheManager provideCacheManager() {
        return new CacheManager(context.get(), lst);
    }

    @Provides
    public DownloadSoundtrackManager downloadSoundtrackManager() {
        return new DownloadSoundtrackManager(context.get());
    }
}
