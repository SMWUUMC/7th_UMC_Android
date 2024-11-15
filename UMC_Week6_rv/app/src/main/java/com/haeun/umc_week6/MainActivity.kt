package com.haeun.umc_week6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.haeun.umc_week6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 더미 데이터 생성
        val restaurantList = listOf(
            Restaurant("롯데리아", 4.7f, 100, "3,000원", "11,000원", R.drawable.sample_image),
            Restaurant("탕화쿵푸", 4.9f, 30, "4,200원", "15,000원", R.drawable.sample_image),
            Restaurant("김밥천국", 4.2f, 50, "2,500원", "10,000원", R.drawable.sample_image),
            Restaurant("맥도날드", 4.7f, 100, "3,000원", "11,000원", R.drawable.sample_image),
            Restaurant("홍콩반점", 4.9f, 30, "4,200원", "15,000원", R.drawable.sample_image),
            Restaurant("바르다김선생", 4.2f, 50, "2,500원", "10,000원", R.drawable.sample_image),
            Restaurant("버거킹", 4.7f, 100, "3,000원", "11,000원", R.drawable.sample_image),
            Restaurant("역전우동", 4.9f, 30, "4,200원", "15,000원", R.drawable.sample_image),
            Restaurant("얌샘김밥", 4.2f, 50, "2,500원", "10,000원", R.drawable.sample_image),
            Restaurant("파이브가이즈", 4.7f, 100, "3,000원", "11,000원", R.drawable.sample_image),
            Restaurant("신전떡볶이", 4.9f, 30, "4,200원", "15,000원", R.drawable.sample_image),
            Restaurant("동대문엽기떡볶이", 4.2f, 50, "2,500원", "10,000원", R.drawable.sample_image),
            )

        // RecyclerView 설정
        val adapter = RestaurantAdapter(restaurantList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
