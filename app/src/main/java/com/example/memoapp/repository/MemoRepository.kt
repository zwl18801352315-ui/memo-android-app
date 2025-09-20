package com.example.memoapp.repository

import com.example.memoapp.data.Memo
import com.example.memoapp.data.MemoDao
import kotlinx.coroutines.flow.Flow

class MemoRepository(private val memoDao: MemoDao) {
    val allMemos: Flow<List<Memo>> = memoDao.getAllMemos()
    
    suspend fun getMemoById(id: Int): Memo? {
        return memoDao.getMemoById(id)
    }
    
    suspend fun insertMemo(memo: Memo) {
        memoDao.insertMemo(memo)
    }
    
    suspend fun updateMemo(memo: Memo) {
        memoDao.updateMemo(memo.copy(updatedAt = System.currentTimeMillis()))
    }
    
    suspend fun deleteMemo(memo: Memo) {
        memoDao.deleteMemo(memo)
    }
    
    suspend fun updateMemoCompletion(id: Int, isCompleted: Boolean) {
        memoDao.updateMemoCompletion(id, isCompleted)
    }
}
