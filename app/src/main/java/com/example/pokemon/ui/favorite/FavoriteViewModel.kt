package com.example.pokemon.ui.favorite

import androidx.lifecycle.*
import com.example.pokemon.core.domain.models.PokemonResult
import com.example.pokemon.core.infrastructure.PokemonRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: PokemonRepository): ViewModel() {

    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>>
        get() = _pokemonList

    init {
        getFavoritePokemonDatabase()
    }

    private fun getFavoritePokemonDatabase() {
        viewModelScope.launch {
            _pokemonList.postValue(repository.getPokemonFavoriteListDatabase())
        }
    }

    fun updateFavoritePokemon(name: String) {
        viewModelScope.launch {
            _pokemonList.postValue(repository.updateFavoritePokemon(name = name, favorite = true))
        }
    }

    class Factory(private val pokemonRepository: PokemonRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(pokemonRepository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}