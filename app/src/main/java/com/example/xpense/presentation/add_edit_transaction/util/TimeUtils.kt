package com.example.xpense.presentation.add_edit_transaction.util

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedTime(): String {
    val currentDateTime = Calendar.getInstance().time
    val formatter = SimpleDateFormat("MMM d, yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(currentDateTime)

}