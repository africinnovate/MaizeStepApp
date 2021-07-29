package com.chuks.maizestemapp.capturedinsect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuks.maizestemapp.common.data.Insect
import com.chuks.maizestemapp.capturedinsect.repository.CapturedInsectRepository
import kotlinx.coroutines.launch

/**
 * This is CapturedInsectViewModel. This has a param [capturedInsectRepository]
 * and extends [ViewModel]
 */
class CapturedInsectViewModel(private val capturedInsectRepository: CapturedInsectRepository)
    : ViewModel() {

    private val _showMessage : MutableLiveData<String> = MutableLiveData()
    val showMessage : LiveData<String> get() = _showMessage

    private val _showProgress : MutableLiveData<Boolean> = MutableLiveData()
    val showProgress : LiveData<Boolean> get() = _showProgress


    val capturedInsect = capturedInsectRepository.getAllCapturedInsect()

    fun deleteInsect(id : String?){
        viewModelScope.launch {
            capturedInsectRepository.deleteInsect(id)
        }
    }

    fun deleteAllInsect(){
        viewModelScope.launch { capturedInsectRepository.deleteAllInsect() }
    }

    init {

        viewModelScope.launch {
            try {
                _showProgress.postValue(true)
                capturedInsectRepository.requestCapturedInsect()
                _showProgress.postValue(false)

            }catch (e : Throwable){
                _showMessage.postValue("Unable to connect to server!")
                _showProgress.postValue(false)
            }
        }
    }

}