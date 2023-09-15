package com.schoolonair.wallet.modules.browser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alphawallet.app.BuildConfig
import com.alphawallet.app.C
import com.alphawallet.app.R
import com.alphawallet.app.entity.URLLoadInterface
import com.alphawallet.app.util.Utils
import com.alphawallet.app.web3.*
import com.alphawallet.app.web3.entity.Address
import com.alphawallet.app.web3.entity.WalletAddEthereumChainObject
import com.alphawallet.app.web3.entity.Web3Call
import com.alphawallet.app.web3.entity.Web3Transaction
import com.alphawallet.token.entity.EthereumMessage
import com.alphawallet.token.entity.EthereumTypedMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class EthanTestDappActivity : AppCompatActivity(), OnSignMessageListener, OnSignPersonalMessageListener,
    OnSignTransactionListener, OnSignTypedMessageListener, OnEthCallListener,
    OnWalletAddEthereumChainObjectListener, OnWalletActionListener, URLLoadInterface {
    private var url: TextView? = null
    private var _web3: Web3View? = null

    private val web3 get() = _web3!!

    private var address = "000"

    //    private Call<SignMessageRequest> callSignMessage;
    //    private Call<SignPersonalMessageRequest> callSignPersonalMessage;
    //    private Call<SignTypedMessageRequest> callSignTypedMessage;
    //    private Call<SignTransactionRequest> callSignTransaction;
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.schoolonair.wallet.R.layout.activity_ethan_test)
        url = findViewById<TextView>(R.id.url)
        _web3 = findViewById<Web3View>(R.id.web3view)
        url!!.text = "https://pancakeswap.finance"
        setupWeb3()
        findViewById<View>(R.id.go).setOnClickListener(View.OnClickListener { v: View? ->
            web3.loadUrl(url!!.text.toString())
            web3.requestFocus()
        })
        web3.loadUrl(url!!.text.toString())
        web3.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        web3.setWebLoadCallback(this);
    }

    private fun setupWeb3() {
        Timber.d("1991 setupWeb3 ")
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        web3.setChainId(56)
        web3.setRpcUrl("https://bsc-dataseed.binance.org")
        web3.setWalletAddress(Address(address))
        web3.setOnSignMessageListener(this)
        web3.setOnSignPersonalMessageListener(this)
        web3.setOnSignTransactionListener(this)
        web3.setOnSignTypedMessageListener(this)
        web3.setOnEthCallListener(this)
        web3.setOnWalletAddEthereumChainObjectListener(this)
        web3.setOnWalletActionListener(this)
        web3.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(webview: WebView, newProgress: Int) {
//                if (newProgress == 100)
//                {
//                    progressBar.setVisibility(View.GONE);
//                    swipeRefreshLayout.setRefreshing(false);
//                    if (refresh != null)
//                    {
//                        refresh.setEnabled(true);
//                    }
//                }
//                else
//                {
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setProgress(newProgress);
//                    swipeRefreshLayout.setRefreshing(true);
//                }
            }

            override fun onConsoleMessage(msg: ConsoleMessage): Boolean {
                Timber.d("1991 Web3 onConsoleMessage " + msg.message())
//                Log.d("1991", "${msg.message()} -- From line " +
//                        "${msg.lineNumber()} of ${msg.sourceId()}")
                val ret = super.onConsoleMessage(msg)
                if (msg.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
//                    if (msg.message().contains(WALLETCONNECT_CHAINID_ERROR))
//                    {
//                        displayCloseWC();
//                    }
                }
                return ret
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                //                currentWebpageTitle = title;
            }

            override fun onPermissionRequest(request: PermissionRequest) {
//                requestCameraPermission(request);
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
                //                requestGeoPermission(origin, callback);
            }

            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fCParams: FileChooserParams
            ): Boolean {
                return if (filePathCallback == null) true else true
            }
        })
        web3.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Timber.d("1991 Web3 shouldOverrideUrlLoading $url")
                val prefixCheck = url.split(":").toTypedArray()
                if (prefixCheck.size > 1) {
                    val intent: Intent
                    when (prefixCheck[0]) {
                        C.DAPP_PREFIX_TELEPHONE -> {
                            intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse(url)
                            startActivity(Intent.createChooser(intent, "Call " + prefixCheck[1]))
                            return true
                        }
                        C.DAPP_PREFIX_MAILTO -> {
                            intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse(url)
                            startActivity(Intent.createChooser(intent, "Email: " + prefixCheck[1]))
                            return true
                        }
                        C.DAPP_PREFIX_ALPHAWALLET -> if (prefixCheck[1] == C.DAPP_SUFFIX_RECEIVE) {
                            Timber.d("Web3 DAPP_PREFIX_ALPHAWALLET")
                            //                                viewModel.showMyAddress(getContext());
                            return true
                        }
                        C.DAPP_PREFIX_WALLETCONNECT -> {
                            //start walletconnect
                            Timber.d("Web3 DAPP_PREFIX_WALLETCONNECT")
                            //                            if (wallet.type == WalletType.WATCH)
//                            {
//                                showWalletWatch();
//                            }
//                            else
//                            {
//                                walletConnectSession = url;
//                                if (getContext() != null)
//                                    viewModel.handleWalletConnect(getContext(), url, activeNetwork);
//                            }
                            return true
                        }
                        else -> {}
                    }
                }

//                setUrlText(url);
                return false
            }
        })
    }

    override fun onSignMessage(message: EthereumMessage) {
        Timber.d("1991 Web3 onSignMessage " + message.getMessage())
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        super.onPointerCaptureChanged(hasCapture)
    }

    override fun onSignPersonalMessage(message: EthereumMessage) {
        Timber.d("1991 Web3  onSignPersonalMessage " + message.getMessage())
    }

    override fun onSignTransaction(transaction: Web3Transaction, url: String) {
        Timber.d("1991 Web3  onSignTransaction $url")
    }

    override fun onSignTypedMessage(message: EthereumTypedMessage) {
        Timber.d("1991 Web3  onSignTypedMessage " + message.getMessage())
    }

    override fun onEthCall(txdata: Web3Call) {
        Timber.d("1991 Web3  onEthCall " + txdata.payload)
    }

    override fun onWalletAddEthereumChainObject(
        callbackId: Long,
        chainObject: WalletAddEthereumChainObject
    ) {
        Timber.d("1991 Web3  onWalletAddEthereumChainObject " + chainObject.chainName)
    }

    override fun onRequestAccounts(callbackId: Long) {
        Timber.d("1991 Web3  onRequestAccounts $callbackId")
        web3.onWalletActionSuccessful(callbackId, "[\"" + address + "\"]");
    }

    override fun onWalletSwitchEthereumChain(
        callbackId: Long,
        chainObj: WalletAddEthereumChainObject
    ) {
        Timber.d("1991 Web3  onWalletSwitchEthereumChain $callbackId")
    }

    override fun onWebpageLoaded(url: String?, title: String?) {
        Timber.d("1991 Web3  onWebpageLoaded $url title $title")
        onWebpageLoadComplete()
    }

    // These two members are for loading a Dapp with an associated chain change.
    // Some multi-chain Dapps have a watchdog thread that checks the chain
    // This thread stays in operation until a new page load is complete.
    private var loadUrlAfterReload: String? = null

    override fun onWebpageLoadComplete() {
        Timber.d("1991 Web3 onWebpageLoadComplete")
        Handler(Looper.getMainLooper()).post {
//            setBackForwardButtons()
            if (loadUrlAfterReload != null) {
                loadUrl(loadUrlAfterReload!!)
                loadUrlAfterReload = null
            }
        }
    }

    private fun loadUrl(urlText: String): Boolean {
//        detachFragments()
//        addToBackStack(DAPP_BROWSER)
//        cancelSearchSession()
//        if (checkForMagicLink(urlText)) return true
        web3.resetView()
        web3.loadUrl(Utils.formatUrl(urlText))
//        setUrlText(Utils.formatUrl(urlText))
        web3.requestFocus()
//        getParentFragmentManager().setFragmentResult(RESET_TOOLBAR, Bundle())
        return true
    }

//    fun switchNetworkAndLoadUrl(chainId: Long, url: String) {
//        forceChainChange = chainId //avoid prompt to change chain for 1inch
//        loadUrlAfterReload =
//            url //after reload with new chain inject, page is clean to load the correct site
//        if (viewModel == null) {
//            initViewModel()
//            return
//        }
//        activeNetwork = viewModel.getNetworkInfo(chainId)
//        updateNetworkMenuItem()
//        viewModel.setNetwork(chainId)
//        startBalanceListener()
//        setupWeb3()
//        web3.resetView()
//        web3.reload()
//    }
}
