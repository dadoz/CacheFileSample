package com.sample.lmn.davide.cachefilesample.manager

import android.content.Context
import android.util.Base64

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by davide-syn on 6/26/17.
 */

class FileStorageManager(context: Context?, lst: FileStorageManager.OnCacheEntryRetrievesCallbacks) {
    private val lst: WeakReference<OnCacheEntryRetrievesCallbacks> = WeakReference(lst)
    private val fileDir: File? = context?.filesDir

    /**

     * @param key
     */
    fun put(key: String) {
        saveFile(key)
    }

    /**

     * @param key
     * *
     * @param file
     */
    fun put(key: String, file: Any) {
        val encodedKey = generateEncodedKey(key)
    }

    /**

     * @param key
     * *
     * @return
     */
    operator fun get(key: String): String? {
        return generateEncodedKey(key)
    }

    /**

     * @param key
     * *
     * @return
     */
    private fun generateEncodedKey(key: String?): String? {
        var encoded = Base64.encodeToString(key?.toByteArray(), Base64.DEFAULT).replace("=", "").toLowerCase()
        if (encoded.length >= 64)
            encoded = encoded.substring(0, 63)
        return encoded
    }

    //    public void getAsync(final String key) {
    //        new Thread(new Runnable() {
    //            @Override
    //            public void run() {
    //                File file = new File(fileDir, "test1");
    //                FileInputStream fileInputStream = null;
    //                try {
    //                    FileOutputStream stream = new FileOutputStream(file);
    //                    stream.write(null);
    //                    stream.close();
    //                    fileInputStream = new FileInputStream(file);
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //
    //                if (lst.get() != null)
    //                    lst.get().onCacheEntryRetrieved(fileInputStream);
    //            }
    //        }).start();
    //    }

    /***

     * @param key
     */
    private fun saveFile(key: String) {
        Thread(Runnable {
            try {
                val file = File(fileDir, key)
                val stream = FileOutputStream(file)
                stream.write(0)
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //                if (lst.get() != null)
            //                    lst.get().onCacheEntryRetrieved(fileInputStream);
        }).start()
    }

    //    /**
    //     *
    //     * @param inFile
    //     * @return
    //     */
    //    public String readFile(String inFile) {
    //        try {
    //            InputStream stream = assetManager.open(inFile);
    //            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    //
    //            StringBuilder out = new StringBuilder();
    //
    //            String line;
    //            while ((line = reader.readLine()) != null) {
    //                out.append(line);
    //            }
    //            String result = out.toString();
    //            reader.close();
    //            return result;
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }

    interface OnCacheEntryRetrievesCallbacks {
        fun onCacheEntryRetrieved(fileInputStream: FileInputStream)
    }
}
