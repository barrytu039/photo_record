package com.barrytu.photorecord.ui.record

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.barrytu.photorecord.SingleClickListener
import com.barrytu.photorecord.databinding.FragmentRecordBinding
import java.io.File
import androidx.core.content.FileProvider
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.barrytu.photorecord.CameraActivity
import com.barrytu.photorecord.GalleryActivity
import com.barrytu.photorecord.MediaBottomSheetDialogFragment

const val PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE = 1001
const val PERMISSION_REQ_CODE_CAMERA = 1002
const val INTENT_REQ_CODE_CAMERA = 1003

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
            ViewModelProvider(this).get(RecordViewModel::class.java)

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        recordViewModel.text.observe(viewLifecycleOwner, Observer {
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

    private fun onPhotoAddClicked() {
//        val rootDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        rootDir?.let { root ->
//            activity?.let { ac ->
//                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////                val file: File = File(rootDir.path, "resultImage.jpeg")
////                val resultUri = FileProvider.getUriForFile(
////                    ac, ac.packageName.toString() + ".fileprovider",
////                    file
////                )
////                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
////                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
////                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                if (takePictureIntent.resolveActivity(ac.packageManager) != null) {
//                    startActivityForResult(takePictureIntent, 1002)
//                }
//            }
//        }
        activity?.let { ac ->
            val cameraIntent = Intent(ac, CameraActivity::class.java)
            startActivity(cameraIntent)
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
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQ_CODE_CAMERA)
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestStoragePermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTENT_REQ_CODE_CAMERA) {
            data?.let {
                it.data?.let {uri ->
                    Log.e("data::", uri.toString())
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQ_CODE_CAMERA -> {
                Log.e("data::", "camera")
            }
            PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE -> {
                Log.e("data::", "storage")
            }
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
            // todo : open custom pick gallery page
            activity?.let {
                val intent = Intent(it, GalleryActivity::class.java)
                it.startActivity(intent)
            }
        } else {
            requestStoragePermission()
        }
    }
}