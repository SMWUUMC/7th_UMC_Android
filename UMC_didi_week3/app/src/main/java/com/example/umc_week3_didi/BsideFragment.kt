package com.example.umc_week3_didi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_week3_didi.databinding.FragmentBsideBinding

class BsideFragment : Fragment() {

    private var _binding: FragmentBsideBinding? = null
    private val binding get() = _binding!!

    private var trackList: List<String>? = null
    private var artistName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBsideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 수록곡 리스트와 가수 이름을 arguments에서 받아옴
        trackList = arguments?.getStringArrayList("trackList")
        artistName = arguments?.getString("artistName")

        // RecyclerView에 수록곡 리스트를 표시
        trackList?.let {
            Log.d("BsideFragment", "TrackList in BsideFragment: $trackList")

            val adapter = TrackListAdapter(it, artistName ?: "Unknown Artist")
            binding.recyclerViewTracks.adapter = adapter
            binding.recyclerViewTracks.layoutManager = LinearLayoutManager(context)
        } ?: run {
            Log.e("BsideFragment", "TrackList is null")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(trackList: List<String>, artistName: String): BsideFragment {
            val fragment = BsideFragment()
            fragment.arguments = Bundle().apply {
                putStringArrayList("trackList", ArrayList(trackList))
                putString("artistName", artistName)
            }
            return fragment
        }
    }
}
