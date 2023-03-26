package com.example.xpense.presentation.dashboard

import com.example.xpense.domain.model.Transaction

data class RecentTransactionsListState(
    val list: List<Transaction> = mutableListOf()
)
