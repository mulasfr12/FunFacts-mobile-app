package com.example.funfactsapp.viewmodel.FactViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel : ViewModel() {
    private val _notificationsEnabled = MutableLiveData<Boolean>()
    val notificationsEnabled: LiveData<Boolean> get() = _notificationsEnabled

    fun updateNotificationSettings(isEnabled: Boolean) {
        _notificationsEnabled.postValue(isEnabled)
    }
}
