package com.example.pokemon.ui.home

import androidx.lifecycle.*
import com.example.pokemon.core.domain.models.PokemonResult
import com.example.pokemon.core.infrastructure.PokemonRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PokemonRepository): ViewModel() {

    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>>
        get() = _pokemonList

    init {
        syncPokemon()
        getPokemonDatabase()
    }

    private fun getPokemonDatabase() {
        viewModelScope.launch {
            _pokemonList.postValue(repository.getPokemonListDatabase())
        }
    }

    private fun syncPokemon() {
        viewModelScope.launch {
            repository.getPokemonList()
        }
    }

    fun updateFavoritePokemon(name: String) {
        viewModelScope.launch {
            _pokemonList.postValue(repository.updateFavoritePokemon(name = name, favorite = false))
        }
    }

    class Factory(private val pokemonRepository: PokemonRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(pokemonRepository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}