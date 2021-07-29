package com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuks.maizestemapp.categoriesofspecies.africanarmyworm.repository.AfricanArmyWormRepository
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.common.data.InsectModel
import kotlinx.coroutines.launch

/**
 * This is AfricanArmyWormViewModel. This has a param [africanArmyWormRepository]
 * and extends [ViewModel]
 */
class AfricanArmyWormViewModel(private val africanArmyWormRepository: AfricanArmyWormRepository)
    : ViewModel(){

    private val _showMessage : MutableLiveData<String> = MutableLiveData()
    val showMessage : LiveData<String> get() = _showMessage

    private val _showProgress : MutableLiveData<Boolean> = MutableLiveData()
    val showProgress : LiveData<Boolean> get() = _showProgress
    var _insects = MutableLiveData<List<InsectModel>>()
    var insects: LiveData<List<InsectModel>> = _insects
    fun africanArmyList(name: String)
            = africanArmyWormRepository.getAfricanArmyWorm(name)



    init {
        viewModelScope.launch {
            try {
                _showProgress.postValue(true)
                africanArmyWormRepository.requestAfricanArmyWorm()
                _showProgress.postValue(false)

            }catch (e : Throwable){
                _showMessage.postValue("Unable to connect to server!")
                _showProgress.postValue(false)
            }

        }
    }
}