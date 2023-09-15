package com.schoolonair.wallet.modules.browser.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetUIWrapper(
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
            .background(Color.White)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 10.dp, start = 15.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painterResource(id = com.schoolonair.wallet.component.resources.R.drawable.ic_baseline_close_24),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 10.dp)
                    .clickable {
                        coroutineScope.launch {
                            modalBottomSheetState.hide()
                        }
                    }
            )
        }
        Box(Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp)) {
            content()
        }
    }
}
