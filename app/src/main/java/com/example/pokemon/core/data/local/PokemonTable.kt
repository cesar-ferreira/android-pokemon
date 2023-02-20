package com.example.pokemon.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonTable")
data class PokemonTable constructor(
    @PrimaryKey()
    val name: String,
    val url: String,
    val isFavorite: Boolean
)
