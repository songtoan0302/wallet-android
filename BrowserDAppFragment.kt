package com.schoolonair.wallet.modules.browser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.alphawallet.ethereum.EthereumNetworkBase
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.schoolonair.wallet.commonui.compose.ComposeAppTheme
import com.schoolonair.wallet.commonui.compose.components.ScrollableTabs
import com.schoolonair.wallet.commonui.compose.components.TabItem
import com.schoolonair.wallet.core.BaseFragment
import com.schoolonair.wallet.core.IAccountsStorage
import com.schoolonair.wallet.core.IAdapterManager
import com.schoolonair.wallet.databinding.FragmentBrowserBinding
import com.schoolonair.wallet.entities.ViewState
import com.schoolonair.wallet.entities.network.ChainNetwork
import com.schoolonair.wallet.features.balance.BalanceViewModel
import com.schoolonair.wallet.modules.browser.view.*
import com.schoolonair.wallet.features.account.manageaccounts.ManageAccountsViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.horizontalsystems.marketkit.models.BlockchainType
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.duckduckgo.app.launch.LaunchBridgeActivity
import com.schoolonair.wallet.features.shared.domain.usecase.GetTestnetEnabledUseCase
import com.schoolonair.wallet.navigation.utils.BrowserActivityNavigationUtils
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class BrowserDAppFragment : BaseFragment() {

    private var _binding: FragmentBrowserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var chainId: Long = 1L
    var address: String = ""
    var rpcServerUrl: String = ""

    private val gson by lazy { Gson() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setUpViewPager()
//        loadChainId()
        chainNetworks.clear()
        binding.search.setContent {
            var flag by remember { mutableStateOf(false) }
            ComposeAppTheme {
                SearchBar(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ){
                    val intent = Intent(requireContext(), LaunchBridgeActivity::class.java)
                    intent.putExtra(BrowserActivityNavigationUtils.EXTRA_CHAIN_ID, chainId)
                    intent.putExtra(BrowserActivityNavigationUtils.EXTRA_ADDRESS, address)
                    intent.putExtra(BrowserActivityNavigationUtils.EXTRA_RPC_SERVER_URL, rpcServerUrl)
                    val json = gson.toJson(chainNetworks)
                    intent.putExtra(BrowserActivityNavigationUtils.EXTRA_CHAIN_NETWORK, json)
                    requireContext().startActivity(intent)
                }
                getAllWallet()
            }
        }
        binding.compose.setContent {
            ComposeAppTheme {
                setUpTab()
            }
//            SimpleButton()
        }
    }

    val balanceViewModel: BalanceViewModel by viewModels()

    val manageAccountsViewModel: ManageAccountsViewModel by viewModels()

    @Inject
    lateinit var adapterManagerInjected: IAdapterManager

    private val chainNetworks = mutableMapOf<Long, ChainNetwork>()

    @Inject
    lateinit var storage: IAccountsStorage

    @Inject
    lateinit var getTestnetEnabledUseCase: GetTestnetEnabledUseCase

    private val testMode get() = runBlocking {
        return@runBlocking getTestnetEnabledUseCase.invoke()
    }

    @Composable
    private fun getAllWallet(){

        val data = storage.allAccounts()
        Timber.d("1994 all acc " + data.size)

        val viewItems by manageAccountsViewModel.viewItemsLiveData.observeAsState()

        viewItems?.let { (regularAccounts, watchAccounts) ->
            if (regularAccounts.isNotEmpty()) {
                regularAccounts.forEach {
                    Timber.d("1994 accountId " + it.accountId + " selected " + it.selected + " title " + it.title)
                }
            }
        }
        balanceViewModel.loadData()
        val uiState = balanceViewModel.uiState
        Crossfade(uiState.viewState) { viewState ->
            when (viewState) {
                is ViewState.Success -> {
                    val balanceViewItems = uiState.balanceViewItems
                    if (balanceViewItems.isNotEmpty()) {
                        EthereumNetworkBase.getAllNetWork().values.toList().forEach {network ->
                            balanceViewItems.forEach {viewItem ->
                                val wallet = viewItem.wallet
                                val receiveAdapter = adapterManagerInjected.getReceiveAdapterForWallet(viewItem.wallet)
                                val receiveAddress = receiveAdapter?.receiveAddress ?: ""
                                val balance = viewItem.balance
                                val coinDecimals =  viewItem.coinDecimals
                                if(network.symbol == "BSC") {
                                    if(wallet.coin.code == "BNB" && wallet.token.blockchain.type == BlockchainType.BinanceSmartChain) { // TODO: Test
                                        if(testMode){
                                            val chainNetwork = ChainNetwork(EthereumNetworkBase.BINANCE_TEST_ID, receiveAddress, balance, coinDecimals)
                                            chainNetworks[chainNetwork.chainId] = chainNetwork
                                        }else{
                                            val chainNetwork = ChainNetwork(EthereumNetworkBase.BINANCE_MAIN_ID, receiveAddress, balance, coinDecimals)
                                            chainNetworks[chainNetwork.chainId] = chainNetwork
                                        }

                                    }
                                }else if(wallet.coin.code == network.symbol){
                                    val chainNetwork = ChainNetwork(network.chainId, receiveAddress, balance, coinDecimals)
                                    chainNetworks[chainNetwork.chainId] = chainNetwork
                                }

                                if(wallet.coin.code == "BNB" && wallet.token.blockchain.type == BlockchainType.BinanceSmartChain) { // TODO: Test
                                    address = receiveAddress
                                    chainId = if(testMode){
                                        EthereumNetworkBase.BINANCE_MAIN_ID
                                    }else{
                                        EthereumNetworkBase.BINANCE_MAIN_ID
                                    }
                                    val networkInfo = EthereumNetworkBase.getNetworkByChain(chainId)
                                    rpcServerUrl = networkInfo.rpcServerUrl
                                }
                            }
                        }
                    } else {
//                        BalanceItemsEmpty(navController, accountViewItem)
                    }
                }
                else -> {}
            }
        }
    }

//    private fun setUpViewPager(){
//        binding.viewPager.adapter = BrowserDAppAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
//        binding.viewPager.isUserInputEnabled = false
//        binding.tabsCompose.setViewCompositionStrategy(
//            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
//        )
//    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun setUpTab() {
        val pagerState = rememberPagerState(initialPage = 0)
        val coroutineScope = rememberCoroutineScope()
        val view = LocalView.current

        val tabs = BrowserDAppTab.values()
        val selectedTab = tabs[pagerState.currentPage]
        val tabItems = tabs.map {
            TabItem(stringResource(id = it.titleResId).toUpperCase(Locale.current), it == selectedTab, it)
        }
        Column() {
            ScrollableTabs(tabItems, onClick = {
                coroutineScope.launch {
                    pagerState.scrollToPage(it.ordinal)
                }
            })

            HorizontalPager(
                count = tabs.size,
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (tabs[page]) {
                    BrowserDAppTab.Today -> {
                        BrowserTodayScreen()
                    }
                    BrowserDAppTab.Favorite -> {
                        BrowserDappScreen(){
                            Timber.d("dapp BrowserDappFragment")
                            showAsBottomSheet(true) {actionCloseBottomSheet ->
                                GuildConnectDApps(actionCloseBottomSheet)
//                                DAppConfirmationContent(com.schoolonair.wallet.component.resources.R.drawable.ic_ethereum, com.alphawallet.app.R.string.eth, com.schoolonair.wallet.component.resources.R.string.soa_link)
                            }
                        }
                    }
                    BrowserDAppTab.Eth -> {
                        BrowserETHScreen()
                    }
                    else -> {
                        Text(text = "" + tabs[page].name)
                    }
                }
            }
        }

    }

    @Composable
    fun SimpleButton() {
        Button(
            onClick = {
                showAsBottomSheet(true) {
//                    GuildConnectDApps()
                DAppConfirmationContent(com.schoolonair.wallet.component.resources.R.drawable.ic_ethereum, com.alphawallet.app.R.string.eth, com.schoolonair.wallet.component.resources.R.string.soa_link)
                }
//            startActivity(Intent(requireContext(), LaunchBridgeActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
        ) {
            Text(text = "Browser")
        }
    }
}