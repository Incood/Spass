package com.example.passwordmanager.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("PasswordManagerPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveSites(sites: MutableList<Website>) {
        val jsonString = gson.toJson(sites)
        sharedPreferences.edit().putString("sites", jsonString).apply()
    }

    fun loadSites(): List<Website> {
        val jsonString = sharedPreferences.getString("sites", null) ?: return emptyList()
        val type = object : TypeToken<List<Website>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}