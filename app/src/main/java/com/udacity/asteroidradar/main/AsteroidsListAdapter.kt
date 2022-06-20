package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.SingleAsteroidItemBinding

class AsteroidsListAdapter(val listener: AsteroidsItemListener) : ListAdapter<Asteroid, AsteroidsListAdapter.AsteroidsViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = SingleAsteroidItemBinding.inflate(layoutInflater, parent, false)
        return AsteroidsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val thisAsteroid = getItem(position)
        holder.itemView.setOnClickListener {
            listener.onItemClicked(thisAsteroid)
        }
        holder.bind(thisAsteroid)
    }

    class AsteroidsViewHolder (private val itemBinding: SingleAsteroidItemBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind (thisAsteroid: Asteroid) {
            itemBinding.asteroid = thisAsteroid
            itemBinding.executePendingBindings()
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class AsteroidsItemListener(val listener: (asteroid : Asteroid) -> Unit) {
        fun onItemClicked (asteroid: Asteroid) = listener(asteroid)
    }
}