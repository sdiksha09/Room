package com.example.dell.roomdatabasekotlin.Local

import android.arch.persistence.room.*
import com.example.dell.roomdatabasekotlin.Model.User
import io.reactivex.Flowable

/**
 * created By Diksha Sharma on 15-08-2018,
 */
@Dao
interface UserDAO {
    @get:Query("SELECT * FROM users")
    val allUsers:Flowable<List<User>>

    @Query("SELECT * FROM users WHERE id= :userId")
    fun getUserById(userId:Int):Flowable<User>

    @Insert
    fun insertUser(vararg users:User)

    @Update
    fun updateUser(vararg users:User)


    @Delete
   fun deleteUser(user:User)

    @Query("DELETE FROM users")
    fun deleteAllUsers()
}