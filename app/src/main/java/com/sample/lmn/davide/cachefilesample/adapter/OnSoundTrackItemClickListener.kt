package com.sample.lmn.davide.cachefilesample.adapter

import com.google.api.services.youtube.model.SearchResult

/**
 * Created by davide-syn on 7/21/17.
 */
interface OnSoundTrackItemClickListener {
    fun onItemClick(item: SearchResult)
}