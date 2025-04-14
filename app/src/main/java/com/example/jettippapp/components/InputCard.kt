package com.example.jettippapp.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jettippapp.R

@Preview
@Composable
fun InputCard(
    modifier: Modifier = Modifier,
    totalBillState: MutableDoubleState = mutableDoubleStateOf(0.0),
    billInputTextState: MutableState<String> = mutableStateOf(""),
    splitBy: MutableIntState = mutableIntStateOf(1),
    tipAmountState: MutableDoubleState = mutableDoubleStateOf(0.0),
    amountPerPersonState: MutableDoubleState = mutableDoubleStateOf(0.0),
    tipPercentState: MutableFloatState = mutableFloatStateOf(0.0f),
    onSliderValueChanged: (Float) -> Unit = {},
    onSplitValueIncreased: () -> Unit = {},
    onSplitValueDecreased: () -> Unit = {},
    onBillInputChanged: (String) -> Unit = {},
    onBillInputCompleted: () -> Unit = {}
) {
    OutlinedCard(modifier = modifier) {
        AmountInputField(
            totalBillState = totalBillState,
            billInputTextState = billInputTextState,
            onBillInputChanged = onBillInputChanged,
            onBillInputCompleted = onBillInputCompleted
        )
        if (billInputTextState.value.toDoubleOrNull() != null && billInputTextState.value.toDouble() > 0) {
            val verticalSpacing = dimensionResource(R.dimen.vertical_space_between_rows)
            SplitSection(
                splitBy = splitBy,
                onSplitValueIncreased = onSplitValueIncreased,
                onSplitValueDecreased = onSplitValueDecreased
            )
            Spacer(modifier = Modifier.height(verticalSpacing))
            TipAmountSection(tipAmountState)
            Spacer(modifier = Modifier.height(verticalSpacing))
            SliderSection(
                tipPercentState = tipPercentState,
                onSliderValueChanged = onSliderValueChanged
            )
        } else if (billInputTextState.value != "") {
            ErrorMessage(R.string.invalid_input)
        }
    }
}
