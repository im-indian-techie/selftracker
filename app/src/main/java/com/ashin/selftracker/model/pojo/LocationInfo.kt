package com.ashin.selftracker.model.pojo

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

 class LocationInfo: RealmObject {
   var longitude:Double=0.0
   var latitude:Double=0.0
   var timestamp:Long=0
}