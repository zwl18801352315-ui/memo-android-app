package com.example.memoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.memoapp.data.AppDatabase
import com.example.memoapp.data.Memo
import com.example.memoapp.repository.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MemoRepository
    
    val allMemos: LiveData<List<Memo>>
    
    init {
        val memoDao = AppDatabase.getDatabase(application).memoDao()
        repository = MemoRepository(memoDao)
        allMemos = repository.allMemos.asLiveData()
    }
    
    fun getMemoById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getMemoById(id)
    }
    
    fun insert(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMemo(memo)
    }
    
    fun update(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateMemo(memo)
    }
    
    fun delete(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMemo(memo)
    }
    
    fun updateCompletion(id: Int, isCompleted: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateMemoCompletion(id, isCompleted)
    }
}
