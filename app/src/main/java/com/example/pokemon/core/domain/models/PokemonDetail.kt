package com.example.pokemon.core.domain.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonDetail (
    val id: Long,
    val height: String,
    val name: String,
    val order: Long,
    val sprites: Sprites,
    val weight: String
) : Parcelable

@Parcelize
data class Ability (
    val ability: Detail,
) : Parcelable

@Parcelize
data class Detail (
    val name: String,
    val url: String
) : Parcelable

@Parcelize
data class Move (
    val move: Detail,
) : Parcelable

@Parcelize
data class Sprites (
    @Json(name = "back_default")
    val back_default: String,

    @Json(name = "back_shiny")
    val back_shiny: String,

    @Json(name = "front_default")
    val front_default: String,

    @Json(name = "front_shiny")
    val front_shiny: String

) : Parcelable

@Parcelize
data class Type (
    val slot: Long,
    val type: Detail
) : Parcelable