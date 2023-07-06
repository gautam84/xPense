/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

package com.example.xpense.presentation.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userDataRepository: TransactionRepository
) : ViewModel() {
    private val _overviewCardState = mutableStateOf(OverviewCardState())
    val overviewCardState: State<OverviewCardState> = _overviewCardState

    private val _recentTransactions = mutableStateOf(RecentTransactionsListState())
    val recentTransactions: State<RecentTransactionsListState> = _recentTransactions

    init {
        viewModelScope.launch {
            userDataRepository.getAllTransactions().collect { transactions ->
                _overviewCardState.value = overviewCardState.value.copy(
                    totalBalance = transactions.filter { transaction -> transaction.transactionType == "Income" }
                        .sumOf { it.amount } - transactions.filter { transaction -> transaction.transactionType == "Expense" }
                        .sumOf { it.amount },
                    income = transactions.filter { it.transactionType == "Income" }.takeLast(2)
                        .sumOf { it.amount },
                    expense = transactions.filter { it.transactionType == "Expense" }.takeLast(2)
                        .sumOf { it.amount }
                )

                _recentTransactions.value = recentTransactions.value.copy(
                    list = transactions.takeLast(4).reversed()
                )
            }

        }

    }


}