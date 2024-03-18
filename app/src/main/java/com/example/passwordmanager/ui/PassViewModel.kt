package com.example.passwordmanager.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.R
import com.example.passwordmanager.data.Datasource
import com.example.passwordmanager.data.Website
import com.example.passwordmanager.encrypt.EncryptPasswords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PassViewModel : ViewModel() {

    companion object {
        private var lastSiteId = 0

        fun getLastSiteId(): Int {
            return lastSiteId
        }

        fun incrementLastSiteId() {
            ++lastSiteId
        }
    }

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

    fun encrypt(filesDir: File, siteId: Int) {
        val bytes = _password.value.encodeToByteArray()
        val file = File(filesDir, "secret_$siteId.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        val fos = FileOutputStream(file)

        _password.value = EncryptPasswords().encrypt(
            bytes = bytes,
            outputStream = fos
        ).decodeToString()
    }

    fun decrypt(filesDir: File, siteId: Int): String {
        val file = File(filesDir, "secret_$siteId.txt")
        return if (file.exists()) {
            try {
                val inputStream = FileInputStream(file)
                EncryptPasswords().decrypt(inputStream).decodeToString()
            } catch (e: Exception) {
                "Error decrypting password"
            }
        } else {
            "File not found"
        }
    }

    fun addSite(siteId: Int) {
        val faviconUrlPrefix = "https://www.google.com/s2/favicons?domain="
        val fullFaviconUrl = "$faviconUrlPrefix${_url.value}"
        val newSite = Website(
            id = siteId,
            icon = R.drawable.ic_launcher_foreground,
            siteName = _siteName.value,
            url = fullFaviconUrl,
            personName = _personName.value,
            password = _password.value
        )
        _sites.value.add(newSite)
    }

    fun removeSite(website: Website, context: Context) {
        viewModelScope.launch {
            val updatedSites = Datasource.sites.toMutableList().apply {
                remove(website)
                deletePasswordFile(context, website.id)
            }
            Datasource.sites = updatedSites
            _sites.value = updatedSites

        }
    }

    fun deletePasswordFile(context: Context, siteId: Int) {
        val fileName = "secret_$siteId.txt"
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            file.delete()
        }
    }
}