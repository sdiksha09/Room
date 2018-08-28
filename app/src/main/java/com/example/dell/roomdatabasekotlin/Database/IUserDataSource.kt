package com.example.dell.roomdatabasekotlin.Database

import com.example.dell.roomdatabasekotlin.Model.User
import io.reactivex.Flowable

/**
 * created By Diksha Sharma on 15-08-2018,
 */
interface IUserDataSource {

    val allusers:Flowable<List<User>>
    fun getUserByid(userId:Int):Flowable<User>

    fun insertUser(vararg users:User)
    fun updateUser(vararg users:User)
    fun deletetUser(user:User)
    fun deleteAllUsers()
}