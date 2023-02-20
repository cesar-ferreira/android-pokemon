package com.example.pokemon.core.data.remote

import com.example.pokemon.core.domain.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiService {

    fun retrieveService(): PokemonService {
        val client = OkHttpClient.Builder()
            .build()

        val moshi = Moshi.Builder().build()
        val retrofitICare = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofitICare.create(PokemonService::class.java)
    }
}