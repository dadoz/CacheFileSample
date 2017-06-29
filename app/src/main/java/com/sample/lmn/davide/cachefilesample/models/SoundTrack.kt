package com.sample.lmn.davide.cachefilesample.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by davide-syn on 6/29/17.
 */
open class SoundTrack: RealmObject() {
    @PrimaryKey
    lateinit var key: String

    var name: String? = null
}