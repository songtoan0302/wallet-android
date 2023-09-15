package com.schoolonair.wallet.modules.browser.entities

data class DApp(
    val drawable: Int,
    val image: String,
    val title: String,
    val description: String,
    val link: String,
    val category: DAppCategory
)