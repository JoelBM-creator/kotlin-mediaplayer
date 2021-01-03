package com.example.mediaplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

class VideosFragment(private val videosFiles: ArrayList<VideosFiles>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_videos, container, false)
        val recyclerView = view.findViewById(R.id.videosRV) as RecyclerView
        if( videosFiles != null || videosFiles.size > 0 ) {
            val videoAdapter = VideoAdapter(view.context, this.videosFiles)
            recyclerView.adapter = videoAdapter
            recyclerView.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
        return view
    }
}