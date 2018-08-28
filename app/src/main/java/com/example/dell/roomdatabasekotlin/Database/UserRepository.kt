package com.example.dell.roomdatabasekotlin.Database

import com.example.dell.roomdatabasekotlin.Local.UserDAO
import com.example.dell.roomdatabasekotlin.Local.UserDataSource
import com.example.dell.roomdatabasekotlin.Model.User
import io.reactivex.Flowable

/**
 * created By Diksha Sharma on 15-08-2018,
 */
class UserRepository(private val mLocalDataSource: IUserDataSource):IUserDataSource {


    override val allusers: Flowable<List<User>>
        get() = mLocalDataSource.allusers

    override fun getUserByid(userId: Int): Flowable<User> {
        return mLocalDataSource.getUserByid(userId)
    }

    override fun insertUser(vararg users: User) {
        mLocalDataSource.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        mLocalDataSource.updateUser(*users)
    }

    override fun deletetUser(user: User) {
       mLocalDataSource.deletetUser(user)
    }

    override fun deleteAllUsers() {
        mLocalDataSource.deleteAllUsers()

    }

    companion object {
        private var mInstance: UserRepository? = null
        fun getInstance(mLocalDataSource: IUserDataSource): UserRepository {

            if (mInstance == null)
                mInstance = UserRepository(mLocalDataSource)
            return mInstance!!
        }


    }

}