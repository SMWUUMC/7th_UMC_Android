package com.example.umc_week3_didi

import BannerItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_week3_didi.databinding.ItemBannerBinding

class BannerAdapter(private val bannerList: List<BannerItem>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int = bannerList.size

    inner class BannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BannerItem) {
            binding.bannerTitle.text = item.title
            binding.bannerSubtitle.text = item.subtitle
            binding.albumCover1.setImageResource(item.albumCover1)
            binding.albumTitle1.text = item.albumTitle1
            binding.albumArtist1.text = item.artist1
            binding.albumCover2.setImageResource(item.albumCover2)
            binding.albumTitle2.text = item.albumTitle2
            binding.albumArtist2.text = item.artist2

            // 배너의 배경 이미지를 설정
            binding.bannerContainer.setBackgroundResource(item.bannerImage)
        }
    }
}
