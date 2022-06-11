package com.muzafferus.cryptotrackerapp.ui.alerthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.data.repository.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertHistoryViewModel @Inject constructor(
    private val alertRepository: AlertRepository
) : ViewModel() {

    fun getCryptoList(cryptoId: String) = liveData {
        alertRepository.getCryptoAlert(cryptoId).collect {
            emit(it)
        }
    }

    fun delete(alert: AlertModel) {
        viewModelScope.launch {
            alertRepository.delete(alert)
        }
    }
}