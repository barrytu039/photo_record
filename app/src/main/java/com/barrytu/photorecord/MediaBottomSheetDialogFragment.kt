package com.barrytu.photorecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barrytu.photorecord.databinding.BottomSheetMediaSelectBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MediaBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetMediaSelectBinding

    private var mediaBottomSheetInterface : MediaBottomSheetInterface? = null

    companion object {
        fun newInstance(mediaBottomSheetInterface : MediaBottomSheetInterface) : MediaBottomSheetDialogFragment {
            val bottomSheetFragment = MediaBottomSheetDialogFragment()
            bottomSheetFragment.mediaBottomSheetInterface = mediaBottomSheetInterface
            return bottomSheetFragment;
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMediaSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentMediaSelectBottomSheetCameraLayout.setOnClickListener {
            mediaBottomSheetInterface?.onCameraMediaClick()
            dismiss()
        }

        binding.fragmentMediaSelectBottomSheetGalleryLayout.setOnClickListener {
            mediaBottomSheetInterface?.onGalleryMediaClick()
            dismiss()
        }
    }

    interface MediaBottomSheetInterface {
        fun onCameraMediaClick()
        fun onGalleryMediaClick()
    }
}