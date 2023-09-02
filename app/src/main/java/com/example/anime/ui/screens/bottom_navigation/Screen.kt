package com.example.anime.ui.screens.bottom_navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.anime.R

sealed class Screen(val route: String, @StringRes val label: Int, @DrawableRes val icon: Int) {
    object Home: Screen(HOME, R.string.home, R.drawable.home)
    object Upload: Screen(UPLOAD, R.string.upload, R.drawable.upload)

    companion object {
        const val HOME = "home"
        const val UPLOAD = "upload"
    }
}
