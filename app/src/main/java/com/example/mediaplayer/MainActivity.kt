package com.example.mediaplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSION = 123

    // Creamos un arraylist de VideosFiles para luego pasarlo al Fragmento de Videos.
    private var videosFiles = ArrayList<VideosFiles>()

    // Creamos un arraylist de lista de carpetas para luego pasarlo al Fragmento de Carpetas.
    private var listaCarpetas = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Comprobamos si han aceptado los permisos.
        this.permisos()
        // Cuando seleccionamos algún item del Nav View del bottom mostraremos un Toast y lo marcamos como checked True.
        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.listaCarpetas -> {
                    Toast.makeText(this, "Carpetas", Toast.LENGTH_SHORT).show()
                    // Creamos una trasacción de Fragmento, cuando seleccionemos carpetas nos desplazara al Fragment de Carpetas además pasamos los ArrayList como parametros.
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.mainFragment, CarpetasFragment(listaCarpetas, videosFiles))
                    fragmentTransaction.commit()
                    menuItem.isChecked = true
                    true
                }
                R.id.listaVideos -> {
                    Toast.makeText(this, "Videos", Toast.LENGTH_SHORT).show()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    // Creamos una trasacción de Fragmento, cuando seleccionemos Videos nos desplazara al Fragment de Videos además pasamos el ArrayList de VideosFiles como parametros.
                    fragmentTransaction.replace(R.id.mainFragment, VideosFragment(videosFiles))
                    fragmentTransaction.commit()
                    menuItem.isChecked = true
                    true
                }
                else -> false
            }
        }
    }

    /* Función de permisos().
    * Comprueba del permiso y de que permiso se trata. Si no es igual que el permiso que se otorga,
    * Volveremos abrir la ventana de permisos y solucitamos nuevamente los permisos. En el arrayOf,
    * se insertaran todos los permisos, que necesitemos controlar. Cuando el usuario los acepte,
    * nos saltara el Toast. */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun permisos() = if(ContextCompat.checkSelfPermission(applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
    } else {
        Toast.makeText(this, "Permisos Concedidos", Toast.LENGTH_SHORT).show()
        // Obtenemos los videos almacenados en las carpetas.
        videosFiles = obtenerVideos(this)
        // Este será el fragmento por defecto al iniciar la aplicación, en caso que se quiere por Videos, cambiamos el Fragmneto por Videos y pasamos el ArrayList.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragment, CarpetasFragment(listaCarpetas, videosFiles))
        fragmentTransaction.commit()
    }

    /* Función de onRequestPemissionsResult() generado por Kotlin.
    * Comprobamos que el requestCode sea igual al introducido, si es así comprobara que el grantResult
    * de los permisos se han concedido si es así, saltara el Toast y en caso contrario, volveremos
    * a pedir los permisos al usario hasta que acepte los permisos. */
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos Concedidos", Toast.LENGTH_SHORT).show()
                // Obtenemos los videos almacenados en las carpetas.
                videosFiles = obtenerVideos(this )
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.mainFragment, CarpetasFragment(listaCarpetas, videosFiles))
                fragmentTransaction.commit()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
            }
        }
    }

    /* Función de obtenerVideos() : ArrayList<VideosFiles>.
    * Obtendremos todos los Videos almacenados dentro del móvil, en este caso
    * crearemos un Array de los Videos temporales, además de la URIl, crearemos una projección del
    * video para pasarlo al Cursor. */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun obtenerVideos(context: Context): ArrayList<VideosFiles> {
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

        val cursor = context.contentResolver.query(uriExternal, projection, null, null, null, null)
        // Si en el cursor se encuenta algún vídeo entonces, lo añadiremos al Array de videosFiles.
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
                Log.e("Duracion", duracion)
                // Comprobamos que detecte el URI del PATH.
                Log.e("Path", path )
                // Obtenemos el path de donde se encuetre el .mp4 -- /storage/sd_card/videos/nombreVideo.mp4
                val primerIndex = path.lastIndexOf("/")
                // En este obtenemos /storage/sd_card/videos
                val subString = path.substring(0, primerIndex)
                Log.e("Primer Index", subString)
                val index = subString.lastIndexOf("/")
                /* Obtenemos el nombre de la carpeta donde se encuentran los vídeos, en caso de que en el
                ArrayList de Carpetas no se encuentre se añadira dicha carpeta para mostrarse por el fragment. */
                val nombreCarpeta = subString.substring(index + 1, primerIndex)
                Log.e("Nombre carpeta", nombreCarpeta)
                if(!listaCarpetas.contains(nombreCarpeta))
                    listaCarpetas.add(nombreCarpeta)

                // Almacenmos los datos, en el arraylist temporal.
                tempVideosFiles.add(VideosFiles(id, path, titulo, nombreVideo, tamano, fechaAnadido, stringDuracion))
            }
            // Cerramos el cursos
            cursor.close()
        }
        // Retornamos el arraylist temporal de los videos.
        return tempVideosFiles
    }
}