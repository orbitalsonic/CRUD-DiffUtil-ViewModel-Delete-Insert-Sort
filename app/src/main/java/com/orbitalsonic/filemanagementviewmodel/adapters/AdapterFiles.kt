package com.orbitalsonic.filemanagementviewmodel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.orbitalsonic.filemanagementviewmodel.R
import com.orbitalsonic.filemanagementviewmodel.databinding.ItemFileBinding
import com.orbitalsonic.filemanagementviewmodel.datamodel.FileItem
import com.orbitalsonic.filemanagementviewmodel.interfaces.OnFileItemClickListener

class AdapterFiles : ListAdapter<FileItem, RecyclerView.ViewHolder>(diffUtilVideos) {

    private var mListener: OnFileItemClickListener? = null

    fun setOnItemClickListener(listener: OnFileItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemFileBinding>(layoutInflater, R.layout.item_file, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val currentItem = getItem(pos)
        val binding = (holder as ViewHolder<*>).globalBinding
        if (binding is ItemFileBinding) {
            binding.apply {
                item = currentItem
                itemClick = mListener
            }
        }
    }

    inner class ViewHolder<T : ViewBinding>(val globalBinding: T) : RecyclerView.ViewHolder(globalBinding.root)

    companion object {
        val diffUtilVideos = object : DiffUtil.ItemCallback<FileItem>() {
            override fun areItemsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
