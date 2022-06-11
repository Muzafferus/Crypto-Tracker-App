package com.muzafferus.cryptotrackerapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muzafferus.cryptotrackerapp.data.entities.AlertModel
import com.muzafferus.cryptotrackerapp.data.entities.DetailModel
import com.muzafferus.cryptotrackerapp.data.entities.PriceModel
import com.muzafferus.cryptotrackerapp.data.repository.AlertRepository
import com.muzafferus.cryptotrackerapp.data.repository.CryptoRepository
import com.muzafferus.cryptotrackerapp.util.Resource
import com.muzafferus.cryptotrackerapp.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _cryptoPrice by lazy {
        MutableLiveData<ViewState<
                Resource<HashMap<String, PriceModel>>>>()
    }
    val cryptoPrice: LiveData<ViewState<
            Resource<HashMap<String, PriceModel>>>>
        get() = _cryptoPrice

    private val _cryptoDetail by lazy {
        MutableLiveData<ViewState<
                Resource<DetailModel>>>()
    }
    val cryptoDetail: LiveData<ViewState<
            Resource<DetailModel>>>
        get() = _cryptoDetail

    fun getCryptoPrice(cryptoId: String) = viewModelScope.launch {
        _cryptoPrice.postValue(ViewState.Loading())
        try {
            val response = cryptoRepository.getPrice(cryptoId)
            _cryptoPrice.postValue(ViewState.Success(response))
        } catch (e: Exception) {
            _cryptoPrice.postValue(ViewState.Error(e.message))
        }
    }

    fun getCryptoDetails(cryptoId: String) = viewModelScope.launch {
        _cryptoDetail.postValue(ViewState.Loading())
        try {
            val response = cryptoRepository.getDetail(cryptoId)
            _cryptoDetail.postValue(ViewState.Success(response))
        } catch (e: Exception) {
            _cryptoDetail.postValue(ViewState.Error(e.message))
        }
    }

    fun insertAlert(alert: AlertModel) {
        viewModelScope.launch {
            alertRepository.insert(alert)
        }
    }


}