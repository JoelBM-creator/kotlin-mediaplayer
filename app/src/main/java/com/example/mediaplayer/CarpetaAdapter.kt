package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarpetaAdapter(private val context: Context, private val videosFiles: ArrayList<VideosFiles>, private val nombreCarpetas: ArrayList<String> ) : RecyclerView.Adapter<CarpetaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.carpeta_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.carpetaNombre.text = nombreCarpetas[position]
        holder.itemView.setOnClickListener{
            val intent = Intent(context, VistaCarpetaActivity::class.java)
            intent.putExtra("carpetaNombre", nombreCarpetas[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return nombreCarpetas.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carpetaNombre: TextView = itemView.findViewById(R.id.nombreCarpeta)
    }
}