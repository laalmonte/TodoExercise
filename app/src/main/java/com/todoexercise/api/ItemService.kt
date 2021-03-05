package com.todoexercise.api

import com.todoexercise.model.Results
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemService {

    @GET("")
    suspend fun getItemsFromApi(): Response<List<Results>>

}