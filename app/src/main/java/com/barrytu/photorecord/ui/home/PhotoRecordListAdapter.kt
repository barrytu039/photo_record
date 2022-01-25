package com.barrytu.photorecord.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barrytu.photorecord.R
import com.barrytu.photorecord.databinding.ItemPhotoRecordLargeBinding
import com.barrytu.photorecord.db.photorecord.PhotoRecordEntity
import com.bumptech.glide.Glide

class PhotoRecordListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photoRecordDataSet = mutableListOf<PhotoRecordEntity>()

    fun updatePhotoRecordDataSet(newDataSet : List<PhotoRecordEntity>) {
        photoRecordDataSet.clear()
        photoRecordDataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoRecordLargeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_record_large, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoRecordLargeViewHolder) {
            holder.bindData(photoRecordEntity = photoRecordDataSet[position])
        }
    }

    override fun getItemCount(): Int {
        return photoRecordDataSet.size
    }

    inner class PhotoRecordLargeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewBind : ItemPhotoRecordLargeBinding = ItemPhotoRecordLargeBinding.bind(itemView)

        fun bindData(photoRecordEntity: PhotoRecordEntity) {
            Glide.with(itemView.context)
                .load(Uri.parse(photoRecordEntity.photoUri))
                .into(viewBind.itemPhotoRecordLargeImageView)
        }
    }
}