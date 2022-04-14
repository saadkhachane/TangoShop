package com.xardev.tangoshop.presentation.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xardev.tangoshop.domain.repositories.ProductRepository
import com.xardev.tangoshop.domain.schedulers.SchedulersProvider
import com.xardev.tangoshop.utils.Result
import com.xardev.tangoshop.utils.Result.Failure
import com.xardev.tangoshop.utils.Result.Success
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val _loadingVisibility = MutableLiveData<Int>()
    val loadingVisibility : LiveData<Int> = _loadingVisibility

    private val _noProductsVisibility = MutableLiveData<Int>()
    val noProductsVisibility : LiveData<Int> = _noProductsVisibility

    private val _result = MutableLiveData<Result<*>>()
    val result: LiveData<Result<*>> = _result

    private val disposables = CompositeDisposable()

    fun getProducts() {

        showLoading()

        val single = repository.getProducts()
            .subscribeOn(schedulers.IO)
            .observeOn(schedulers.MAIN)
            .subscribe({ value ->
                hideLoading()
                if (value.isEmpty()) showNoProducts() else hideNoProducts()
                _result.postValue(Success(value))
            }, { error ->
                hideLoading()
                _result.postValue(Failure<Throwable>(error))
            })

        disposables.add(single)

    }

    private fun showLoading(){
        _loadingVisibility.postValue(View.VISIBLE)
    }
    private fun hideLoading(){
        _loadingVisibility.postValue(View.GONE)
    }
    private fun showNoProducts(){
        _noProductsVisibility.postValue(View.VISIBLE)
    }
    private fun hideNoProducts(){
        _noProductsVisibility.postValue(View.GONE)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}