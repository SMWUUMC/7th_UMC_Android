package com.example.umc_week6

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_week6.databinding.ItemPokemonBinding

class PokemonAdapter(
    private val context: Context,
    private val pokemonList: List<Pokemon>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pokemon = pokemonList[position]
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra("pokemonNumber", pokemon.pokemonNumber)
                        putExtra("name", pokemon.name)
                        putExtra("imageResId", pokemon.imageResId)
                        putExtra("backgroundColor", pokemon.backgroundColor)
                        putExtra("type", pokemon.type)
                        putExtra("ovalColor", pokemon.ovalColor)
                        putExtra("weight", pokemon.weight)
                        putExtra("height", pokemon.height)
                        putIntegerArrayListExtra("baseStats", ArrayList(pokemon.baseStats))
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.binding.pokemonImage.setImageResource(pokemon.imageResId)
        holder.binding.pokemonName.text = pokemon.name
        holder.binding.itemLayout.setCardBackgroundColor(Color.parseColor(pokemon.backgroundColor))

        holder.binding.itemLayout.post {
            val width = holder.binding.itemLayout.width
            holder.binding.itemLayout.layoutParams.height = width + 20
            holder.binding.itemLayout.requestLayout()
        }
    }

    override fun getItemCount() = pokemonList.size
}
