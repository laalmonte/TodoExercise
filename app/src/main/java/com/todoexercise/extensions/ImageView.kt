package com.todoexercise.extensions

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.todoexercise.R


fun AppCompatImageView.loadUrlRounded(imageUrl: String?) {
    if (imageUrl == null || imageUrl.isEmpty()) return

    Glide.with(context)
        .load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .apply(RequestOptions().transforms(RoundedCorners(24)))
        .into(this)
}

fun AppCompatImageView.loadUrls(mContext: Context, imageUrl: String?) {
    if (imageUrl == null || imageUrl.isEmpty()) return

    Glide.with(context)
        .load(imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_no_image))
        .into(this)
}