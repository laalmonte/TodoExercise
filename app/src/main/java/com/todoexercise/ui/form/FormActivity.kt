package com.todoexercise.ui.form

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.todoexercise.R
import com.todoexercise.extensions.hideKeyboard
import com.todoexercise.model.Item
import com.todoexercise.utils.SessionManager
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*
import splitties.bundle.BundleSpec
import splitties.bundle.withExtras
import java.sql.Timestamp

class FormActivity: AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    private var itemGlobal: Item? = null

    object ExtraSpec : BundleSpec() {
        var itemParam: Item? = null
        var type: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        withExtras(ExtraSpec){
            itemParam?.let {
                etTitle.setText(it.title.toString())
                etDesc.setText(it.description.toString())
                etIcon.setText(it.iconUrl.toString())

                itemGlobal = it
            }

            if (type == "edit"){
                btnSave.visibility = View.VISIBLE
                btnCreate.visibility = View.GONE
                toolbarTitle.text    = getString(R.string.edit_item)
            } else {
                btnSave.visibility = View.GONE
                btnCreate.visibility = View.VISIBLE
                toolbarTitle.text    = getString(R.string.add_item)
            }
        }

        attachActions()
        subscribeUi()
    }

    private fun initData(){

    }

    private fun attachActions(){
        btnCreate.setOnClickListener {
            if (etTitle.text.toString() == "") { tvTitleValidation.visibility = View.VISIBLE
            } else {
                tvTitleValidation.visibility = View.GONE
                saveToFireStore()
            }
            hideKeyboard()
        }

        btnSave.setOnClickListener {
            if (etTitle.text.toString() == "") { tvTitleValidation.visibility = View.VISIBLE
            } else {
                tvTitleValidation.visibility = View.GONE
                updateToFireStore()
            }
            hideKeyboard()
        }
    }

    private fun subscribeUi(){

    }

    private fun saveToFireStore(){
        FirebaseFirestore.setLoggingEnabled(true);
        val sampleNote: MutableMap<String, Any> = HashMap()
        sampleNote["created_at"]  = Timestamp(System.currentTimeMillis())
        sampleNote["description"] = etDesc.text.toString()
        sampleNote["icon_url"]    = etIcon.text.toString()
        sampleNote["title"]       = etTitle.text.toString()

        db.collection("items")
                .add(sampleNote)
                .addOnSuccessListener {
                    Log.e("FIRE", "Success")

                    SessionManager.triggerRefresh.postValue(true)

                    finish()}
                .addOnFailureListener {
                    Log.e("FIRE", "Failed > " + it.toString())
                }
    }

    private fun updateToFireStore(){
        FirebaseFirestore.setLoggingEnabled(true);
        val sampleNote: MutableMap<String, Any> = HashMap()
        sampleNote["created_at"]  = Timestamp(System.currentTimeMillis())
        sampleNote["description"] = etDesc.text.toString()
        sampleNote["icon_url"]    = etIcon.text.toString()
        sampleNote["title"]       = etTitle.text.toString()

        itemGlobal?.let { itm ->
            db.collection("items")
                .document(itm.docId.toString())
                .update(sampleNote)
                .addOnSuccessListener {
                    Log.e("FIRE", "Success")
                    SessionManager.triggerRefresh.postValue(true)
                    finish()}
                .addOnFailureListener {
                    Log.e("FIRE", "Failed > " + it.toString())
                }
        }

    }

    companion object {
        private const val TAG = "FormActivity"
    }
}