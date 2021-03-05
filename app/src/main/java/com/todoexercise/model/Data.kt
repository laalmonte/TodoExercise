package com.todoexercise.model

data class Data<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Data<T> { return Data(Status.SUCCESS, data, null) }

        fun <T> error(msg: String, data: T?): Data<T> { return Data(Status.ERROR, data, msg) }

        fun <T> loading(data: T?): Data<T> { return Data(Status.LOADING, data, null) }
    }

}

enum class Status { SUCCESS, ERROR, LOADING }