package com.example.droidsoftthird.extentions_depreciated

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

fun View.gs(@StringRes id: Int): String = context.getString(id)

fun View.gs(@StringRes id: Int, vararg obj: Any): String = context.getString(id, *obj)

fun View.gd(@DrawableRes id: Int): Drawable? = context.getDrawable(id)

