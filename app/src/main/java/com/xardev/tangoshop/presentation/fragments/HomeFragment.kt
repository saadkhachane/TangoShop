package com.xardev.tangoshop.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.xardev.tangoshop.R
import com.xardev.tangoshop.databinding.FragmentHomeBinding
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.domain.schedulers.DefaultSchedulers
import com.xardev.tangoshop.presentation.adapters.ProductRecyclerAdapter
import com.xardev.tangoshop.presentation.adapters.ProductSliderAdapter
import com.xardev.tangoshop.presentation.viewModels.MainViewModel
import com.xardev.tangoshop.utils.Result.Failure
import com.xardev.tangoshop.utils.Result.Success
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val recyclerAdapter: ProductRecyclerAdapter by lazy {
        ProductRecyclerAdapter(requireContext())
    }

    private val sliderAdapter by lazy {
        ProductSliderAdapter(requireContext())
    }

    private var sliderRotationDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupProductSlider()
        setupRecycler()
        setClickListeners()
        setSearchListener()
        setupObservers()
        setupSwipeRefreshListener()

        return binding.root
    }

    private fun setupSwipeRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }

    }

    private fun setupProductSlider() {
        binding.viewPager.adapter = sliderAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager, true)
        setAutoRotation()
    }

    private fun setAutoRotation() {
        if (sliderRotationDisposable == null)
            sliderRotationDisposable = Observable.interval(0, 2, TimeUnit.SECONDS)
                .subscribeOn(DefaultSchedulers.IO)
                .observeOn(DefaultSchedulers.MAIN)
                .subscribe({
                    if (binding.viewPager.currentItem < 4) {
                        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
                    } else {
                        binding.viewPager.currentItem = 0
                    }
                }, {})
    }

    private fun setSearchListener() {
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.getProductsByName(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setClickListeners() {
        binding.btnRefresh.setOnClickListener {
            refresh()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) {
            it?.let {

                when (it) {
                    is Success -> {
                        showProductSlider()
                        showDealsTimer()
                        showRecyclerView()

                        recyclerAdapter.updateList(it.value as List<Product>)
                        viewModel.startDealsCountDownTimer()
                    }
                    is Failure -> {
                        hideProductSlider()
                        hideDealsTimer()
                        hideRecyclerView()
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel.featuredProducts.observe(viewLifecycleOwner) {
            it?.let {
                sliderAdapter.updateList(it)
            }
        }

        viewModel.dealTimer.observe(viewLifecycleOwner) {
            it?.let {
                binding.dealsTimer.timerHour.text = it["hours"]
                binding.dealsTimer.timerMins.text = it["minutes"]
                binding.dealsTimer.timerSeconds.text = it["seconds"]
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
    }

    private fun refresh() {
        if (binding.search.text.isNullOrBlank())
            viewModel.getProducts()
        else
            viewModel.getProductsByName(binding.search.text.toString())
    }

    private fun setupRecycler() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = recyclerAdapter
    }

    private fun showDealsTimer() {
        TransitionManager.beginDelayedTransition(binding.constraintLayout)
        binding.dealsTimer.layout.visibility = View.VISIBLE
    }

    private fun hideDealsTimer() {
        TransitionManager.beginDelayedTransition(binding.constraintLayout)
        binding.dealsTimer.layout.visibility = View.GONE
    }

    private fun showRecyclerView() {
        binding.recycler.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.recycler.visibility = View.GONE
    }

    private fun showProductSlider() {
        TransitionManager.beginDelayedTransition(binding.constraintLayout)
        binding.featuredProductsCard.visibility = View.VISIBLE
    }

    private fun hideProductSlider() {
        TransitionManager.beginDelayedTransition(binding.constraintLayout)
        binding.featuredProductsCard.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderRotationDisposable?.dispose()
    }

}