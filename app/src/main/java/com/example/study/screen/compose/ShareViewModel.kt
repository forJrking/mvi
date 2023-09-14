package com.example.study.screen.compose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.study.repo.Contact

class ShareViewModel : ViewModel() {
    val shareContact = MutableLiveData<Contact>()
}