package com.blueray.fares.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.blueray.fares.api.Repository
import com.blueray.fares.helpers.HelperUtils.showMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: Repository,
    // adding context to constructor can cause some memory leaks but hilt consider this type of injections safe
    @ApplicationContext val context: Context
): ViewModel() {

    init {
        showToast()
    }

    private fun showToast(){
        showMessage(context,"hey there I am alive")
    }
}