package org.wahyuheriyanto.nutricom.view.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel

class DiabetesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiabetesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiabetesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}