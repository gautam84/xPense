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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.xpense.presentation.common.components.TransactionCard
import com.example.xpense.presentation.dashboard.DashboardViewModel
import com.example.xpense.presentation.util.Screen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Transactions(
    navHostController: NavHostController,
    viewModel: TransactionsViewModel = hiltViewModel()

) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val transactionList by viewModel.transactions

        val options = listOf("All", "Expense", "Income")

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Transactions", fontSize = 20.sp)

            ExposedDropdownMenuBox(
                expanded = viewModel.transactionType.value.isExpanded,
                onExpandedChange = {
                    viewModel.onEvent(TransactionsEvent.OnExpandedChange)
                },
                modifier = Modifier.width(140.dp)
            ) {
                TextField(
                    readOnly = true,
                    value = viewModel.transactionType.value.selectedOption,
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = viewModel.transactionType.value.isExpanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color(0xFFD16C97),
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                ExposedDropdownMenu(
                    expanded = viewModel.transactionType.value.isExpanded,
                    onDismissRequest = {
                        viewModel.onEvent(TransactionsEvent.OnDismissRequest)
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.onEvent(
                                    TransactionsEvent.ChangeSelectedOption(
                                        selectionOption
                                    )
                                )
                                viewModel.onEvent(TransactionsEvent.OnDismissRequest)
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (transactionList.list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent transactions..",
                    color = Color(0xFFD16C97),
                )
            }

        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    8.dp, 0.dp, 8.dp, 64.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transactionList.list) {
                    TransactionCard(transaction = it) {
                        navHostController.navigate(Screen.TransactionDetails.withArgs(it.id.toString()))

                    }
                }

            }
        }
    }
}


