package com.barrytu.photorecord

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.barrytu.photorecord.databinding.ActivityGalleryBinding
import com.barrytu.photorecord.ui.record.PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE
import kotlinx.coroutines.launch

class GalleryActivity : AppCompatActivity(), MediaAdapter.MediaItemInterface {

    private lateinit var binding : ActivityGalleryBinding

    private val mediaAdapter : MediaAdapter by lazy {
        MediaAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMediaItem()
        PhotoRecordApplication.mediaRetriever.mediaMutableLiveData.observe(this) {
            if (it.isNullOrEmpty()) {

            } else {
                it.onEach { m ->
                    Log.e("uri::", m.uri.path?:" empty")
                }
                mediaAdapter.setDataSet(it)
            }
        }
        binding.mediaRecyclerView.apply {
            adapter = mediaAdapter
            layoutManager = GridLayoutManager(
                this@GalleryActivity,
                4,
                GridLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
    }

    private fun loadMediaItem() {
        if (haveStoragePermission()) {
            lifecycleScope.launch {
                PhotoRecordApplication.mediaRetriever.scanMediaItem()
            }
        } else {
            requestPermission()
        }
    }


    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    // permission granted
                    loadMediaItem()
                } else {
                    // permission dined
                    Toast.makeText(this, "Permission Dined!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemClick(position: Int) {

    }
}