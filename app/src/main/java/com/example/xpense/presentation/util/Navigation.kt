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

package com.example.xpense.presentation.util

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.xpense.presentation.about.About
import com.example.xpense.presentation.add_edit_transaction.AddEditTransaction
import com.example.xpense.presentation.dashboard.Dashboard
import com.example.xpense.presentation.transaction_details.TransactionDetails
import com.example.xpense.presentation.transactions.Transactions

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SetupNavigation() {
    val navController = rememberNavController()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Dashboard.route || currentRoute == Screen.Transactions.route) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    BottomBar(navController = navController)
                    Column {
                        FloatingActionButton(onClick = {
                            navController.navigate(Screen.AddEditTransaction.route + "/-1" + "/null")
                        }, backgroundColor = Color(0xFF243D25)) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "Add Transactions",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            }
        },

        ) {
        NavigationGraph(
            navController = navController,
        )
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

        composable(
            route = Screen.Dashboard.route,
        ) {
            Dashboard(navController)
        }

        composable(
            route = Screen.AddEditTransaction.route + "/{id}/{previousScreen}",
            arguments = listOf(
                navArgument(
                    name = "id"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "previousScreen"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val transactionId = it.arguments?.getInt("transactionId") ?: -1
            val previousScreen = it.arguments?.getString("previousScreen") ?: ""

            AddEditTransaction(
                navHostController = navController,
                transactionId = transactionId,
                previousScreen = previousScreen
            )
        }

        composable(
            route = Screen.TransactionDetails.route + "/{transactionId}",
            arguments = listOf(
                navArgument(
                    name = "transactionId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {
            val transactionId = it.arguments?.getInt("transactionId") ?: -1

            TransactionDetails(navHostController = navController, transactionId = transactionId)
        }

        composable(route = Screen.About.route) {
            About()
        }
        composable(
            route = Screen.Transactions.route
        ) {
            Transactions(navHostController = navController)
        }

    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color(0xFFE4AEC5)

    ) {
        BottomNavigationItem(
            icon =
            {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(com.example.xpense.R.string.dashboard),
                )
            },
            label = {
                Text(text = "Dashboard")
            },


            selectedContentColor = Color(0xFF243D25),
            unselectedContentColor = Color.White,
            alwaysShowLabel = false,
            selected = currentRoute == Screen.Dashboard.route,

            onClick = {
                navController.navigate(Screen.Dashboard.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // re-selecting the same item
                    launchSingleTop = true
                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                }
            }
        )

        Row() {
            Spacer(modifier = Modifier.width(56.dp))
        }


        BottomNavigationItem(
            icon =
            {
                Icon(
                    imageVector = Icons.Filled.Assignment,
                    contentDescription = stringResource(com.example.xpense.R.string.transactions),
                )
            },
            label = {
                Text(text = "Transactions")
            },


            selectedContentColor = Color(0xFF243D25),
            unselectedContentColor = Color.White,
            alwaysShowLabel = false,
            selected = currentRoute == Screen.Transactions.route,
            onClick = {
                navController.navigate(Screen.Transactions.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // re-selecting the same item
                    launchSingleTop = true
                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                }
            }
        )

    }

}