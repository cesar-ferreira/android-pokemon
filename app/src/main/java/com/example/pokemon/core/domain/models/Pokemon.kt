package com.example.pokemon.core.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemon(var results: List<PokemonResult>) : Parcelable

@Parcelize
data class PokemonResult (
    val name: String,
    val url: String,
    var isFavorite: Boolean = false
) : Parcelable
