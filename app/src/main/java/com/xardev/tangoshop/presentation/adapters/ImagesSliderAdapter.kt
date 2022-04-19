package com.xardev.tangoshop.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.xardev.tangoshop.databinding.ItemImagesSliderBinding
import com.xardev.tangoshop.domain.models.ProductImage

class ImagesSliderAdapter(
    val context: Context
) : PagerAdapter() {

    private val list = ArrayList<ProductImage>(emptyList())

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemImagesSliderBinding.inflate(LayoutInflater.from(context), container, false)

        val image = list[position]
        binding.productImage = image
        binding.size = list.size

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

    fun updateList(newList: List<ProductImage>){
        list.clear()
        list.addAll(newList)

        notifyDataSetChanged()
    }
}