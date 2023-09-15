package com.schoolonair.wallet.modules.browser

import androidx.lifecycle.ViewModel
import com.schoolonair.wallet.local.walletconnect.WC1SessionStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletConnectManagementViewModel @Inject constructor(
    private val wc1SessionStorage: WC1SessionStorage
): ViewModel(){

    fun checkExistSession(sessionId: String?): Boolean{
        return if(sessionId.isNullOrEmpty()){
            false
        }else {
            val session = wc1SessionStorage.getBySessionId(sessionId)
            session == null
        }
    }
}