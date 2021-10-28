package com.example.droidsoftthird

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IndexOutOfBoundsException




class HomeViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment){

    companion object {
        private const val PAGE_COUNT = 2
        private const val RECOMMEND_PAGE_INDEX = 0
        private const val MAP_PAGE_INDEX = 1
    }

    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        RECOMMEND_PAGE_INDEX to {RecommendPagerFragment()},
        MAP_PAGE_INDEX to {MapPagerFragment()}
    )

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

}