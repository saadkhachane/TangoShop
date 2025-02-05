package com.xardev.tangoshop.presentation.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.xardev.tangoshop.R
import com.xardev.tangoshop.databinding.ActivityProductDetailsBinding
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.presentation.adapters.ImagesSliderAdapter

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    private val imagesSliderAdapter : ImagesSliderAdapter by lazy {
        ImagesSliderAdapter(this)
    }

    lateinit var product: Product

    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)

        product = intent.extras?.getSerializable("product") as Product

        setClickListeners()

        binding.lifecycleOwner = this
        binding.product = product

        setupImagesSlider()

    }

    private fun setupImagesSlider() {
        binding.viewPager.adapter = imagesSliderAdapter
        imagesSliderAdapter.updateList(product.images)
    }

    private fun setClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.quantity.text = quantity.toString()

                val newPrice = "$${product.gross_price.toFloat() * quantity}"
                binding.price.text = newPrice
            }
        }

        binding.btnPlus.setOnClickListener {
            quantity++
            binding.quantity.text = quantity.toString()

            val newPrice = "$${product.gross_price.toFloat() * quantity}"
            binding.price.text = newPrice
        }

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, "Product added to your cart.", Toast.LENGTH_LONG).show()
        }
    }
}