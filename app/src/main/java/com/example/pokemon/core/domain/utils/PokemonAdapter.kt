package com.example.pokemon.core.domain.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.core.domain.models.PokemonResult
import com.example.pokemon.databinding.PokemonItemBinding

class PokemonAdapter(
    private val clickListener: (PokemonResult, Boolean) -> Unit
) : ListAdapter<PokemonResult, PokemonAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ViewHolder(private val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(pokemon: PokemonResult){

            binding.pokemon =  pokemon

            binding.favoriteButton.setOnClickListener {
                clickListener(pokemon, true)
            }

            binding.root.setOnClickListener {
                clickListener(pokemon, false)
            }
        }
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<PokemonResult>() {
    override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean = oldItem.isFavorite == newItem.isFavorite && oldItem.name == newItem.name && oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean = oldItem.isFavorite == newItem.isFavorite  && oldItem.name == newItem.name && oldItem.url == newItem.url
}