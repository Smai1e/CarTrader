package dev.smai1e.carTrader.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.FragmentHomeBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.utils.findTopNavController
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initToolbarMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(this, pagesList)
        binding.homeViewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.homeViewPager) { tab, pos ->
            tab.text = getString(pagesList[pos].titleId)
        }.attach()
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.profile -> {
                        navigateToProfileFragment()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun navigateToProfileFragment() = findTopNavController().navigate(R.id.profileFragment)
}