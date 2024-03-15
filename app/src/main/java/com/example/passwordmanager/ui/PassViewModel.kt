package com.example.passwordmanager.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.passwordmanager.R
import com.example.passwordmanager.data.Datasource
import com.example.passwordmanager.data.Website
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PassViewModel : ViewModel() {

    private val _sites = MutableStateFlow(Datasource.sites)
    val sites: StateFlow<List<Website>> = _sites.asStateFlow()

    private val _siteName = mutableStateOf("")
    val siteName: String get() = _siteName.value

    private val _url = mutableStateOf("")
    val url: String get() = _url.value

    private val _personName = mutableStateOf("")
    val personName: String get() = _personName.value

    private val _password = mutableStateOf("")
    val password: String get() = _password.value

    fun setSiteName(value: String) {
        _siteName.value = value
    }

    fun setUrl(value: String) {
        _url.value = value
    }

    fun setPersonName(value: String) {
        _personName.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun addSite() {
        val newSite = Website(
            id = _sites.value.size + 1,
            icon = R.drawable.ic_launcher_foreground,
            siteName = _siteName.value,
            url = _url.value,
            personName = _personName.value,
            password = _password.value
        )
        _sites.value.add(newSite)
    }

    fun removeSite(website: Website) {
        Datasource.sites.remove(website)
        _sites.value = Datasource.sites.toMutableList()
    }
}