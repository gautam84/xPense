package com.example.xpense.presentation.transaction_details

import android.content.Context
import androidx.navigation.NavHostController
import com.example.xpense.domain.model.Transaction

sealed class TransactionsDetailEvent {
    data class Share(val context:Context) : TransactionsDetailEvent()
    data class Edit(val transaction:Transaction) : TransactionsDetailEvent()
    data class Delete(val navHostController: NavHostController, val id: Int) : TransactionsDetailEvent()
}