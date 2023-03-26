package com.example.xpense.presentation.transaction_details

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val userDataRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val transactionId: Int = checkNotNull(savedStateHandle["transactionId"])
    private val _currTransaction = mutableStateOf(CurrTransactionState())
    val currTransaction: State<CurrTransactionState> = _currTransaction

    init {
        viewModelScope.launch {
            userDataRepository.getTransactionById(transactionId).collect {
                _currTransaction.value = currTransaction.value.copy(
                    transaction = it
                )
            }
        }

    }

    fun onEvent(event: TransactionsDetailEvent) {
        when (event) {
            is TransactionsDetailEvent.Delete -> {
                event.navHostController.navigateUp()

                viewModelScope.launch {
                    userDataRepository.deleteTransactionById(event.id)
                }

            }
            is TransactionsDetailEvent.Edit -> {}
            is TransactionsDetailEvent.Share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        if (_currTransaction.value.transaction?.transactionType == "Expense") {
                            "I paid $${_currTransaction.value.transaction?.amount} for ${_currTransaction.value.transaction!!.title}."
                        } else {
                            "I earned $${_currTransaction.value.transaction?.amount} from ${_currTransaction.value.transaction!!.title}."

                        }
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                event.context.startActivity(shareIntent)

            }
        }
    }


}