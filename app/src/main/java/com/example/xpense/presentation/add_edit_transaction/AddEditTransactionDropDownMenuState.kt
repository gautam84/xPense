package com.example.xpense.presentation.add_edit_transaction

data class AddEditTransactionDropDownMenuState(
    val isExpanded: Boolean = false,
    val selectedOption: String = "",
    val hint: String = ""
)
