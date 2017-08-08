package com.sample.lmn.davide.cachefilesample.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.sample.lmn.davide.cachefilesample.R

/**
 * Created by davide-syn on 7/21/17.
 */
class CustomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val title: TextView? = itemView?.findViewById(R.id.title) as TextView
}
