package dev.smai1e.carTrader.ui.views.photoView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dev.smai1e.carTrader.databinding.FragmentPhotoViewBinding
import dev.smai1e.carTrader.ui.views.photoView.adapter.PhotoViewSliderAdapter

class PhotoViewFragment : Fragment() {

    private var _binding: FragmentPhotoViewBinding? = null
    private val binding get() = _binding!!

    private val args: PhotoViewFragmentArgs by navArgs()
    private val viewPagerAdapter = PhotoViewSliderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewPager() {
        viewPagerAdapter.urlList = args.urlList.toList()
        binding.viewPagerImageSlider.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setCurrentItem(args.position, false)
        }
    }
}