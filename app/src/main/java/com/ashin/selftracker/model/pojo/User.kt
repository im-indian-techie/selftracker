package com.ashin.selftracker.model.pojo

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


 class User: RealmObject {
    @PrimaryKey
    var email:String=""
    var password:String=""
    var locations: RealmList<LocationInfo> = realmListOf()
}
