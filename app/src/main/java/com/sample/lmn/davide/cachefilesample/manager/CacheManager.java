package com.sample.lmn.davide.cachefilesample.manager;

import android.content.Context;
import android.content.res.AssetManager;

import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.JsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Created by davide-syn on 6/26/17.
 */

public class CacheManager {
    private static final String CACHE_NAME = "file_cache";
    private static final int TEST_APP_VERSION = 1;
    private final DualCache<Object> cache;
    private final AssetManager assetManager;
    private final int DISK_MAX_SIZE = 10000;

    public CacheManager(Context context) {
        this.assetManager = context.getAssets();
        cache = new Builder<>(CACHE_NAME, TEST_APP_VERSION)
                .enableLog()
                .noRam()
//                .useReferenceInRam(RAM_MAX_SIZE, new SizeOfVehiculeForTesting())
                .useSerializerInDisk(DISK_MAX_SIZE, true, new JsonSerializer<>(Object.class), context)
                .build();
    }

    /**
     *
     * @param key
     */
    public void putFile(String key) {
        String filename = key;
        if (!isValidKey(key))
            key = parsedKey(key);
        cache.put(key, readFile(filename));
    }


    /**
     *
     * @param key
     * @param file
     */
    public void put(String key, Object file) {
        if (!isValidKey(key))
            key = parsedKey(key);
        cache.put(key, file);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        if (!isValidKey(key))
            key = parsedKey(key);
        return cache.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    private String parsedKey(String key) {
        if (key == null)
            return null;
        return key.replace(".", "");
    }

    /**
     *
     * @param key
     * @return
     */
    public static boolean isValidKey(String key) {
        if (key == null)
            return false;
        Pattern p = Pattern.compile("[a-z0-9_-]{1,64}");
        return p.matcher(key).matches();
    }


    /**
     *
     * @param inFile
     * @return
     */
    public byte[] readFile(String inFile) {

        try {
            InputStream stream = assetManager.open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
