package com.example.emptyclassroomfinder.utility

import android.app.Activity
import android.widget.EditText
import android.widget.Toast

fun Activity.getEditTextValue(id: Int): String{
    return findViewById<EditText>(id).text.toString()
}

fun Activity.getToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}