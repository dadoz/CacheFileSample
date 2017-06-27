package com.sample.lmn.davide.cachefilesample.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.JsonSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * Created by davide-syn on 6/26/17.
 */

public class CacheManager {
    private static final String CACHE_NAME = "file_cache";
    private static final int TEST_APP_VERSION = 1;
    private final DualCache<Object> cache;
    private final AssetManager assetManager;
    private static final int DISK_MAX_SIZE = 28967680;
    private final WeakReference<OnCacheEntryRetrievesCallbacks> lst;

    public CacheManager(Context context, OnCacheEntryRetrievesCallbacks lst) {
        this.assetManager = context.getAssets();
        this.lst = new WeakReference<>(lst);
        cache = new Builder<>(CACHE_NAME, TEST_APP_VERSION)
                .enableLog()
                .noRam()
//                .useReferenceInRam(RAM_MAX_SIZE, new SizeOfVehiculeForTesting())
                .useSerializerInDisk(DISK_MAX_SIZE, true, new BinaryFileSerializer(), context)
                .build();
    }

    /**
     *
     * @param key
     */
    public void put(String key) {
        put(key, readFile(key));
    }

    /**
     *
     * @param key
     * @param file
     */
    public void put(String key, Object file) {
        request("PUT", key, file);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return request("GET", key, null);
    }

    /**
     * TODO make this on decorator ??
     * @param type
     * @param key
     * @param file
     * @return
     */
    private Object request(String type, String key, Object file) {
        //encoding key
        String encodedKey = generateEncodedKey(key);
        //make request
        if (type.equals("GET")) {
            return cache.get(encodedKey);
        }
        if (type.equals("PUT")) {
            cache.put(encodedKey, file);
        }
        return null;
    }

    /**
     *
     * @param key
     * @return
     */
    private String generateEncodedKey(String key) {
        if (key != null) {
            String encoded = Base64.encodeToString(key.getBytes(), Base64.DEFAULT).replace("=", "").toLowerCase();
            if (encoded.length() >= 64)
                encoded = encoded.substring(0, 63);
            return encoded;
        }
        return null;
    }

    public void getAsync(final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lst.get() != null)
                    lst.get().onCacheEntryRetrieved(get(key));
            }
        }).start();
    }

    /**
     *
     * @param inFile
     * @return
     */
    public String readFile(String inFile) {
        try {
            InputStream stream = assetManager.open(inFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder out = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            String result = out.toString();
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * public interface
     */
    public interface OnCacheEntryRetrievesCallbacks {
        void onCacheEntryRetrieved(Object file);
    }

    /**
     * custome deserializer
     */
    private class BinaryFileSerializer extends JsonSerializer<Object> {

        BinaryFileSerializer() {
            super(Object.class);
        }

        @Override
        public String toString(Object data) {
            Log.e(getClass().getName(), data.toString().length() + "");
            return data.toString();
        }
        @Override
        public Object fromString(String data) {
            try {

                Log.e(getClass().getName(), data.length() + "");
                return data.getBytes();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
