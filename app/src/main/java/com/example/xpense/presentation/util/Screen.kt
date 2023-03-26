package com.example.xpense.presentation.util

sealed class Screen(val route: String) {
    object Dashboard : Screen(route = "dashboard")
    object TransactionDetails : Screen(route = "transaction_details")
    object AddEditTransaction : Screen(route = "add_transaction")
    object About : Screen(route = "about")
    object Transactions : Screen("transactions")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
