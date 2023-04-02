package com.orbitalsonic.filemanagementviewmodel.interfaces

import com.orbitalsonic.filemanagementviewmodel.datamodel.FileItem

interface OnFileItemClickListener {
    fun onFileClick(fileItem: FileItem)
    fun onDeleteClick(fileItem: FileItem)
}