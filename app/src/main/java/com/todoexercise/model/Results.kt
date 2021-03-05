package com.todoexercise.model

data class Results(
    val doc_id: String,
    val title: String,
    val description: String,
    val icon_url: String,
    val created_at: String,
){
    fun toItem() : Item {
        return Item(
            doc_id,
            title,
            icon_url,
            description,
            created_at ,
        )
    }
}