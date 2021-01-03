package com.example.mediaplayer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class VideoAdapter(private val context: Context, private val videosFiles: ArrayList<VideosFiles>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Creamos un Inflate de Layout, para pasar el view de los items.
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pasamos la posiciones de los archivos de video.
        holder.anadirViewHolder(videosFiles[position])

        // Si hacemos click en el Video, cargaremos el activity del reproductor.
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ReproductorActivity::class.java)
            // Pasamos el path de los videos.
            intent.putExtra("path", videosFiles[position].path)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // Obtenemos el tamaño del arraylist.
        return videosFiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Creamos las variables para mostrar el thumbnail del video, el menu de las opciones de video, el nombre del video y la duración.
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val videoMenu: ImageView = itemView.findViewById(R.id.video_menu)
        private val videoNombre: TextView = itemView.findViewById(R.id.video_nombre)
        private val duracionVideo: TextView = itemView.findViewById(R.id.duracion_video)
        fun anadirViewHolder(videosFiles: VideosFiles) {
            // Añadimos el nombre del archivo del video.
            videoNombre.text = videosFiles.nombreVideo
            // Cargamos el Thumbnail a través del Glide.
            Glide.with(itemView.context).load(File(videosFiles.path)).into(thumbnail)
            // Cargamos la duración del video.
            duracionVideo.text = videosFiles.duracion
            // Borrar un video cuando le demos al icono de video menu.
            videoMenu.setOnClickListener { borrarVideo(videosFiles) }
        }
        fun borrarVideo(videosFiles: VideosFiles){
            var alertaDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
            alertaDialog.setTitle("ALERTA")
            alertaDialog.setMessage("Va a borrar un video, esta seguro?")
            alertaDialog.setPositiveButton(
                    "Si"
            ) {_, _ ->
                val video = File(videosFiles.path)
                video.delete()
                Log.e("BORRADO", videosFiles.nombreVideo + "se ha borrado correctamente")

                MediaScannerConnection.scanFile(itemView.context, arrayOf(video.toString()),
                arrayOf(video.name), null)
            }
            alertaDialog.setNegativeButton(
                    "No"
            ) {_, _ -> }
            val alerta: AlertDialog = alertaDialog.create()
            alerta.setCanceledOnTouchOutside(false)
            alerta.show()
        }
    }
}