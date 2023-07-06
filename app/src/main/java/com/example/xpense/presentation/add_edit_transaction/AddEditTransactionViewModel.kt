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

package com.example.xpense.presentation.add_edit_transaction

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense.domain.model.Transaction
import com.example.xpense.domain.repository.TransactionRepository
import com.example.xpense.presentation.add_edit_transaction.util.getFormattedTime
import com.example.xpense.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val userDataRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private val transactionId: Int = checkNotNull(savedStateHandle["id"])
    private val previousScreen: String = checkNotNull(savedStateHandle["previousScreen"])


    private val _title = mutableStateOf(
        AddEditTransactionTextFieldState(
            hint = "Enter a Title.."
        )
    )
    val title: State<AddEditTransactionTextFieldState> = _title

    private val _tags = mutableStateOf(
        AddEditTransactionTextFieldState(
            hint = "Tags"
        )
    )
    val tags: State<AddEditTransactionTextFieldState> = _tags

    private val _amount = mutableStateOf(
        AddEditTransactionTextFieldState(
            hint = "Enter the Amount.."
        )
    )
    val amount: State<AddEditTransactionTextFieldState> = _amount

    private val _note = mutableStateOf(
        AddEditTransactionTextFieldState(
            hint = "Type a note.."
        )
    )
    val note: State<AddEditTransactionTextFieldState> = _note

    private val _transactionType = mutableStateOf(
        AddEditTransactionDropDownMenuState(
            hint = "Transaction Type"
        )
    )
    val transactionType: State<AddEditTransactionDropDownMenuState> = _transactionType

    init {

        if (previousScreen == Screen.TransactionDetails.route) {
            viewModelScope.launch {
                userDataRepository.getTransactionById(transactionId).collect{
                    _title.value = title.value.copy(
                        text = it.title
                    )
                    _amount.value = amount.value.copy(
                        text = it.amount.toString()
                    )
                    _note.value = note.value.copy(
                        text = it.note
                    )
                    _tags.value = tags.value.copy(
                        text = it.tags
                    )
                    _transactionType.value = transactionType.value.copy(
                        selectedOption = it.transactionType
                    )
                    _dialogState.value = dialogState.value.copy(
                        text = "Do you want to discard changes?"
                    )

                }
            }
        }
    }

    private val _dialogState = mutableStateOf(DialogState())
    val dialogState: State<DialogState> = _dialogState


    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.EnteredTitle -> {
                _title.value = title.value.copy(
                    text = event.value
                )

            }
            is AddEditTransactionEvent.EnteredAmount -> {
                _amount.value = amount.value.copy(
                    text = event.value
                )
            }
            is AddEditTransactionEvent.EnteredNote -> {
                _note.value = note.value.copy(
                    text = event.value
                )
            }


            is AddEditTransactionEvent.OnExpandedChange -> {
                _transactionType.value = transactionType.value.copy(
                    isExpanded = !_transactionType.value.isExpanded
                )
            }
            is AddEditTransactionEvent.OnDismissRequest -> {
                _transactionType.value = transactionType.value.copy(
                    isExpanded = false
                )
            }
            is AddEditTransactionEvent.ChangeSelectedOption -> {
                _transactionType.value = transactionType.value.copy(
                    selectedOption = event.value
                )
            }
            is AddEditTransactionEvent.EnteredTags -> {
                _tags.value = tags.value.copy(
                    text = event.value
                )
            }
            is AddEditTransactionEvent.SaveEditTransaction -> {
                if (
                    _title.value.text != "" && _tags.value.text != "" && _transactionType.value.selectedOption != "" && _note.value.text != "" && _amount.value.text != ""
                ) {
                    viewModelScope.launch {
                        if (previousScreen == Screen.TransactionDetails.route){
                            userDataRepository.update(
                                Transaction(
                                    id = transactionId,
                                    title = _title.value.text,
                                    amount = _amount.value.text.toLong(),
                                    transactionType = _transactionType.value.selectedOption,
                                    tags = _tags.value.text,
                                    date = getFormattedTime(),
                                    note = _note.value.text


                                )
                            )
                        }else{
                        userDataRepository.insert(
                            Transaction(
                                title = _title.value.text,
                                amount = _amount.value.text.toLong(),
                                transactionType = _transactionType.value.selectedOption,
                                tags = _tags.value.text,
                                date = getFormattedTime(),
                                note = _note.value.text


                            )
                        )
                    }}

                    event.navHostController.navigateUp()

                } else {
                    Toast.makeText(
                        event.context,
                        "Please fill-up all attributes.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is AddEditTransactionEvent.OpenDialog -> {
                _dialogState.value = dialogState.value.copy(
                    state = true
                )
            }
            is AddEditTransactionEvent.CloseDialog -> {
                _dialogState.value = dialogState.value.copy(
                    state = false
                )
            }


        }

    }


}