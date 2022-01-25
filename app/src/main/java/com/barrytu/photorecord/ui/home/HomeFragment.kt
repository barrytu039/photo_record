package com.barrytu.photorecord.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.barrytu.photorecord.PhotoRecordApplication
import com.barrytu.photorecord.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _viewBinding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val viewBinding get() = _viewBinding!!

    private lateinit var photoRecordListAdapter: PhotoRecordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoRecordListAdapter = PhotoRecordListAdapter()
        viewBinding.fragmentHomePhotoRecordRecycleView.apply {
            adapter = photoRecordListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        activity?.let { ac ->
            homeViewModel =
                ViewModelProvider(this, HomeViewModelFactory(ac.application, PhotoRecordApplication.photoRecordRepository))[HomeViewModel::class.java]
            homeViewModel.photoRecordList.observe(viewLifecycleOwner, {
                photoRecordListAdapter.updatePhotoRecordDataSet(it)
                it.onEach { p ->

                    Log.e("data::", p.toString())
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}