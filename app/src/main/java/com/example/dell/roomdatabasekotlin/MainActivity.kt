package com.example.dell.roomdatabasekotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dell.roomdatabasekotlin.Model.User
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import com.example.dell.roomdatabasekotlin.Database.UserRepository
import com.example.dell.roomdatabasekotlin.Local.UserDatabase
import com.example.dell.roomdatabasekotlin.Local.UserDataSource
import com.example.dell.roomdatabasekotlin.Local.UserDataSource.Companion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var adapter:ArrayAdapter<*>
    var userList:MutableList<User> = ArrayList() //A generic ordered collection of elements that supports adding and removing elements.
                                                // Parameters User - the type of elements contained in the list. The mutable list is invariant on its element type.
    //Database
    private var compositeDisposable:CompositeDisposable?=null
    private var userRepository:UserRepository?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init

        compositeDisposable = CompositeDisposable()

        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_2,userList)  // simple_list_item_2 has two inside a subclass of RelativeLayout
        registerForContextMenu(user_lst)
        user_lst!!.adapter = adapter


        //Database
        val userdatabase = UserDatabase.getInstance(this)
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userdatabase.userDAO()))

        //Load all data from db
        loadData()

    }

    private fun loadData() {
        val disposible=userRepository!!.allusers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({users-> onGetAllUsersSuccess(users)}){

                    Throwable-> Toast.makeText(this@MainActivity,""+Throwable.message,Toast.LENGTH_SHORT).show()
                }
        compositeDisposable!!.add(disposible)

        


    }

    private fun onGetAllUsersSuccess(users: List<User>) {

        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item.itemId){

            R.id.clear->deleteAllUser()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUser() {
        val disposable
    }
}
