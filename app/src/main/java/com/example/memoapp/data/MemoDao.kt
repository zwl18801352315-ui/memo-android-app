package com.example.memoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Query("SELECT * FROM memos ORDER BY updatedAt DESC")
    fun getAllMemos(): Flow<List<Memo>>
    
    @Query("SELECT * FROM memos WHERE id = :id")
    suspend fun getMemoById(id: Int): Memo?
    
    @Insert
    suspend fun insertMemo(memo: Memo)
    
    @Update
    suspend fun updateMemo(memo: Memo)
    
    @Delete
    suspend fun deleteMemo(memo: Memo)
    
    @Query("UPDATE memos SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateMemoCompletion(id: Int, isCompleted: Boolean)
}
