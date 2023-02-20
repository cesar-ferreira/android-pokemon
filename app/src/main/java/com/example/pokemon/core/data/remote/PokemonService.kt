package com.example.pokemon.core.data.remote

import com.example.pokemon.core.domain.models.Pokemon
import com.example.pokemon.core.domain.models.PokemonDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface PokemonService {

    @GET("pokemon?limit=100000&offset=0")
    fun getAll(): Call<Pokemon>

    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String): Call<PokemonDetail>
}