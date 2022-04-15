package com.xardev.tangoshop.presentation.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.domain.repositories.ProductRepository
import com.xardev.tangoshop.domain.schedulers.TestSchedulers
import com.xardev.tangoshop.utils.Result
import com.xardev.tangoshop.utils.exceptionOrNull
import com.xardev.tangoshop.utils.getOrNull
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.io.IOException

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

    private val products = getProductsList()

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(repository, TestSchedulers)
    }

    @Test
    fun `getProducts() Updates isLoading to true then to false`() {
        // Arrange
        `when`(repository.getProducts())
            .thenAnswer { Single.just(emptyList<Product>()) }

        viewModel.isLoading.observeForever(observer)

        // Act
        viewModel.getProducts()

        // Assert
        verify(observer, atLeastOnce()).onChanged(true)
        verify(observer, atLeastOnce()).onChanged(false)
    }

    @Test
    fun `getProducts() Given EmptyList Updates noProductsAvailable to true`() {
        // Arrange
        `when`(repository.getProducts())
            .thenReturn(Single.just(emptyList()))

        viewModel.noProductsAvailable.observeForever(observer)

        // Act
        viewModel.getProducts()

        // Assert
        verify(observer, atLeastOnce()).onChanged(true)
    }

    @Test
    fun `getProducts() Given Products List Updates noProductsAvailable to false`() {
        // Arrange
        `when`(repository.getProducts())
            .thenAnswer { Single.just(getProductsList()) }

        viewModel.noProductsAvailable.observeForever(observer)

        // Act
        viewModel.getProducts()

        // Assert
        verify(observer, atLeastOnce()).onChanged(false)
    }

    @Test
    fun `getProducts() Given Products List Returns Success Result with products`() {
        // Arrange
        `when`(repository.getProducts())
            .thenAnswer { Single.just(products) }

        viewModel.result.observeForever(observer)

        // Act
        viewModel.getProducts()

        // Assert
        val r = viewModel.result.value
        assert(r is Result.Success)
        assert(r?.getOrNull() == products)

    }

    @Test
    fun `getProducts() With Random Exception Returns Failure Result with Exception`() {
        // Arrange
        `when`(repository.getProducts())
            .thenAnswer { Single.error<Throwable>(Exception()) }

        viewModel.result.observeForever(observer)

        // Act
        viewModel.getProducts()

        // Assert
        val r = viewModel.result.value
        assert(r is Result.Failure)
        assert(r?.exceptionOrNull() is Exception)
    }

    @Test
    fun `getProducts() With No Internet returns failure & updates networkError to true`() {
        // Arrange
        `when`(repository.getProducts())
            .thenAnswer { Single.error<Throwable>(IOException()) }

        viewModel.networkError.observeForever(observer)

        // Act
        viewModel.getProducts()

        // Assert
        val r = viewModel.result.value
        assert(r is Result.Failure)
        assert(r?.exceptionOrNull() is IOException)
        verify(observer, atLeastOnce()).onChanged(true)
    }

    @Test
    fun `getProductsByName() Given Empty response Returns Success with EmptyList`() {
        // Arrange
        val name = "Random Name"

        `when`(repository.getProductsByName(name))
            .thenAnswer { Single.just(emptyList<Product>()) }

        viewModel.result.observeForever(observer)

        // Act
        viewModel.getProductsByName(name)

        // Assert
        val r = viewModel.result.value
        assert(r is Result.Success)
        assertEquals(r?.getOrNull(), emptyList<Product>())
    }

    @Test
    fun `getProductsByName() Given Products List Returns Success with the Given list`() {
        // Arrange
        val name = "dobra"
        val list = getProductsListByName(name)

        `when`(repository.getProductsByName(name))
            .thenAnswer { Single.just(list) }

        viewModel.result.observeForever(observer)

        // Act
        viewModel.getProductsByName(name)

        // Assert
        val r = viewModel.result.value
        assert(r is Result.Success)
        assertEquals(r?.getOrNull(), list)
    }



    private fun getProductsList() : List<Product>{
        val file = File("src\\main\\res\\raw\\products.json")
        val json = file.bufferedReader().use { it.readText() }
        return Gson().fromJson(json, Array<Product>::class.java).asList()
    }

    private fun getProductsListByName(name: String) : List<Product>{
        return products.filter { it.product_name.contains(name) }
    }

}