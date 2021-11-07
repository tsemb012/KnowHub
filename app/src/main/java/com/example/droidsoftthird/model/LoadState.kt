package com.example.droidsoftthird.model

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job

sealed class LoadState {
    object Initialized :LoadState()
    data class Loaded<out T: Any>(val value: T): LoadState()
    class Loading(val job: Job): LoadState()
    class Error(val error: Throwable): LoadState()

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> getValueOrNull(): T? = if (this is Loaded<*>) value as T else null
    fun getErrorOrNull(): Throwable? = if (this is Error) error else null
}

val MutableLiveData<LoadState>.loading get() = value.let { it is LoadState.Loading } // Loadingの型かどうか確認して、真偽値を返す。
val MutableLiveData<LoadState>.loaded get() = value.let { it is LoadState.Loaded<*> }
val MutableLiveData<LoadState>.error get() = value?.getErrorOrNull()