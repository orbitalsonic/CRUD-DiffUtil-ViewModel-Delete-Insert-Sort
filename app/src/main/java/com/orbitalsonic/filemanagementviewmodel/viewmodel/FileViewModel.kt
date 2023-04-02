package com.orbitalsonic.filemanagementviewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.github.javafaker.Faker
import com.orbitalsonic.filemanagementviewmodel.datamodel.FileItem
import com.orbitalsonic.filemanagementviewmodel.enums.SortingOrder
import com.orbitalsonic.filemanagementviewmodel.enums.SortingType
import kotlinx.coroutines.*
import java.util.*

class FileViewModel : ViewModel() {

    /**
     * Indicates whether files are currently being fetched or not.
     * @param isFileFetching True if files are being fetched, false otherwise.
     */
    private var isFileFetching = false

    /**
     * Determines if files are currently being sorted or not.
     * @param isFilesSorting True if files are being sorted, false otherwise.
     */
    private var isFilesSorting = false

    /**
     * Handles exceptions that occur during background processes such as fetching files, deleting files, or sorting.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        isFileFetching = false
        isFilesSorting = false
        setFileUpdated(false)
        Log.e("FileViewModelTAG", "${exception.message}")
    }

    /**
     * Handles the variables and functions related to LiveData updates for files.
     */
    private val _fileUpdate = MutableLiveData<Boolean>()
    var fileUpdate: LiveData<Boolean> = _fileUpdate.map { it }

    /**
     * Sets the LiveData value for file updates to the specified boolean value.
     * @param update The boolean value to set as the LiveData value.
     */
    fun setFileUpdated(update: Boolean) {
        _fileUpdate.postValue(update)
    }

    /**
     * The original list of files that cannot be changed outside of the view model.
     * @param originalFileList list of files to be used within the view model.
     *
     * @param fileList to be used outside of the view model. This list can be accessed using the `getFileList()` function.
     * It is up to you and your use case whether you use this parameter or the `getFileList()` function, as they both have the same functionality.
     */
    private var originalFileList = ArrayList<FileItem>()
    val filesList: List<FileItem> get() = originalFileList.toList()

    fun getFileList(): MutableList<FileItem> {
        val list = mutableListOf<FileItem>()
        list.addAll(originalFileList)
        return list
    }


    /**
     * If a context is needed in the ViewModel, use AndroidViewModel to provide it.
     *
     * The sorting order to be applied.
     * @param sortingOrder The sorting order to be used.
     *
     * The sorting type to be applied.
     * @param sortingType The sorting type to be used.
     *
     * If you don't need any of these variables initially, you can remove them.
     */
    fun fetchingFiles(sortingType: SortingType, sortingOrder: SortingOrder) {
        val loadedFileList = ArrayList<FileItem>()
        if (!isFileFetching) {
            isFileFetching = true
            if (isFilesListEmpty()) {
                viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
                    async(Dispatchers.IO + exceptionHandler) {
                        for (index in 0..50) {
                            loadedFileList.add(
                                FileItem(
                                    fileName = randomName(),
                                    fileDescription = randomName()
                                )
                            )
                        }
                    }.await()
                    isFileFetching = false
                    clearFileListData()
                    originalFileList.addAll(loadedFileList)
                    setFileUpdated(true)
                }
            } else {
                isFileFetching = false
                setFileUpdated(true)
            }
        }
    }

    /**
     * Deletes an item from the original list by passing in the item as a parameter.
     */
    fun deleteFile(fileItem: FileItem) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            async(Dispatchers.IO + exceptionHandler) {
                originalFileList.remove(fileItem)
            }.await()
            setFileUpdated(true)
        }
    }

    /**
     * Insert an item at position 1 in original list by passing in the item as a parameter.
     */
    fun insertFile(fileItem: FileItem) {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            originalFileList.add(0, fileItem)
            setFileUpdated(true)
        }
    }

    /**
     * Sorts the original list by applying the specified sorting type and order.
     * @param sortingType The type of sorting to be applied.
     * @param sortingOrder The order in which the list should be sorted.
     */
    fun sortFiles(sortingType: SortingType, sortingOrder: SortingOrder) {
        var sortedFileList = ArrayList<FileItem>()
        if (!isFilesSorting) {
            isFilesSorting = true
            viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
                async(Dispatchers.IO + exceptionHandler) {
                    when (sortingType) {
                        SortingType.NAME -> {
                            when (sortingOrder) {
                                SortingOrder.DESC -> {
                                    sortedFileList = originalFileList.sortedByDescending { it.fileName }.toCollection(ArrayList())
                                }
                                SortingOrder.ASC -> {
                                    sortedFileList = originalFileList.sortedBy { it.fileName }.toCollection(ArrayList())
                                }
                            }
                        }
                        else -> {}
                    }

                    clearFileListData()
                    originalFileList.addAll(sortedFileList)
                }.await()
                isFilesSorting = false
                setFileUpdated(true)
            }
        }
    }

    /**
     * Checks if the original list is empty.
     */
    fun isFilesListEmpty() = originalFileList.isEmpty()

    /**
     * Clears the original list, making it empty.
     */
    fun clearFileListData() = originalFileList.clear()

    /**
     * Generates a random name using the Faker library.
     */
    fun randomName(): String {
        return Faker().name().fullName()
    }
}