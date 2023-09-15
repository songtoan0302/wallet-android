package com.schoolonair.wallet.modules.browser.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.schoolonair.wallet.component.resources.R as ResourceR

@Composable
fun GuildConnectDApps(actionCloseBottomSheet: () -> Unit) {
    Column(
        modifier = Modifier.padding(24.dp),
    ) {
        Text(
            "How to connect your wallet to DApps",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row() {
            Image(
                painter = painterResource(com.schoolonair.wallet.component.resources.R.drawable.ic_dot),
                contentDescription = "",
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Your wallet will be connected to most DApps automatically.",
                style = MaterialTheme.typography.body1
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row() {
            Image(
                painter = painterResource(com.schoolonair.wallet.component.resources.R.drawable.ic_dot),
                contentDescription = "",
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = buildAnnotatedString {
                    append("In case it doesn't, please find ")
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W600, color = Color.Black)
                    ) {
                        append("Connect wallet")
                    }
                    append(" option on the DApp, and use ")
//                    withStyle(
//                        style = SpanStyle(fontWeight = FontWeight.W600, color = Color.Black)
//                    ) {
//                        append("Browser wallet, ")
//                    }
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W600, color = Color.Blue)
                    ) {
                        append("Wallet Connect")
                    }
//                    append(" or ")
//                    withStyle(
//                        style = SpanStyle(fontWeight = FontWeight.W600, color = Color.Red)
//                    ) {
//                        append("MetaMask")
//                    }
                    append(" options to connect.")
                }, style = MaterialTheme.typography.body1
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "CONNECT WALLET",
                modifier = Modifier
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(id = com.schoolonair.wallet.component.resources.R.drawable.walletconnect_logo),
                contentDescription = "",
                modifier = Modifier
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 50.dp)
                    .width(180.dp)
            )

        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { actionCloseBottomSheet() },
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF925BCA)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(top = 16.dp),
        ) {
            Text(
                text = "Got it".uppercase(),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.W500),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Don't show this again".uppercase(),
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable(
                    enabled = false
                ) { actionCloseBottomSheet() },
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.W500),
        )
    }
}

@Composable
fun DAppConfirmationContent(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    @StringRes website: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(24.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(drawable), contentDescription = "")
            Text(stringResource(text), style = MaterialTheme.typography.h6)
            Text(stringResource(website), style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(text) + " is a third-party DApp. When using this DApp you are subject to this DApp's User Agreement and Privacy Policy.",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                stringResource(text) + " is liable to you directly and separately from " + stringResource(
                    id = com.schoolonair.wallet.component.resources.R.string.appName
                ), style = MaterialTheme.typography.body1
            )
        }

        ActionButton({

        }, {

        })
    }
}

@Composable
fun ActionButton(
    onClickPositive: () -> Unit,
    onClickNegative: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(text = stringResource(ResourceR.string.Button_Decline).uppercase(),
            color = Color.Black,
            style = TextStyle(fontWeight = FontWeight.W600),
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable {
                    onClickNegative()
                }
        )

        Button(
            onClick = { onClickPositive() },
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan)
        ) {
            Text(
                text = stringResource(ResourceR.string.Send_Confirmation_Title).uppercase(),
                color = Color.White,
                style = TextStyle(fontWeight = FontWeight.W600),
            )
        }

    }
}

val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

val ButtonShape = RoundedCornerShape(
    topStart = 8.dp,
    topEnd = 8.dp,
    bottomStart = 8.dp,
    bottomEnd = 8.dp
)