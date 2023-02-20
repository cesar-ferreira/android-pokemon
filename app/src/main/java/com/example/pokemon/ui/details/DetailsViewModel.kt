package com.example.pokemon.ui.details

import androidx.lifecycle.*
import com.example.pokemon.core.domain.models.PokemonDetail
import com.example.pokemon.core.infrastructure.PokemonRepository
import kotlinx.coroutines.launch

class DetailsViewModel(private val name: String, private val repository: PokemonRepository): ViewModel() {

    private val _pokemon = MutableLiveData<PokemonDetail>()
    val pokemon: LiveData<PokemonDetail>
        get() = _pokemon

    init {
        getPokemonDetail()
    }

    private fun getPokemonDetail() {
        viewModelScope.launch {
            repository.getPokemon(name = name,
                onError = { },
                onSuccess = {
                    _pokemon.postValue(it)
                }
            )
        }
    }

    class Factory(private val name: String, private val pokemonRepository: PokemonRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailsViewModel(name, pokemonRepository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}