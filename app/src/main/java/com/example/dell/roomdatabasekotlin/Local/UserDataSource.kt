package com.example.dell.roomdatabasekotlin.Local

import com.example.dell.roomdatabasekotlin.Database.IUserDataSource
import com.example.dell.roomdatabasekotlin.Model.User
import com.example.dell.roomdatabasekotlin.Database.UserRepository
import io.reactivex.Flowable

/**
 * created By Diksha Sharma on 15-08-2018,
 */
class UserDataSource(private val userDAO:UserDAO): IUserDataSource {
    override val allusers: Flowable<List<User>>
        get() = userDAO.allUsers

    override fun getUserByid(userId: Int): Flowable<User> {
        return userDAO.getUserById(userId)
    }

    override fun insertUser(vararg users: User) {
        userDAO.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        userDAO.updateUser(*users)
    }

    override fun deletetUser(user: User) {
        userDAO.deleteUser(user)
    }

    override fun deleteAllUsers() {
       userDAO.deleteAllUsers()
    }

    companion object {
        private var mInstance:UserDataSource?=null
        fun getInstance(userDao:UserDAO):UserDataSource{

            if (mInstance==null)
                mInstance=UserDataSource(userDao)
                return mInstance!!
        }
    }
}