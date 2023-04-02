package com.orbitalsonic.filemanagementviewmodel.koin

import com.orbitalsonic.filemanagementviewmodel.viewmodel.FileViewModel
import org.koin.dsl.module


private val viewModelsModules = module {
    single { FileViewModel() }
}

val modulesList = listOf(viewModelsModules)