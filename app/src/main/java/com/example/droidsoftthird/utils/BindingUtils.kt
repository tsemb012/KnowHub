package com.example.droidsoftthird.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.droidsoftthird.R
import java.net.URI



@BindingAdapter("imageURL")
fun ImageView.imageURI(uri: URI) {
    Glide.with(this)
        .load(uri)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
        )
        .into(this)
}

@BindingAdapter("prefecture","city")//TODO 引数が複数の場合のBindingAdapterの記述が上記であっているか検証を行う。
fun TextView.bindArea(prefecture: String, city: String){
    text =
        if (prefecture != resources.getString(R.string.no_set) && city != resources.getString(R.string.no_set)) {
        String.format("%s、%s", prefecture, city)
        }else{
            resources.getString(R.string.no_set)
    }
}

@BindingAdapter("basis","frequency")
fun TextView.bindBasisFrequency(basis: String, frequency: String){
    text = if (basis != resources.getString(R.string.no_set) && frequency != resources.getString(R.string.no_set) && frequency != resources.getString(R.string.everyday)) {
        String.format("%s%s回", basis, frequency)
    } else if(frequency == R.string.everyday.toString()) {
        resources.getString(R.string.everyday)
    } else{
        resources.getString(R.string.no_set)
    }
}

@BindingAdapter("minAge","maxAge")
fun TextView.bindAgeRange(minAge: Int, maxAge:Int){
    text = if (minAge == -1  && maxAge == -1) {
        resources.getString(R.string.no_set)
    } else{
        String.format("%d〜%d才", minAge, maxAge)

    }
}

@BindingAdapter("minNumberPerson","maxNumberPerson")
fun TextView.bindNumberPerson(minNumberPerson: Int, maxNumberPerson:Int){
    text = if (minNumberPerson == -1  && maxNumberPerson == -1) {
        resources.getString(R.string.no_set)
    } else{
        String.format("%d〜%d人", minNumberPerson, maxNumberPerson)
    }
}

