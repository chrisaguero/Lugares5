package com.example.sem08.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.sem08.data.LugarDao
import com.example.sem08.model.Lugar
import com.example.sem08.repository.LugarRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val getLugares : MutableLiveData<List<Lugar>>
    private val repository: LugarRepository = LugarRepository(LugarDao())

    init {

        getLugares = repository.getLugares
    }

    fun saveLugar(lugar: Lugar) {
         repository.saveLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        repository.deleteLugar(lugar)
    }
}