package com.barrytu.photorecord.ui.record

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barrytu.photorecord.tools.SingleClickListener
import com.barrytu.photorecord.databinding.FragmentRecordBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.barrytu.photorecord.ui.camera.CameraActivity
import com.barrytu.photorecord.ui.gallery.GalleryActivity
import com.barrytu.photorecord.MediaBottomSheetDialogFragment
import com.barrytu.photorecord.R
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

const val PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE = 1001

@AndroidEntryPoint
class RecordFragment : Fragment(), MediaBottomSheetDialogFragment.MediaBottomSheetInterface, TextWatcher {

    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null
    private var mediaBottomSheetDialogFragment : MediaBottomSheetDialogFragment? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var recordViewModelFactory: RecordViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recordViewModel =
            ViewModelProvider(this, recordViewModelFactory)[RecordViewModel::class.java]

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fragmentRecordImageView.setOnClickListener(object : SingleClickListener(){
            override fun onSingleClick(view: View?) {
                mediaBottomSheetDialogFragment?.dismiss()
                mediaBottomSheetDialogFragment = MediaBottomSheetDialogFragment.newInstance(this@RecordFragment)
                mediaBottomSheetDialogFragment?.show(childFragmentManager, MediaBottomSheetDialogFragment::class.java.name)
            }
        })

        binding.fragmentRecordCostEditTextView.addTextChangedListener(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recordViewModel.record.observe(viewLifecycleOwner, { record ->
            record?.let {
                activity?.let { ac ->
                    record.photoUri?.let { uriString ->
                        try {
                            Glide.with(ac)
                                .load(Uri.parse(uriString))
                                .into(binding.fragmentRecordImageView)
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                        binding.fragmentRecordImageView.setBackgroundColor(ContextCompat.getColor(ac, R.color.black))
                    }?: run {
                        try {
                            Glide.with(ac)
                                .load(R.drawable.ic_baseline_add_36)
                                .into(binding.fragmentRecordImageView)
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                        binding.fragmentRecordImageView.setBackgroundColor(ContextCompat.getColor(ac, R.color.white))
                    }
                    if (record.cost == null) {
                        binding.fragmentRecordCostEditTextView.setText("")
                    }
                }
                if (it.isRecordCompleted()) {
                    binding.fragmentRecordAddButton.isEnabled = true
                    binding.fragmentRecordAddButton.setOnClickListener(object : SingleClickListener() {
                        override fun onSingleClick(view: View?) {
                            recordViewModel.insertData()
                        }
                    })
                } else {
                    binding.fragmentRecordAddButton.isEnabled = false
                    binding.fragmentRecordAddButton.setOnClickListener(null)
                }
            }?:run {
                recordViewModel.setPhotoRecordImageUri(null)
            }
        })
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
            onGalleryMediaClick()
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

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.let { intent ->
            intent.data?.let { uri ->
                uri.path?.let { path ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        activity?.let { ac ->
                            recordViewModel.setPhotoRecordImageUri(uri)
                        }
                    }
                }
            }
        }
    }

    override fun onGalleryMediaClick() {
        if (haveStoragePermission()) {
            activity?.let {
                val intent = Intent(it, GalleryActivity::class.java)
                galleryResultLauncher.launch(intent)
            }
        } else {
            requestStoragePermission()
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        p0?.let {
            if (it.isNotEmpty()) {
                if (TextUtils.isDigitsOnly(p0)) {
                    recordViewModel.setPhotoRecordCost(p0.toString())
                }
            } else {
                recordViewModel.setPhotoRecordCost(null)
            }
        }
    }
}