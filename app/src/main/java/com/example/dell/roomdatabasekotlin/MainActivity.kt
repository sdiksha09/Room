package com.example.dell.roomdatabasekotlin

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.dell.roomdatabasekotlin.Model.User
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import com.example.dell.roomdatabasekotlin.Database.UserRepository
import com.example.dell.roomdatabasekotlin.Local.UserDatabase
import com.example.dell.roomdatabasekotlin.Local.UserDataSource
import com.example.dell.roomdatabasekotlin.Local.UserDataSource.Companion
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<*>
    var userList: MutableList<User> = ArrayList() //A generic ordered collection of elements that supports adding and removing elements.
    // Parameters User - the type of elements contained in the list. The mutable list is invariant on its element type.
    //Database
    private var compositeDisposable: CompositeDisposable? = null
    private var userRepository: UserRepository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init

        compositeDisposable = CompositeDisposable()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, userList)  // simple_list_item_2 has two inside a subclass of RelativeLayout
        registerForContextMenu(user_lst)
        user_lst!!.adapter = adapter


        //Database
        val userdatabase = UserDatabase.getInstance(this)
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userdatabase.userDAO()))

        //Load all data from db
        loadData()

    }

    private fun loadData() {
        val disposible = userRepository!!.allusers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ users -> onGetAllUsersSuccess(users) }) {

                    Throwable ->
                    Toast.makeText(this@MainActivity, "" + Throwable.message, Toast.LENGTH_SHORT).show()
                }
        compositeDisposable!!.add(disposible)


    }

    private fun onGetAllUsersSuccess(users: List<User>) {

        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

            R.id.clear -> deleteAllUser()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUser() {
        val disposable = Observable.create(ObservableOnSubscribe<Any> { e ->
            userRepository!!.deleteAllUsers()
            e.onComplete()
        })

                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(io.reactivex.functions.Consumer { },
                        io.reactivex.functions.Consumer {

                            Throwable ->
                            Toast.makeText(this@MainActivity, "" + Throwable.message, Toast.LENGTH_SHORT).show()

                        },
                        Action { loadData() }
                )

        compositeDisposable!!.addAll(disposable)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        menu!!.setHeaderTitle("Select Action")

        menu.add(Menu.NONE, 0, Menu.NONE, "Update")
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete")
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return super.onContextItemSelected(item)

        val info = item!!.menuInfo as AdapterView.AdapterContextMenuInfo
        val user = userList[info.position]

        when (item.itemId) {
            0 -> {
                val edtName = EditText(this@MainActivity)
                edtName.setText(user.name)
                edtName.hint = "Enter your Name"

                //Create Dialog

                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Edit")
                        .setMessage("Edit Your Name")
                        .setView(edtName)
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            if (TextUtils.isEmpty(edtName.text.toString()))
                                return@OnClickListener
                            else {

                                user.name = edtName.text.toString()
                                updateUser(user)
                            }
                        }).setNegativeButton(android.R.string.cancel)

            }
        }


    }
}
