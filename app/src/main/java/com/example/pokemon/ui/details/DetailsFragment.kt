package com.example.pokemon.ui.details

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pokemon.core.infrastructure.PokemonRepository
import com.example.pokemon.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.schedule

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this,
            DetailsViewModel.Factory(args.name, PokemonRepository.getInstance(requireContext())!!)
        )[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        _binding?.lifecycleOwner = this
        _binding?.viewModel = viewModel

        setupView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        setupPokemon()
        click()
    }

    private fun setupPokemon() {
        viewModel.pokemon.observe(viewLifecycleOwner, Observer {
            binding.pokemon = it
            it.sprites.front_default.also { url ->
                Picasso.with(requireContext())
                    .load(url)
                    .into(binding.image)
            }
        })
    }

    private fun click() {
        _binding?.motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                findNavController().popBackStack()
            }
        })
    }
}