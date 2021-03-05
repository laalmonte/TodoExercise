package com.todoexercise.ui.main

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.todoexercise.model.Item
import com.todoexercise.utils.SessionManager
import com.todoexercise.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Date
import java.sql.Timestamp

class MainViewModel
@ViewModelInject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val notesListStorage = mutableListOf<Item>()

    val notesList     = SingleLiveEvent<MutableList<Item>>()
    val resultString  = SingleLiveEvent<String>()
    val deleteString  = SingleLiveEvent<String>()

    init {

    }

    fun getData(){
        notesListStorage.clear()
        db.collection("items")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    task.result?.let { snap ->
                        snap.forEach { docu ->
                            var docIdValue = ""
                            var titleValue = ""
                            var descValue  = ""
                            var urlValue   = ""
                            var dateValue  = ""
                            docIdValue = docu.id.toString()
                            docu.data.forEach {
                                when (it.key.toString()){
                                  "title" -> { titleValue = it.value.toString()
                                  } "description" -> { descValue  = it.value.toString()
                                  } "icon_url" -> { urlValue   = it.value.toString()
                                  } "created_at" -> {
                                    val timestampValue = it.value as com.google.firebase.Timestamp
                                    val dateString = Date(timestampValue.seconds * 1000)
                                    dateValue  = dateString.toString() }
                                }
                            }

                            val newItem = Item(docIdValue, titleValue, urlValue, descValue, dateValue)
                            notesListStorage.add(newItem)
                        }
                    }
                    resultString.postValue("success")
                    notesList.postValue(notesListStorage)
                } else { resultString.postValue("failed") }
            }
    }

    fun delete(item: Item){
        Log.e("FIRE", item.docId.toString())
        db.collection("items").document(item.docId.toString()).delete()
                .addOnSuccessListener {
                    deleteString.postValue(item.title)
                    SessionManager.triggerRefresh.postValue(true)
                }
                .addOnFailureListener {
                    resultString.postValue("failed")
                }
    }

}
