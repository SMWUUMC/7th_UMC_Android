package com.example.umc_week3_didi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_week3_didi.databinding.ItemTrackBinding

class TrackListAdapter(
    private val trackList: List<String>,
    private val artistName: String
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position], position + 1)
    }

    override fun getItemCount(): Int = trackList.size

    inner class TrackViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: String, trackNumber: Int) {
            binding.trackNumber.text = String.format("%02d", trackNumber)
            binding.trackTitle.text = track
            binding.trackArtist.text = artistName
        }
    }
}
