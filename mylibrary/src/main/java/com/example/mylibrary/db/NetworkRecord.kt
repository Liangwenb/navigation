package com.example.mylibrary.db

import androidx.room.Entity


@Entity
data class NetworkRecord(
    val headers: String,
    val method:String,
    val requestTime:Long,
    val timeConsume:Long,
    val requestBody:String,
    val responseBody:String

)






