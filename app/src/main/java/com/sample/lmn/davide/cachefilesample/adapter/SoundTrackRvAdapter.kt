package com.sample.lmn.davide.cachefilesample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.api.services.youtube.model.SearchResult
import com.sample.lmn.davide.cachefilesample.R

/**
 * Created by davide-syn on 7/10/17.
 */

class SoundTrackRvAdapter(var list: List<SearchResult>, val listener: OnSoundTrackItemClickListener) : RecyclerView.Adapter<CustomViewHolder>() {
    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        holder?.title?.text = list[position].snippet.title
        holder?.itemView?.setOnClickListener { listener.onItemClick(list[position]) }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.abc_list_menu_item_layout, null)
        return CustomViewHolder(view)
    }
}