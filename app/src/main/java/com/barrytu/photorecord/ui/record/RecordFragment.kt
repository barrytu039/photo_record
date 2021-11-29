package com.barrytu.photorecord.ui.record

import android.R.attr
import android.net.Uri
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
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore

import android.content.Intent
import android.os.Build
import android.R.attr.path





class RecordFragment : Fragment() {

    private lateinit var recordViewModel: RecordViewModel
    private var _binding: FragmentRecordBinding? = null

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
                onPhotoAddClicked()
            }
        })

        return root
    }

    fun onPhotoAddClicked() {
        Log.e("state::", "click")

        val rootDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        rootDir?.let { root ->
            activity?.let { ac ->
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val file: File = File(rootDir.path, "resultImage.jpeg")
                val resultUri = FileProvider.getUriForFile(
                    ac, ac.packageName.toString() + ".fileprovider",
                    file
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                if (takePictureIntent.resolveActivity(ac.packageManager) != null) {
                    startActivityForResult(takePictureIntent, 1002)
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002) {

            Log.e("data:", data?.data.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}