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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.xardev.tangoshop.R
import com.xardev.tangoshop.databinding.FragmentHomeBinding
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.presentation.adapters.ProductRecyclerAdapter
import com.xardev.tangoshop.presentation.viewModels.MainViewModel
import com.xardev.tangoshop.utils.Result.Failure
import com.xardev.tangoshop.utils.Result.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val adapter: ProductRecyclerAdapter by lazy {
        ProductRecyclerAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setClickListeners()
        setSearchListener()
        setupRecycler()
        setupObservers()

        return binding.root
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
        binding.btnOrderNow.setOnClickListener {
            if (viewModel.featuredProduct.value != null) {
                val bundle = Bundle().apply {
                    putSerializable("product", viewModel.featuredProduct.value)
                }

                Navigation.findNavController(binding.root)
                    .navigate(
                        R.id.productDetailsActivity,
                        bundle,
                        null
                    )
            }

        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setupObservers() {
        viewModel.result.observe(viewLifecycleOwner) {
            it?.let {

                when (it) {
                    is Success -> {
                        viewModel.showFeaturedProducts(it.value as List<Product>)
                        binding.recycler.visibility = View.VISIBLE
                        adapter.updateList(it.value)
                    }
                    is Failure -> {
                        binding.recycler.visibility = View.GONE
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel.featuredProduct.observe(viewLifecycleOwner) { product ->
            product?.let {

                viewModel.startDealsCountDownTimer()
                if (binding.featuredProductsCard.isShown.not()) showFeaturedProductsCard()

                val price = "$${product.gross_price}"
                binding.featuredPrice.text = price

                Glide.with(this)
                    .load(product.images[0].image)
                    .into(binding.featuredImage)
            }
        }

        viewModel.dealTimer.observe(viewLifecycleOwner) {
            it?.let {
                binding.dealsTimer.timerHour.text = it["hours"]
                binding.dealsTimer.timerMins.text = it["minutes"]
                binding.dealsTimer.timerSeconds.text = it["seconds"]
            }
        }
    }

    private fun showFeaturedProductsCard() {
        TransitionManager.beginDelayedTransition(binding.constraintLayout)
        binding.featuredProductsCard.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        //refresh()
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
        binding.recycler.adapter = adapter
    }

}