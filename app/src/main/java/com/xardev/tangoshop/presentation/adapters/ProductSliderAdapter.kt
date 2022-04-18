package com.xardev.tangoshop.presentation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.viewpager.widget.PagerAdapter
import com.xardev.tangoshop.R
import com.xardev.tangoshop.databinding.ItemProductSliderBinding
import com.xardev.tangoshop.domain.models.Product

class ProductSliderAdapter(
    val context: Context
) : PagerAdapter() {

    private val list = ArrayList<Product>(emptyList())

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemProductSliderBinding.inflate(LayoutInflater.from(context), container, false)

        val product = list[position]
        binding.product = product

        binding.btnOrderNow.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("product", product)
            }

            Navigation.findNavController(binding.root)
                .navigate(
                    R.id.productDetailsActivity,
                    bundle,
                    null
                )
        }

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return list.count()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object` as View)
    }

    fun updateList(newList: List<Product>){
        list.clear()
        list.addAll(newList)

        notifyDataSetChanged()
    }
}