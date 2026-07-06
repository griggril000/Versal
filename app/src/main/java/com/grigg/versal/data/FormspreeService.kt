package com.grigg.versal.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FormspreeApi {
    @FormUrlEncoded
    @POST("f/mbdvwwgp")
    suspend fun submitForm(
        @Field("email") email: String,
        @Field("type") type: String,
        @Field("message") message: String
    ): Response<Unit>
}

object FormspreeService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://formspree.io/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api: FormspreeApi = retrofit.create(FormspreeApi::class.java)
}
