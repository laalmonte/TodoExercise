package com.todoexercise.model

import androidx.annotation.Keep
import java.sql.Timestamp

//@Keep
data class Item(
    var docId: String?,
    var title: String?,
    var iconUrl: String?,
    var description: String?,
    var createdAt: String?,
)