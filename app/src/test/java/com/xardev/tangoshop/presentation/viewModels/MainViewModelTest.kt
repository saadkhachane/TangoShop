package com.xardev.tangoshop.presentation.viewModels

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.domain.repositories.ProductRepository
import com.xardev.tangoshop.domain.schedulers.TestSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subscribers.TestSubscriber
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest : TestCase() {


    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: ProductRepository

    @Mock
    lateinit var observer: Observer<Any>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(repository, TestSchedulers)
    }


    @Test
    fun `getProducts() changes LoadingVisibility to Visible then Gone`() {
        Mockito.`when`(repository.getProducts())
            .thenAnswer { Single.just(emptyList<Product>()) }

        viewModel.loadingVisibility.observeForever(observer)

        viewModel.getProducts()

        Mockito.verify(observer, times(1)).onChanged(View.VISIBLE)
        Mockito.verify(observer, times(1)).onChanged(View.GONE)
    }

    @Test
    fun `getProducts() with EmptyList changes noProductsVisibility to Visible`() {
        Mockito.`when`(repository.getProducts())
            .thenReturn(Single.just(emptyList()))

        viewModel.noProductsVisibility.observeForever(observer)

        viewModel.getProducts()

        Mockito.verify(observer, times(1)).onChanged(View.VISIBLE)
    }

    @Test
    fun `getProducts() with Products List changes noProductsVisibility to GONE`() {
        Mockito.`when`(repository.getProducts())
            .thenAnswer { Single.just(getProductsList()) }

        viewModel.noProductsVisibility.observeForever(observer)

        viewModel.getProducts()

        Mockito.verify(observer).onChanged(View.GONE)
    }


    private fun getProductsList() : List<Product>{
        val file = File("src\\main\\res\\raw\\products.json")
        val json = file.bufferedReader().use { it.readText() }
        return Gson().fromJson(json, Array<Product>::class.java).asList()
    }

}