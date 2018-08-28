package com.example.dell.roomdatabasekotlin.Model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.annotations.NonNull

/**
 * created By Diksha Sharma on 15-08-2018,
 */

@Entity(tableName = "users")
class User {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Int=0

    @ColumnInfo(name="name")
    var name:String?=null

    @ColumnInfo(name="email")
    var email:String?=null

    constructor(){
    }

    override fun toString(): String {
        return StringBuilder(name)
                .append("\n")
                .append(email)
                .toString()
    }

}