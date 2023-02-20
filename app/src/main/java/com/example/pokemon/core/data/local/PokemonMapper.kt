package com.example.pokemon.core.data.local

import com.example.pokemon.core.domain.models.*

fun PokemonTable.toDomainModel(): PokemonResult {
    return PokemonResult(
        name = this.name,
        url = this.url,
        isFavorite = this.isFavorite
    )
}

fun PokemonResult.toDatabaseModel(): PokemonTable {
    return PokemonTable(
        name = this.name,
        url = this.url,
        isFavorite = this.isFavorite
    )
}