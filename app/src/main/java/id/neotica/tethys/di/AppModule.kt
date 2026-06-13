package id.neotica.tethys.di

import id.neotica.tethys.feature.tethys.TethysViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TethysViewModel() }
}
