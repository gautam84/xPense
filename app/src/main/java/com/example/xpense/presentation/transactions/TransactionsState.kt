package com.example.xpense.presentation.transactions

import com.example.xpense.domain.model.Transaction

data class TransactionsState(
    val list: List<Transaction> = mutableListOf()
)