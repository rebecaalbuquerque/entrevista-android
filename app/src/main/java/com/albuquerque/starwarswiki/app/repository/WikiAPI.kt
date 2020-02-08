package com.albuquerque.starwarswiki.app.repository

import com.albuquerque.starwarswiki.app.model.dto.ResponseFavorite
import com.albuquerque.starwarswiki.app.model.dto.ResponsePeople
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WikiAPI {

    @GET("people/")
    suspend fun fetchPeople(): ResponsePeople

    @POST("favorite/abc123")
    suspend fun favorite(
        @Header("Prefer") value: String = ""
    ): ResponseFavorite

}