package com.xardev.tangoshop.presentation.viewModels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.domain.repositories.ProductRepository
import com.xardev.tangoshop.domain.schedulers.SchedulersProvider
import com.xardev.tangoshop.utils.Result
import com.xardev.tangoshop.utils.Result.Failure
import com.xardev.tangoshop.utils.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean> = _networkError

    private val _noProductsAvailable = MutableLiveData(false)
    val noProductsAvailable: LiveData<Boolean> = _noProductsAvailable

    private val _result = MutableLiveData<Result<*>>()
    val result: LiveData<Result<*>> = _result

    private val _featuredProducts = MutableLiveData<List<Product>>()
    val featuredProducts: LiveData<List<Product>> = _featuredProducts

    private val _dealTimer = MutableLiveData<Map<String, String>>()
    val dealTimer: LiveData<Map<String, String>> = _dealTimer

    private lateinit var countDownTimer: CountDownTimer
    private var isCountDownCancelled = true

    private val disposables = CompositeDisposable()

    init {
        getProducts()
    }

    fun getProducts() {

        showLoading()

        val single = repository.getProducts()
            .subscribeOn(schedulers.IO)
            .observeOn(schedulers.MAIN)
            .subscribe({ value ->
                hideLoading()
                hideNetworkError()
                if (value.isEmpty()) showNoProducts() else hideNoProducts()

                _result.postValue(Success(value))
            },
                { error ->
                    hideLoading()
                    if (error is IOException) showNetworkError()

                    _result.postValue(Failure<Throwable>(error))
                })

        disposables.add(single)
    }

    fun getProductsByName(name: String) {

        showLoading()

        val single = repository.getProductsByName(name)
            .subscribeOn(schedulers.IO)
            .observeOn(schedulers.MAIN)
            .subscribe({ value ->
                hideLoading()
                hideNetworkError()
                if (value.isEmpty()) showNoProducts() else hideNoProducts()

                _result.postValue(Success(value))
            },
                { error ->
                    hideLoading()
                    if (error is IOException) showNetworkError()

                    _result.postValue(Failure<Throwable>(error))
                })

        disposables.add(single)

    }

    fun setFeaturedProducts(list: List<Product>) {
        if(list.size > 5) _featuredProducts.postValue(list.subList(0, 5))
        else _featuredProducts.postValue(list.subList(0, list.size - 1))
    }

    private fun showLoading() {
        _isLoading.postValue(true)
        hideNetworkError()
    }

    private fun hideLoading() {
        _isLoading.postValue(false)
    }

    private fun showNetworkError() {
        _networkError.postValue(true)
    }

    private fun hideNetworkError() {
        _networkError.postValue(false)
    }

    private fun showNoProducts() {
        _noProductsAvailable.postValue(true)
    }

    private fun hideNoProducts() {
        _noProductsAvailable.postValue(false)
    }

    fun startDealsCountDownTimer() {

        val time = 1000 * 60 * 60 * 24

        if (isCountDownCancelled) {
            isCountDownCancelled = false

            countDownTimer = object : CountDownTimer(time.toLong(), 1000) {

                override fun onTick(millisFeature: Long) {

                    var millis = millisFeature
                    val hours = TimeUnit.MILLISECONDS.toHours(millis)
                    millis -= TimeUnit.HOURS.toMillis(hours)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
                    millis -= TimeUnit.MINUTES.toMillis(minutes)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

                    val map = HashMap<String, String>().apply {
                        put("hours", hours.toString())
                        put("minutes", minutes.toString())
                        put("seconds", seconds.toString())
                    }

                    _dealTimer.postValue(map)
                }

                override fun onFinish() {
                    cancelCountDownTimer()
                }
            }.start()

        }

    }

    private fun cancelCountDownTimer() {
        countDownTimer.cancel()
        isCountDownCancelled = true
    }

    override fun onCleared() {
        super.onCleared()

        cancelCountDownTimer()
        disposables.clear()
    }



}