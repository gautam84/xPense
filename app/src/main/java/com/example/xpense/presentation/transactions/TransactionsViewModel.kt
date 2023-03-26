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