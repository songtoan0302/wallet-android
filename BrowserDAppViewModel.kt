package com.schoolonair.wallet.modules.browser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schoolonair.wallet.core.event.SingleLiveEvent
import com.schoolonair.wallet.modules.market.MarketModule
import com.schoolonair.wallet.modules.market.Tab

class BrowserDAppViewModel() : ViewModel() {

    val tabs = Tab.values()
    val selectedTab = MutableLiveData(getSelectedTab())
    val discoveryListTypeLiveEvent = SingleLiveEvent<MarketModule.ListType>()

    fun onSelect(tab: BrowserDAppTab) {
//        service.currentTab = tab
        selectedTab.postValue(tab)
    }

    fun onClickSeeAll(listType: MarketModule.ListType) {
        discoveryListTypeLiveEvent.value = listType
//        onSelect(Tab.Posts)
    }

    private fun getSelectedTab(): BrowserDAppTab {
        return BrowserDAppTab.Favorite
    }

}
