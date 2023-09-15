package com.schoolonair.wallet.modules.browser

import androidx.annotation.StringRes
import com.schoolonair.wallet.modules.market.MarketModule

enum class BrowserDAppTab (@StringRes val titleResId: Int){
    Today(com.schoolonair.wallet.component.resources.R.string.Browser_TabToday),
    Favorite(com.schoolonair.wallet.component.resources.R.string.CoinPage_Favorite),
    Eth(com.schoolonair.wallet.component.resources.R.string.Eth),
    Bsc(com.schoolonair.wallet.component.resources.R.string.Bsc),
    Ftm(com.schoolonair.wallet.component.resources.R.string.Ftm),
    Ada(com.schoolonair.wallet.component.resources.R.string.Ada),
    Avax(com.schoolonair.wallet.component.resources.R.string.Avax),
    Polygon(com.schoolonair.wallet.component.resources.R.string.Polygon);

    companion object {
        private val map = BrowserDAppTab.values().associateBy(BrowserDAppTab::name)

        fun fromString(type: String?): BrowserDAppTab? = map[type]
    }
}