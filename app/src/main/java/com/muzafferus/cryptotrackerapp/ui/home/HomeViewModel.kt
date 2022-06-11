package com.muzafferus.cryptotrackerapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muzafferus.cryptotrackerapp.data.entities.CryptoModel
import com.muzafferus.cryptotrackerapp.data.repository.CryptoRepository
import com.muzafferus.cryptotrackerapp.util.Resource
import com.muzafferus.cryptotrackerapp.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository
) : ViewModel() {

    private val _cryptoList by lazy {
        MutableLiveData<ViewState<
                Resource<List<CryptoModel>>>>()
    }
    val cryptoList: LiveData<ViewState<
            Resource<List<CryptoModel>>>>
        get() = _cryptoList

    fun getCryptoList() = viewModelScope.launch {
        _cryptoList.postValue(ViewState.Loading())
        try {
            val response = cryptoRepository.getCrypto()
            _cryptoList.postValue(ViewState.Success(response))
        } catch (e: Exception) {
            _cryptoList.postValue(ViewState.Error(e.message))
        }
    }
}