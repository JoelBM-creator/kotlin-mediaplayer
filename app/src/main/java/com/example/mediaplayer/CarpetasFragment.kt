package com.example.mediaplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarpetasFragment(private val listaCarpetas: ArrayList<String>, private val videosFiles: ArrayList<VideosFiles>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_carpetas, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.carpetasRV)
        if(listaCarpetas != null && listaCarpetas.size > 0 && videosFiles != null) {
            val carpetaAdapter = CarpetaAdapter(view.context, this.videosFiles, this.listaCarpetas)
            recyclerView.adapter = carpetaAdapter
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        return view
    }
}