package com.example.pokemon.ui.favorite

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pokemon.R
import com.example.pokemon.core.domain.models.PokemonResult
import com.example.pokemon.core.domain.utils.PokemonAdapter
import com.example.pokemon.core.infrastructure.PokemonRepository
import com.example.pokemon.databinding.FragmentFavoriteBinding
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val adapter = PokemonAdapter { pokemon, isFavorite ->
        if (!isFavorite) {
            findNavController().navigate(FavoriteFragmentDirections.actionNavigationFavoriteToDetailsFragment(pokemon.name))
        } else {
            viewModel.updateFavoritePokemon(name = pokemon.name)
            requestPermission(pokemon = pokemon)
        }
    }

    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this,
            FavoriteViewModel.Factory(PokemonRepository.getInstance(requireContext())!!)
        )[FavoriteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        _binding?.recyclerView?.adapter = adapter
        _binding?.lifecycleOwner = this
        _binding?.viewModel = viewModel

//        checkPermission()
        setupView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        setupListOfFavoritePokemon()
    }

    private fun setupListOfFavoritePokemon() {
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun requestPermission(pokemon: PokemonResult) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                sendNotification(pokemon = pokemon)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                    binding.root,
                    "Notification blocked",
                    Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", "com.example.pokemon", null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }
        }
    }

    private fun sendNotification(pokemon: PokemonResult) {
        val notificationManager = requireContext()
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            notificationManager.getNotificationChannel("Notification") == null
        ) {
            val channel = NotificationChannel(
                "Notification",
                "Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val status = if (pokemon.isFavorite) "removed" else "added"
        val builder = NotificationCompat.Builder(requireContext(), "Notification")
            .setContentTitle(pokemon.name)
            .setContentText("It has been successfully $status to your favorites list")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setAutoCancel(true)

        notificationManager.notify(1, builder.build())
    }
}