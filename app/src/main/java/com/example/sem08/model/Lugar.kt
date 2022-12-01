package com.example.sem08.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lugar(
    var id: String,
    var nombre: String,
    var correo: String?,
    var web: String?,
    var telefono: String?,
    val rutaAudio: String?,
    val rutaImagen: String?
) : Parcelable{
    constructor():
            this("","","","","","","")
}
