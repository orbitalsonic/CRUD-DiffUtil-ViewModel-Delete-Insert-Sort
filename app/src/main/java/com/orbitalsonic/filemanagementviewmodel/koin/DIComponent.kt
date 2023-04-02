package com.orbitalsonic.filemanagementviewmodel.koin

import com.orbitalsonic.filemanagementviewmodel.viewmodel.FileViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent : KoinComponent {
    // ViewModels
    val fileViewModel by inject<FileViewModel>()

}