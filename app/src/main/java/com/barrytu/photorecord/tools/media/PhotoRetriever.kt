package com.barrytu.photorecord.tools.media

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.barrytu.mediastoreretriever.MediaEntity
import com.barrytu.photorecord.PhotoRecordApplication
import com.barrytu.photorecord.R
import java.io.File

class PhotoRetriever(private val contentResolver: ContentResolver) {

    @SuppressLint("Range")
    fun scanItem() : MutableList<MediaEntity> {

        val uriMutableList = mutableListOf<MediaEntity>()

        val uri = collection

        val columns = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.DATE_ADDED
        )

        val orderBy = "DATE_MODIFIED DESC"

        val where = MediaStore.Images.Media.DATE_ADDED + ">" + 0

        val cursor = contentResolver.query(uri, columns, where, null, orderBy)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id              = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                val folderId        = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                val folderName      = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val name            = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                val size            = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                val width           = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                val height          = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                val addedTime       = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)) * 1000L
                val modifierTime    = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)) * 1000L
                val takenTime       = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                val mimeType        = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                val orientation     = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION))
                val imageUri        = ContentUris.withAppendedId(uri, id)
                Log.e("data::", id.toString())
                uriMutableList.add(MediaEntity(imageUri, id, name, mimeType, modifierTime, addedTime))
            } while (cursor.moveToNext())
            cursor.close()
        }
        return uriMutableList
    }

    @SuppressLint("Range")
    fun scanAppDir() : MutableList<MediaEntity> {

        val uriMutableList = mutableListOf<MediaEntity>()
        Log.e("getOutputDirectory()", getOutputDirectory().absolutePath)


        val uri = FileProvider.getUriForFile(PhotoRecordApplication.getAppContext(), "com.barrytu.photorecord.fileprovider", getOutputDirectory())


        val columns = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.DATE_ADDED
        )

        val orderBy = "DATE_MODIFIED DESC"

        val where = MediaStore.Images.Media.DATE_ADDED + ">" + 0

        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, where, null, orderBy)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.e("count", cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)))
                val id              = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                val folderId        = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                val folderName      = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val name            = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                val size            = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                val width           = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                val height          = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                val addedTime       = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)) * 1000L
                val modifierTime    = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)) * 1000L
                val takenTime       = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                val mimeType        = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                val orientation     = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION))
                val imageUri        = ContentUris.withAppendedId(uri, id)
                uriMutableList.add(MediaEntity(imageUri, id, name, mimeType, modifierTime, addedTime))
            } while (cursor.moveToNext())
            cursor.close()
        }
        Log.e("size::", uriMutableList.size.toString() + "")
        return uriMutableList
    }

    private fun getOutputDirectory(): File {
        val mediaDir = PhotoRecordApplication.getAppContext().externalMediaDirs.firstOrNull()?.let {
            File(it, PhotoRecordApplication.getAppContext().resources.getString(R.string.app_name) + "/").apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else PhotoRecordApplication.getAppContext().filesDir
    }

    private val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
}