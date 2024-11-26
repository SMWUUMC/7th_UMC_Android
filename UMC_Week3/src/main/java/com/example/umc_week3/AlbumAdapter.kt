package com.example.umc_week3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_week3.databinding.ItemLayoutBinding

class AlbumAdapter(
    private val albums: List<Album>,
    private val onItemClick: (Album) -> Unit,
    private val onPlayClick: (Album) -> Unit // Play 버튼 클릭 콜백 추가
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    inner class AlbumViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            // RoomDB에서 가져온 데이터를 사용하여 UI를 업데이트
            album.coverImg?.let { binding.albumCover.setImageResource(it) } // coverImg는 Int? 타입
            binding.albumTitle.text = album.title
            binding.albumArtist.text = album.singer

            // 앨범 아이템 클릭 시
            binding.root.setOnClickListener {
                onItemClick(album)
            }

            // Play 버튼 클릭 시
            binding.playIcon.setOnClickListener {
                onPlayClick(album)
            }
        }
    }
}