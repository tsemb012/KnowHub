package com.example.droidsoftthird.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.droidsoftthird.R
import com.google.android.material.slider.Slider
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imageURI")
fun ImageView.imageURI(imageMap: Map<String, String>?) {
    if (imageMap.isNullOrEmpty()) return
    val key = imageMap.keys.first()
    val image = imageMap.values.first()
    when (key) {
        "REF_FOR_INITIALIZE" -> {
            Glide.with(this)
                .load(image.let { FirebaseStorage.getInstance().getReference(it) })//TODO Transformationで画像の加工処理を行う。
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//Changed from AUTOMATIC to RESOURCE
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(this)
        }
        "URI_FOR_UPDATE" -> Glide.with(this)
            .load(image?.let { Uri.parse(it) })
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//Changed from AUTOMATIC to RESOURCE
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
        else -> {
            Glide.with(this)
                .load(R.drawable.loading_animation)
                .placeholder(R.drawable.ic_baseline_account_box_24)
                .into(this)
        }
    }
}

//DONE GlideでStorageのデータを表示する。
@BindingAdapter("imageFireStorage")
fun ImageView.imageFireStorage(ref: String?) {
    val maxRetries = 3 // 最大再試行回数
    var retries = 0 // 現在の再試行回数

    val glide = Glide.with(this)
        .load(if (ref != null) FirebaseStorage.getInstance().getReference(ref) else R.drawable.loading_animation)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_baseline_image_24)
        )

    glide.listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            isFirstResource: Boolean,
        ): Boolean {
            if (retries < maxRetries) {
                retries++
                glide.into(this@imageFireStorage)
            }
            return false // falseを返すと、error Drawableが表示されます。
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            return false // falseを返すと、読み込みが成功した画像が表示されます。
        }
    }).into(this)
}

@BindingAdapter("imageURI")
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

@BindingAdapter("imageUserURI")
fun ImageView.imageUserURI(uri: URI) {
    Glide.with(this)
        .load(uri)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_baseline_account_box_24)
                .error(R.drawable.ic_broken_image)
        )
        .into(this)
}

@BindingAdapter("imageUserUrlString")
fun ImageView.imageUserUrlString(url: String) {
    val uri = Uri.parse(url)
    Glide.with(this)
        .load(uri)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_baseline_account_box_24)
                .error(R.drawable.ic_broken_image)
        )
        .into(this)
}

@BindingAdapter("prefecture", "city")//TODO 引数が複数の場合のBindingAdapterの記述が上記であっているか検証を行う。
fun TextView.bindArea(prefecture: String, city: String){
    text =
        if(prefecture == resources.getStringArray(R.array.online_and_prefectures)[0].toString()) { prefecture }
        else if ( prefecture != resources.getString(R.string.no_set) && city != resources.getString(R.string.no_set)) { String.format("%s、%s", prefecture, city) }
        else if ( prefecture != resources.getString(R.string.no_set) && city == resources.getString(R.string.no_set)) { prefecture }
        else{ resources.getString(R.string.no_set) }
}

@BindingAdapter("basis", "frequency")
fun TextView.bindBasisFrequency(basis: String, frequency: Int){
    text = if (basis == resources.getString(R.string.no_set) && frequency == -1) {
        resources.getString(R.string.no_set)
    }else if(basis == resources.getString(R.string.everyday)) {
        resources.getString(R.string.everyday)
    }else if(basis == resources.getString(R.string.irregularly) && frequency == -1){
            resources.getString(R.string.irregularly)
    } else {
        val displayBasis = if (basis == resources.getString(R.string.weekly)) {
            resources.getString(R.string.week)
        } else {
            resources.getString(R.string.month)
        }
        String.format("%s %s 回", displayBasis, frequency)
    }
}

@BindingAdapter("minAge", "maxAge")
fun TextView.bindAgeRange(minAge: Int, maxAge: Int){
    text = if (minAge == -1  && maxAge == -1) {
        resources.getString(R.string.no_set)
    } else{
        String.format("%d〜%d才", minAge, maxAge)
    }
}

@BindingAdapter("maxNumberPerson")
fun TextView.bindNumberPerson(minNumberPerson: Int, maxNumberPerson: Int){//TODO DST-520 消す
    text = if (maxNumberPerson == -1) {
        resources.getString(R.string.no_set)
    } else{
        String.format("最大参加人数 %d人", maxNumberPerson)
    }
}

@BindingAdapter("formatDateToHHMM")
fun formatDateToHHMM(textView: TextView, timestamp: Date?) {

    val sdf = SimpleDateFormat("hh:mm")
    textView.text = sdf.format(timestamp)

}

@BindingAdapter("setDuration")
fun setDuration(textView: TextView, timeinmillis: String?) {

    if (timeinmillis == null) return

    val h = (timeinmillis.toInt().div(3600000))
    val m = (timeinmillis.toInt().div(60000).rem(60))
    val s = (timeinmillis.toInt().div(1000).rem(60))

    val sp = when (h) {
        0 -> {
            StringBuilder().append(m).append(":").append(s)
        }
        else -> {
            StringBuilder().append(h).append(":").append(m).append(":").append(s)
        }
    }
    textView.text = sp
}

@InverseBindingAdapter(attribute = "android:value")
fun getSliderValue(slider: Slider) = slider.value

@BindingAdapter( "android:valueAttrChanged")
fun setSliderListeners(slider: Slider, attrChange: InverseBindingListener) {
    slider.addOnChangeListener { _, _, _ ->
        attrChange.onChange()
    }
}


@BindingAdapter("floatToIntString")
fun TextView.floatToIntString(float: Float) {
    text = float.toInt().toString()
}


/** NetworkingStatus表現時に下記のコードを再利用。現状、Glideでも同じような表現はできている。
 * This binding adapter displays the [MarsApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 *

@BindingAdapter("marsApiStatus")
fun bindStatus(statusImageView: ImageView, status: MarsApiStatus?) {
    when (status) {
        MarsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MarsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MarsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}*/

