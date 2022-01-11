package com.barrytu.photorecord.ui.record

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barrytu.photorecord.tools.SingleClickListener
import com.barrytu.photorecord.databinding.FragmentRecordBinding
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.barrytu.photorecord.ui.camera.CameraActivity
import com.barrytu.photorecord.ui.gallery.GalleryActivity
import com.barrytu.photorecord.MediaBottomSheetDialogFragment

const val PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE = 1001

class RecordFragment : Fragment(), MediaBottomSheetDialogFragment.MediaBottomSheetInterface {

    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null
    private var mediaBottomSheetDialogFragment : MediaBottomSheetDialogFragment? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recordViewModel =
            ViewModelProvider(this)[RecordViewModel::class.java]

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        recordViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        binding.fragmentRecordAddPhotoFloatingActionButton.setOnClickListener(object : SingleClickListener(){
            override fun onSingleClick(view: View?) {
                mediaBottomSheetDialogFragment?.dismiss()
                mediaBottomSheetDialogFragment = MediaBottomSheetDialogFragment.newInstance(this@RecordFragment)
                mediaBottomSheetDialogFragment?.show(childFragmentManager, MediaBottomSheetDialogFragment::class.java.name)
            }
        })

        return root
    }


    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

    }

    private fun onPhotoAddClicked() {
        activity?.let { ac ->
            val cameraIntent = Intent(ac, CameraActivity::class.java)
            cameraResultLauncher.launch(cameraIntent)
        }
    }

    private fun haveCameraPermission() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        if (!haveCameraPermission()) {
            val permissions = arrayOf(
                Manifest.permission.CAMERA
            )
            cameraPermissionReqLauncher.launch(permissions)
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestStoragePermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            storagePermissionReqLauncher.launch(
                permissions
            )
        }
    }

    private val cameraPermissionReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var isAllPermissionGranted = true
        for (i in it.keys) {
            it[i]?.let { isGranted ->
                if (!isGranted) {
                    isAllPermissionGranted = false
                }
            }
        }
        if (isAllPermissionGranted) {
            onCameraMediaClick()
        }
    }

    private val storagePermissionReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var isAllPermissionGranted = true
        for (i in it.keys) {
            it[i]?.let { isGranted ->
                if (!isGranted) {
                    isAllPermissionGranted = false
                }
            }
        }
        if (isAllPermissionGranted) {
            onPhotoAddClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCameraMediaClick() {
        if (haveCameraPermission()) {
            onPhotoAddClicked()
        } else {
            requestCameraPermission()
        }
    }

    override fun onGalleryMediaClick() {
        if (haveStoragePermission()) {
            activity?.let {
                val intent = Intent(it, GalleryActivity::class.java)
                it.startActivity(intent)
            }
        } else {
            requestStoragePermission()
        }
    }
}