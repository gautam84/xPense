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

package com.example.xpense.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val userDataRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = mutableStateOf(TransactionsState())
    val transactions: State<TransactionsState> = _transactions

    private val _transactionType = mutableStateOf(
        TransactionsDropDownMenuState(
            selectedOption = "All"
        )
    )
    val transactionType: State<TransactionsDropDownMenuState> = _transactionType

    init {
        viewModelScope.launch {
            userDataRepository.getAllTransactions().collect {
                _transactions.value = transactions.value.copy(

                    list = it.reversed()
                )
            }
        }
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.OnExpandedChange -> {
                _transactionType.value = transactionType.value.copy(
                    isExpanded = !_transactionType.value.isExpanded
                )
            }
            is TransactionsEvent.OnDismissRequest -> {
                _transactionType.value = transactionType.value.copy(
                    isExpanded = false
                )
            }
            is TransactionsEvent.ChangeSelectedOption -> {
                _transactionType.value = transactionType.value.copy(
                    selectedOption = event.value
                )
                viewModelScope.launch {
                    userDataRepository.getAllTransactions().collect {
                        _transactions.value = transactions.value.copy(

                            list = when (_transactionType.value.selectedOption) {
                                "Expense" -> {
                                    it.filter { transaction ->
                                        transaction.transactionType == "Expense"

                                    }.reversed()
                                }
                                "Income" -> {
                                    it.filter { transaction ->
                                        transaction.transactionType == "Income"

                                    }.reversed()
                                }
                                else -> {
                                    it.reversed()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}