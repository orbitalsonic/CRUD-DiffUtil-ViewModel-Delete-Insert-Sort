package com.orbitalsonic.filemanagementviewmodel.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.orbitalsonic.filemanagementviewmodel.R
import com.orbitalsonic.filemanagementviewmodel.adapters.AdapterFiles
import com.orbitalsonic.filemanagementviewmodel.databinding.ActivityMainBinding
import com.orbitalsonic.filemanagementviewmodel.datamodel.FileItem
import com.orbitalsonic.filemanagementviewmodel.enums.SortingOrder
import com.orbitalsonic.filemanagementviewmodel.enums.SortingType
import com.orbitalsonic.filemanagementviewmodel.interfaces.OnFileItemClickListener
import com.orbitalsonic.filemanagementviewmodel.koin.DIComponent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mAdapter by lazy { AdapterFiles() }

    private val diComponent = DIComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initRecyclerView()
        onClickMethod()
        fetchingFiles()
        initObserverViewModel()
    }

    private fun onClickMethod() {
        binding.btnInsertion.setOnClickListener {
            diComponent.fileViewModel.apply {
                insertFile(
                    FileItem(
                        fileName = randomName(),
                        fileDescription = randomName()
                    )
                )
            }
        }

        binding.btnSortAZ.setOnClickListener {
            diComponent.fileViewModel.sortFiles(SortingType.NAME, SortingOrder.ASC)
        }

        binding.btnSortZA.setOnClickListener {
            diComponent.fileViewModel.sortFiles(SortingType.NAME, SortingOrder.DESC)
        }
    }

    private fun fetchingFiles() {
        diComponent.fileViewModel.apply {
            /**
             * add if(isAdded) in fragment to avoid null binding
             */
            if (isFilesListEmpty()) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.VISIBLE
            }
            fetchingFiles(
                SortingType.NAME,
                SortingOrder.DESC
            )
        }
    }

    private fun initObserverViewModel() {
        diComponent.fileViewModel.apply {
            fileUpdate.observe(this@MainActivity) {
                /**
                 * add if(isAdded) in fragment to avoid null binding
                 */
                binding.pbLoading.visibility = View.GONE
                if (it) {
                    if (isFilesListEmpty()) {
                        binding.tvNoFilesFound.visibility = View.VISIBLE
                    } else {
                        binding.tvNoFilesFound.visibility = View.GONE
                    }
                    mAdapter.submitList(filesList)
//                    both can be used
//                    mAdapter.submitList(getFileList())
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : OnFileItemClickListener {
            override fun onFileClick(fileItem: FileItem) {
                Toast.makeText(this@MainActivity, "${fileItem.fileName}", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteClick(fileItem: FileItem) {
                diComponent.fileViewModel.deleteFile(fileItem)
            }

        })
    }
}