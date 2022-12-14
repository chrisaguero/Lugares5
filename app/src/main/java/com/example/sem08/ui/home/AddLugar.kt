package com.example.sem08.ui.home

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.sem08.R
import com.example.sem08.databinding.FragmentAddLugarBinding
import com.example.sem08.model.Lugar
import com.example.sem08.utilidades.AudioUtiles
import com.example.sem08.utilidades.imagenUtiles
import com.example.sem08.viewmodel.HomeViewModel
import android.content.Intent
import android.net.Uri
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class AddLugar : Fragment() {

    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var audioUtiles: AudioUtiles
    private lateinit var imagenUtiles: imagenUtiles
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(homeViewModel::class.java)
        _binding = FragmentAddLugarBinding.inflate(inflater,container,false)

        binding.btAddLugar.setOnClickListener{

          binding.progressBar.visibility = ProgressBar.VISIBLE
          binding.msgMensaje.text = getString(R.string.msgGuardandoLugar)
            binding.msgMensaje.visibility = TextView.VISIBLE
            subirAudio()

        }
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_lugar, container, false)

        //Audio
        audioUtiles = AudioUtiles(requireActivity(),requireContext(), binding.btAccion, binding.btPlay, binding.btDelete, getString(R.string.msgInicioNotaAudio),getString(R.string.msgDetieneNotaAudio))

        //Fotos
        tomarFotoActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = imagenUtiles(requireContext(), binding.btPhoto, binding.btRotaL, binding.btRotaR, binding.imagen, tomarFotoActivity)

        return binding.root
    }

    private fun subirAudio(){
        val audioFile = audioUtiles.audioFile
        if(audioFile.exists() && audioFile.isFile && audioFile.canRead()){
            val ruta = Uri.fromFile(audioFile)
            val rutaNube = "lugaresM/${Firebase.auth.currentUser?.email}/audios/${audioFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta).addOnSuccessListener {
                referencia.downloadUrl.addOnSuccessListener{
                    val rutaAudio = it.toString()
                    subirImagen(rutaAudio)
                }
            }
                .addOnCanceledListener {  subirImagen("")}
        }
        else{
            subirImagen("")
        }
    }

    private fun subirImagen(rutaAudio:String){
        val imagenFile = imagenUtiles.imagenFile
        if(imagenFile.exists() && imagenFile.isFile && imagenFile.canRead()){
            val ruta = Uri.fromFile(imagenFile)
            val rutaNube = "lugaresM/${Firebase.auth.currentUser?.email}/imagenes/${imagenFile.name}"
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)
            referencia.putFile(ruta).addOnSuccessListener {
                referencia.downloadUrl.addOnSuccessListener{
                    val rutaImagen = it.toString()
                    agregarLugar(rutaAudio,rutaImagen)
                }
            }
                .addOnCanceledListener {  agregarLugar(rutaAudio,"")}
        }
        else{
            agregarLugar(rutaAudio,"")
        }
    }

    private fun agregarLugar(rutaAudio: String,rutaImage: String){

        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreoLugar.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()

        if(nombre.isNotEmpty()){

            val lugar = Lugar("",nombre,correo,telefono,web,rutaAudio,rutaImage)
            //proceso de agregar a DB

        }else{
            Toast.makeText(requireContext(),getString(R.string.msg_error),Toast.LENGTH_LONG).show()
        }
    }

}