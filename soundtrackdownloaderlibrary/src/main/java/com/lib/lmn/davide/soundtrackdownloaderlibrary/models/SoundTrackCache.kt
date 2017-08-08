package com.lib.lmn.davide.soundtrackdownloaderlibrary.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by davide-syn on 6/29/17.
 */
open class SoundTrackCache : RealmObject() {
    @PrimaryKey
    lateinit var key: String

    var name: String? = null
}