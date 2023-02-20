package com.example.pokemon.core.infrastructure

import android.content.Context
import com.example.pokemon.core.data.local.DatabaseManager
import com.example.pokemon.core.data.local.getDatabase
import com.example.pokemon.core.data.local.toDatabaseModel
import com.example.pokemon.core.data.local.toDomainModel
import com.example.pokemon.core.data.remote.ApiService
import com.example.pokemon.core.data.remote.PokemonService
import com.example.pokemon.core.domain.models.Pokemon
import com.example.pokemon.core.domain.models.PokemonDetail
import com.example.pokemon.core.domain.models.PokemonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository {

    lateinit var service: PokemonService
    lateinit var database: DatabaseManager

    companion object {
        private var INSTANCE: PokemonRepository? = null
        fun getInstance(context: Context): PokemonRepository? {
            if (INSTANCE == null) {
                INSTANCE = PokemonRepository()
                INSTANCE?.service = ApiService().retrieveService()
                INSTANCE?.database = getDatabase(context)
            }
            return INSTANCE
        }
    }

    private fun getAllPokemon(onSuccess: (pokemon: Pokemon) -> Unit, onError: (e: Throwable) -> Unit) {
        val call = service.getAll()
        call.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                response.body().let {
                    it?.let { pokemon ->
                        onSuccess(pokemon)
                    }
                }
            }
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getPokemon(name: String, onSuccess: (pokemon: PokemonDetail) -> Unit, onError: (e: Throwable) -> Unit) {
        val call = service.getPokemon(name = name)
        call.enqueue(object : Callback<PokemonDetail> {
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                response.body().let {
                    it?.let { pokemon ->
                        onSuccess(pokemon)
                    }
                }
            }
            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getPokemonListDatabase(): List<PokemonResult> {
        return database.pokemonDao.getAll().map { it.toDomainModel() }
    }

    fun getPokemonFavoriteListDatabase(): List<PokemonResult> {
        return database.pokemonDao.getAllFavorites().map { it.toDomainModel() }
    }

    fun updateFavoritePokemon(name: String, favorite: Boolean): List<PokemonResult> {
        database.pokemonDao.updateFavoritePokemon(name = name)
        if (favorite) {
            return getPokemonFavoriteListDatabase()
        }
        return getPokemonListDatabase()
    }

    suspend fun getPokemonList() {
        withContext(Dispatchers.IO) {
            try {
                getAllPokemon(
                    onError = {},
                    onSuccess = { pokemon ->
                        database.pokemonDao.insertAll(pokemon.results.map { it.toDatabaseModel()})
                    }
                )
            } catch (error: Exception) {
                println(error)
            }
        }
    }
}