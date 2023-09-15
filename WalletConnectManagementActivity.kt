package com.schoolonair.wallet.modules.browser

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.alphawallet.app.C
import com.schoolonair.wallet.R
import com.schoolonair.wallet.core.WalletBaseActivity
import com.schoolonair.wallet.modules.walletconnect.session.v1.WCSessionModule
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WalletConnectManagementActivity : WalletBaseActivity() {

    private val viewModel: WalletConnectManagementViewModel by viewModels()

    //WalletConnectManagementActivity url wc:254986d1-6955-4761-b7a8-db3914a53469@1?bridge=https%3A%2F%2Fo.bridge.walletconnect.org&key=2af6bd1c6c8a87987b3b8e4b743fb9d040ec152230d7a4841e1cbca7edb9a36b
    //103 WalletConnectManagementActivity url wc:2c169b6c-e583-41a6-b68c-8263caef9b69@1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        val url = intent.extras?.getString(C.EXTRA_URL_CONNECT)
        val tabId = intent.extras?.getString(C.EXTRA_TAB_ID)

        Timber.d("103 WalletConnectManagementActivity url $url")
//        val graph = navHost.navController.graph
        val navController = navHost.navController
        val navGraph = navController.navInflater.inflate(com.schoolonair.wallet.navigation.R.navigation.wallet_connect_graph)

        val sessionId = getSessionId(url)
        Timber.d("103 WalletConnectManagementActivity sessionId $sessionId")
        if(viewModel.checkExistSession(sessionId)){
//            graph.startDestination = R.id.wcSessionFragment
            val bundle = WCSessionModule.prepareParams(
                sessionId,
                null,
                null,
                null,
            )
            navGraph.setStartDestination(com.schoolonair.wallet.navigation.R.id.wcSessionFragment)
            navController.setGraph(navGraph, bundle)

//            navHost.navController.setGraph(R.navigation.wallet_connect_graph, bundle)
        }else{
//            graph.startDestination = R.id.wcListFragment
            val bundle = bundleOf("url" to url, "from_screen" to C.FROM_DAPP, "tabId" to tabId)
//            navHost.navController.setGraph(R.navigation.wallet_connect_graph, bundle)

            navGraph.setStartDestination(com.schoolonair.wallet.navigation.R.id.wcListFragment)
            navController.setGraph(navGraph, bundle)
        }

        navHost.navController.addOnDestinationChangedListener(this)

    }

    private fun getSessionId(url: String?): String?{
        if(url.isNullOrEmpty()) return null
        val uri = url.replace("wc:", "wc://")
        return Uri.parse(uri).userInfo
    }

}