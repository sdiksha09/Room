package com.example.dell.roomdatabasekotlin.Local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.dell.roomdatabasekotlin.Local.UserDatabase.Companion.DATABASE_VERSION
import com.example.dell.roomdatabasekotlin.Model.User

/**
 * created By Diksha Sharma on 15-08-2018,
 */

@Database(entities = arrayOf(User::class),version = DATABASE_VERSION)
abstract class UserDatabase:RoomDatabase(){
    abstract fun userDAO():UserDAO



companion object{
    const val DATABASE_VERSION = 1
    val DATABASE_NAME="Room-Database"
    private var mInstance:UserDatabase?=null

    fun getInstance(context:Context):UserDatabase{
        if(mInstance==null)
            mInstance= Room.databaseBuilder(context,UserDatabase::class.java,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

            return mInstance!!


    }

}}