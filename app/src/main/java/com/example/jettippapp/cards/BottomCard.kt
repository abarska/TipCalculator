package com.example.jettippapp.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun BottomCard(
    totalBillState: MutableState<String> = mutableStateOf(""),
    validBillState: Boolean = false,
    splitBy: MutableIntState = mutableIntStateOf(1),
    tipAmountState: MutableDoubleState = mutableDoubleStateOf(0.0),
    amountPerPersonState: MutableDoubleState = mutableDoubleStateOf(0.0)
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = Color.Magenta,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AmountInputField(
            totalBillState = totalBillState,
            amountPerPersonState = amountPerPersonState,
            splitBy = splitBy,
            tipAmountState = tipAmountState,
            validBillState = validBillState,
            onAction = KeyboardActions {
                if (!validBillState) return@KeyboardActions
                keyboardController?.hide()
            }
        )
        if (validBillState) {
            SplitSection(
                totalBillState = totalBillState,
                splitBy = splitBy,
                tipAmountState = tipAmountState,
                amountPerPersonState = amountPerPersonState
            )
            Spacer(modifier = Modifier.height(16.dp))
            TipAmountSection(tipAmountState)
            Spacer(modifier = Modifier.height(16.dp))
            SliderSection(
                tipAmountState = tipAmountState,
                totalBillState = totalBillState,
                splitBy = splitBy,
                amountPerPersonState = amountPerPersonState
            )
        }
    }
}
