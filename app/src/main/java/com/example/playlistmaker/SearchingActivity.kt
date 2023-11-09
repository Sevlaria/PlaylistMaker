package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity



class SearchingActivity: AppCompatActivity() {
   private var searchText:String=""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)
        val inputEditText = findViewById<EditText>(R.id.input_search)
        val buttonX = findViewById<ImageView>(R.id.button_x)
        inputEditText.requestFocus()
        inputEditText.setText(searchText)
        val backToMain = findViewById<ImageView>(R.id.back_main2)
            backToMain.setOnClickListener {
            super.finish()
        }
        val simpleTextWatcher = object: TextWatcher {
            @SuppressLint("SuspiciousIndentation")
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty())
                buttonX.visibility = ImageView.INVISIBLE

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonX.visibility = ImageView.VISIBLE
                searchText = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty())
                    buttonX.visibility = ImageView.INVISIBLE
                else {
                    buttonX.visibility = ImageView.VISIBLE

                }
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        buttonX.setOnClickListener {
            inputEditText.text.clear()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) 
                    as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(buttonX.windowToken, 0)
        }
        }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if(savedInstanceState != null) {
            searchText = savedInstanceState.getString(EDIT_TEXT, "")
        }
    }
    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
    }

}