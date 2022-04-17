package com.xardev.tangoshop.presentation.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xardev.tangoshop.R
import com.xardev.tangoshop.databinding.ItemProductPromoBinding
import com.xardev.tangoshop.domain.models.Product
import com.xardev.tangoshop.utils.MyDiffUtil

class ProductRecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<ProductRecyclerAdapter.ProductsViewHolder>() {

    private var list = emptyList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = DataBindingUtil.inflate<ItemProductPromoBinding>(
            LayoutInflater.from(context), R.layout.item_product_promo, parent, false
        )

        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        if (list.isNotEmpty()) {

            val product = list[position]
            val binding = holder.binding

            binding.product = product

            Glide.with(context)
                .load(product.images[0].image)
                .into(binding.image)

            binding.productCard.setOnClickListener {
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

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<Product>) {
        val diffUtil = MyDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = newList

        diffResult.dispatchUpdatesTo(this)
    }

    class ProductsViewHolder(var binding: ItemProductPromoBinding) :
        RecyclerView.ViewHolder(binding.root)
}