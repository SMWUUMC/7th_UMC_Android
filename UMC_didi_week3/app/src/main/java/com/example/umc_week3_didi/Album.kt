package com.example.umc_week3_didi

data class Album(
    val coverResId: Int,       // 앨범 커버 이미지 리소스 ID
    val title: String,         // 앨범 제목
    val artist: String,        // 가수 이름
    val info: String,          // 앨범 정보 (예: 발매일, 앨범 유형)
    val trackList: List<String> // 수록곡 목록 (곡 제목 리스트)
)
