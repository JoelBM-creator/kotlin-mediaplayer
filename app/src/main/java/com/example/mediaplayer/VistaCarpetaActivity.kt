package com.example.mediaplayer

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Adapter
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.TimeUnit

class VistaCarpetaActivity : AppCompatActivity() {
    // Creamos un arrayList de los ficheros de video.
    private var videosFiles = ArrayList<VideosFiles>()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_folder)

        // Obtenemos el Recycler view apartir del ViewById.
        val recyclerView = findViewById<RecyclerView>(R.id.VistaCarpetaRV)
        // Obtenemos el Intent del Fragment.
        val carpetaNombre = intent.getStringExtra("carpetaNombre")
        // Si tenemos nombres de carpetas, obtendremos las vídeos.
        if(carpetaNombre != null) {
            videosFiles = obtenerVideos(this, carpetaNombre)
        }
        // Mientras tengamos videos, podemos cargar el Adapter y el LayoutManager.
        if(videosFiles.size > 0) {
            val vistaCarpetaAdapter = VistaCarpetaAdapter(this, videosFiles)
            recyclerView.adapter = vistaCarpetaAdapter
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }
    }

    /* Misma función que la del MainActivity pero en este caso en el cursor
     * pasamos un selector y pasamos los ARGS del selector. (Como si fuera una query). */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun obtenerVideos(context: Context, nombreCarpeta: String): ArrayList<VideosFiles> {
        val tempVideosFiles = ArrayList<VideosFiles>()
        val uriExternal: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME
        )
        val seleccion = MediaStore.Video.Media.DATA + " like?"
        val seleccionArgs = arrayOf<String>("%" + nombreCarpeta + "%")

        val cursor = context.contentResolver.query(uriExternal, projection, seleccion, seleccionArgs, null, null)

        if(cursor != null) {
            while (cursor.moveToNext()){
                val id = cursor.getString(0)
                val path = cursor.getString(1)
                val titulo = cursor.getString(2)
                val tamano = cursor.getString(3)
                val fechaAnadido = cursor.getString(4)
                val duracion = cursor.getString(5)
                val nombreVideo = cursor.getString(6)
                // Convertimos la duración a String (Que se pueda entender).
                val stringDuracion:String = String.format("%2d:%2d", TimeUnit.MILLISECONDS.toMinutes(duracion.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(duracion.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duracion.toLong())))
                videosFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))
                // Comprobamos que detecte el URI del PATH.
                Log.e("Path", path )
                // Cargamos los videos en el String temporal de videos.
                tempVideosFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))
            }
            cursor.close()
        }
        return tempVideosFiles
    }
}