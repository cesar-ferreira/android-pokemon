package com.example.pokemon.ui.home

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.core.domain.utils.PokemonAdapter
import com.example.pokemon.core.infrastructure.PokemonRepository
import com.example.pokemon.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter = PokemonAdapter { pokemon, isFavorite ->
        if (!isFavorite) {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToDetailsFragment(pokemon.name))
        } else {
            viewModel.updateFavoritePokemon(name = pokemon.name)
            createNotification(name = pokemon.name, isFavorite = pokemon.isFavorite)
        }
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this,
            HomeViewModel.Factory(PokemonRepository.getInstance(requireContext())!!)
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        _binding?.recyclerView?.adapter = adapter
        _binding?.lifecycleOwner = this
        _binding?.viewModel = viewModel

        checkPermission()
        setupView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        setupListOfPokemon()
    }

    private fun setupListOfPokemon() {
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                "Notification",
                "Notification",
                importance
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager =
                getSystemService(requireContext(), NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(name: String, isFavorite: Boolean) {
        val status = if (isFavorite) "removed" else "added"
        val builder = NotificationCompat.Builder(requireContext(), "Notification")
            .setContentTitle(name)
            .setContentText("It has been successfully $status to your favorites list")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setAutoCancel(true)

        val managerCompat = NotificationManagerCompat.from(requireContext())
        managerCompat.notify(1, builder.build())
    }
}