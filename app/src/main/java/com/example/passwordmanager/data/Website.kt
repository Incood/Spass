package com.example.passwordmanager.data

import androidx.annotation.DrawableRes

data class Website(
    var id: Int,
    @DrawableRes val icon: Int,
    var siteName: String,
    var url: String,
    var personName: String,
    var password: String
)