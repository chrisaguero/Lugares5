package com.example.sem08.repository

import androidx.lifecycle.LiveData
import com.example.sem08.data.LugarDao
import com.example.sem08.model.Lugar
class LugarRepository(private val lugarDao: LugarDao) {

    suspend fun saveLugar(lugar: Lugar) {
        if (lugar.id==0) {  //Es un lugar nuevo
            lugarDao.addLugar(lugar)
        } else {  //El lugar existe... entonces se actualiza...
            lugarDao.updateLugar(lugar)
        }
    }

    suspend fun deleteLugar(lugar: Lugar) {
        lugarDao.deleteLugar(lugar)
    }

    val getLugares : LiveData<List<Lugar>> = lugarDao.getLugares()
}