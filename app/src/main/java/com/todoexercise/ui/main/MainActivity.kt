package com.todoexercise.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.todoexercise.R
import com.todoexercise.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.todoexercise.extensions.hideKeyboard
import com.todoexercise.extensions.start
import com.todoexercise.model.Item
import com.todoexercise.ui.adapters.ItemAdapter
import com.todoexercise.ui.form.FormActivity
import com.todoexercise.utils.SessionManager
import com.todoexercise.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.activity_main.*
import splitties.bundle.putExtras
import java.sql.Timestamp


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    private val mainViewModel : MainViewModel by viewModels()

    lateinit var firestore: FirebaseFirestore
    lateinit var query: Query
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainViewModel.getData()
        attachActions()
        setupAdapter()
        subscribeUI()
    }

    private fun subscribeUI(){
        mainViewModel.deleteString.observe(this, Observer {
            createDialog(it)
        })

        mainViewModel.resultString.observe(this, Observer {
            when (it) {
                "success" -> {showList()}
                "failed" -> {hideList()}
            }
        })

        mainViewModel.notesList.observe(this, Observer {
            Log.e("FIRE", "pass through")
            Log.e("FIRE", "subs " + it.toString())
            itemAdapter.updateList(it)
            itemAdapter.updateContext(this)
        })

        SessionManager.triggerRefresh.observe(this, Observer {
            if (it) {
                hideKeyboard()
                itemAdapter.updateEmptyList()
                rvTodoList.visibility        = View.INVISIBLE
                lottieLoading.visibility     = View.VISIBLE
                tvCheckConnection.visibility = View.GONE

                Handler(Looper.getMainLooper()).postDelayed({
                    mainViewModel.getData()
                }, 2000)

            }
        })
    }

    private fun attachActions(){
        floatingBtnNote.setOnClickListener {
            start<FormActivity>{
                putExtras(FormActivity.ExtraSpec) {
                    itemParam = null
                    type      = "create"
                }
            }
        }
    }

    private fun setupAdapter(){
        itemAdapter = ItemAdapter(onItemSelectCallback)
        rvTodoList.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private val onItemSelectCallback = object : ItemAdapter.OnItemSelectCallback {
        override fun onSelectItem(item: Item) {
            goToEdit(item)
        }
        override fun onLongSelectItem(item: Item) {
            createDeleteDialog(item)
        }
    }

    private fun goToEdit(item: Item){
        val itemNullable: Item? = item
        start<FormActivity>{
            putExtras(FormActivity.ExtraSpec) {
                itemParam = itemNullable
                type      = "edit"
            }
        }
    }

    private fun createDeleteDialog(item: Item){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Item ")
        builder.setMessage("Do you want do delete ${item.title}?")
        builder.setPositiveButton(this.getText(R.string.delete)) { dialog, which -> mainViewModel.delete(item) }
        builder.setNegativeButton(this.getText(R.string.cancel)) { dialog, which -> }
        builder.show()
    }

    private fun createDialog(titleParam: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Item ")
        builder.setMessage(" ${titleParam} deleted")
        builder.setNegativeButton(this.getText(R.string.ok)) { dialog, which -> }
        builder.show()
    }

    private fun showList(){
        Handler(Looper.getMainLooper()).postDelayed({
            lottieLoading.visibility        = View.GONE
            tvCheckConnection.visibility    = View.GONE
            rvTodoList.visibility           = View.VISIBLE
        }, 2000)
    }

    private fun hideList(){
        Handler(Looper.getMainLooper()).postDelayed({
            lottieLoading.visibility        = View.GONE
            rvTodoList.visibility           = View.INVISIBLE
            tvCheckConnection.visibility    = View.VISIBLE
        }, 2000)
    }

    companion object {

        private const val TAG = "MainActivity"
        private const val LIMIT = 50
    }

}