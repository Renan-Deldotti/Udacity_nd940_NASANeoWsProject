package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    /*private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }*/
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var asteroidsListAdapter : AsteroidsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidsListAdapter = AsteroidsListAdapter(AsteroidsListAdapter.AsteroidsItemListener {
            //Toast.makeText(context, "Test: ${it.id}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        /* Test RecyclerView */
        val asteroidsList = arrayListOf<Asteroid>()
        (1 .. 100).forEach {
            asteroidsList.add(Asteroid(it.toLong(),"Asteroid $it", "2022-01-01", 0.3 + it, 0.5 + it, 1.0 + it, 100.0, ((it % 2) == 0)))
        }
        asteroidsListAdapter.submitList(asteroidsList)

        binding.asteroidRecycler.adapter = asteroidsListAdapter


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
