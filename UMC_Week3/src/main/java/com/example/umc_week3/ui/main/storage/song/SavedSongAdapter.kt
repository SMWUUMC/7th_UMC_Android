package com.example.umc_week3.ui.main.storage.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_week3.R
import com.example.umc_week3.data.entities.SavedSong

class SavedSongAdapter(
    private val songList: MutableList<SavedSong>,
    private val onItemDeleted: (Int) -> Unit
) : RecyclerView.Adapter<SavedSongAdapter.SavedSongViewHolder>() {

    inner class SavedSongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.music_thumbnail)
        val title: TextView = itemView.findViewById(R.id.music_title)
        val artist: TextView = itemView.findViewById(R.id.music_artist)
        val moreIcon: ImageView = itemView.findViewById(R.id.more_icon)

        init {
            // 더보기 아이콘 클릭 시 아이템 삭제
            moreIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemDeleted(position) // 해당 position을 전달하여 삭제
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_storage_song, parent, false)
        return SavedSongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedSongViewHolder, position: Int) {
        val song = songList[position]
        holder.thumbnail.setImageResource(song.thumbnailResId)
        holder.title.text = song.title
        holder.artist.text = song.artist
    }

    override fun getItemCount(): Int = songList.size
}
