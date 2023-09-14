package com.example.mvi.screen.compose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvi.repo.Contact

class ShareViewModel : ViewModel() {
    val shareContact = MutableLiveData<Contact>()
}