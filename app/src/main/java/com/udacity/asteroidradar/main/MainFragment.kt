package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireActivity().application)
    }

    private lateinit var asteroidsListAdapter : AsteroidsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidsListAdapter = AsteroidsListAdapter(AsteroidsListAdapter.AsteroidsItemListener {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        viewModel.asteroids.observe(viewLifecycleOwner) { asteroidsListAdapter.submitList(it) }
        viewModel.todayImage.observe(viewLifecycleOwner) {
            it?.let {
                if (Constants.IMAGE_MEDIA_TYPE == it.mediaType) {
                    Picasso.with(context).load(it.url).into(binding.activityMainImageOfTheDay)
                } else {
                    Picasso.with(context).load(R.drawable.ic_broken_image).into(binding.activityMainImageOfTheDay)
                }
            }
        }

        binding.asteroidRecycler.adapter = asteroidsListAdapter


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_today_asteroids_menu -> {viewModel.filterAsteroids(MainViewModel.AsteroidFilters.TODAY_ASTEROIDS)}
            R.id.show_week_asteroids_menu -> {viewModel.filterAsteroids(MainViewModel.AsteroidFilters.WEEK_ASTEROIDS)}
            else -> viewModel.filterAsteroids(MainViewModel.AsteroidFilters.SAVED_ASTEROIDS)
        }
        return true
    }
}
