package dev.smai1e.carTrader.ui.views.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.FragmentTabsBinding

class TabsFragment : Fragment() {

    private var _binding: FragmentTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)
        binding.bottomNavigationView.background = null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { navigateToNewLotFragment() }

        val navHost =
            childFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNewLotFragment() = findNavController().navigate(R.id.action_tabsFragment_to_newLotFragment)
}