package com.example.pokemon.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemonTable WHERE isFavorite = 1")
    fun getAllFavorites(): List<PokemonTable>

    @Query("SELECT * FROM pokemonTable")
    fun getAll(): List<PokemonTable>

    @Query("UPDATE pokemonTable SET isFavorite = NOT isFavorite WHERE name = :name")
    fun updateFavoritePokemon(name: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(pokemonList: List<PokemonTable>)
}