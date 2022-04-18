package com.xardev.tangoshop.presentation.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, imageUrl: String) {

        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)

    }
}
