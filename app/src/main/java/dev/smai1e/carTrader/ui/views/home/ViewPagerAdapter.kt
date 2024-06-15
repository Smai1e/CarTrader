package dev.smai1e.carTrader.ui.views.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(f: Fragment, private val pages: List<Page>) : FragmentStateAdapter(f) {
    override fun getItemCount() = pages.size

    override fun createFragment(position: Int) = pages[position].fragment
}