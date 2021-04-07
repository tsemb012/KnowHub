package com.example.droidsoftthird

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IndexOutOfBoundsException

const val PAGE_COUNT = 3
const val RECOMMEND_PAGE_INDEX = 0
const val SCHEDULE_PAGE_INDEX = 1
const val MAP_PAGE_INDEX = 2


class HomeViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment){

    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        RECOMMEND_PAGE_INDEX to {RecommendPagerFragment()},
        SCHEDULE_PAGE_INDEX to {SchedulePagerFragment()},
        MAP_PAGE_INDEX to {MapPagerFragment()}
    )

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

}