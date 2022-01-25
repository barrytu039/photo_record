package com.barrytu.photorecord.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.barrytu.photorecord.MediaAdapter
import com.barrytu.photorecord.PhotoRecordApplication
import com.barrytu.photorecord.databinding.ActivityGalleryBinding
import com.barrytu.photorecord.ui.record.PERMISSION_REQ_CODE_READ_EXTERNAL_STORAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity(), MediaAdapter.MediaItemInterface {

    private lateinit var viewBind : ActivityGalleryBinding

    private val mediaAdapter : MediaAdapter by lazy {
        MediaAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        loadMediaItem()
        PhotoRecordApplication.mediaRetriever.mediaMutableLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                // todo: handle empty state
            } else {
                mediaAdapter.setDataSet(it)
            }
        }
        viewBind.mediaRecyclerView.apply {
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
        intent.data = mediaAdapter.mediaItems[position].uri
        setResult(RESULT_OK, intent)
        finish()
    }
}