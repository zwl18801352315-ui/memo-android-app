package com.example.memoapp.ui.navigation

object NavRoutes {
    const val HOME = "home"
    const val ADD_MEMO = "add_memo"
    const val EDIT_MEMO = "edit_memo/{memoId}"
    
    fun getEditMemoRoute(memoId: Int): String {
        return "edit_memo/$memoId"
    }
}
