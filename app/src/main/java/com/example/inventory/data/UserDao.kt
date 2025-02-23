package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun post(user: User)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun put(user: User)

    @Delete
    suspend fun delete(user: User)


    @Query("SELECT * from users WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    @Query("SELECT * from users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)


}