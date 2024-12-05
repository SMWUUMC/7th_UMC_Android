package com.example.umc_week3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_week3.databinding.ItemStorageAlbumBinding

class SavedAlbumAdapter(
    var albums: List<Album>, // 수정 가능하도록 var로 변경
    private val onItemClick: (Album) -> Unit
) : RecyclerView.Adapter<SavedAlbumAdapter.SavedAlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAlbumViewHolder {
        val binding = ItemStorageAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    fun updateData(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged() // RecyclerView 갱신
    }

    inner class SavedAlbumViewHolder(private val binding: ItemStorageAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            // Title, Artist, and Album Info
            binding.albumTitle.text = album.title
            binding.albumArtist.text = album.singer
            binding.albumInfo.text = "${album.releaseDate} | ${album.type} | ${album.genre}"

            // Album cover image
            album.coverImg?.let { binding.albumCover.setImageResource(it) }

            // Item click listener
            binding.root.setOnClickListener {
                onItemClick(album)
            }
        }
    }
}