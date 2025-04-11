package com.example.jettippapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettippapp.cards.InputCard
import com.example.jettippapp.cards.InfoCard
import com.example.jettippapp.ui.theme.JetTippAppTheme
import com.example.jettippapp.util.calculateAmountPerPerson
import com.example.jettippapp.util.calculateTip
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Preview
@Composable
private fun MyApp() {
    JetTippAppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->

            // state variables
            val totalBillState = rememberSaveable { mutableDoubleStateOf(0.0) }
            val billInputTextState = rememberSaveable { mutableStateOf("") }
            val splitBy = rememberSaveable { mutableIntStateOf(1) }
            val tipAmountState = rememberSaveable { mutableDoubleStateOf(0.0) }
            val amountPerPersonState = rememberSaveable { mutableDoubleStateOf(0.0) }
            val tipPercentState = rememberSaveable { mutableFloatStateOf(0f) }

            val keyboardController = LocalSoftwareKeyboardController.current

            // callbacks
            val onSliderValueChanged = { newSliderValue: Float ->
                val rounded =
                    BigDecimal(newSliderValue.toDouble())
                        .setScale(2, RoundingMode.HALF_UP)
                        .toFloat()
                tipPercentState.floatValue = rounded
                tipAmountState.doubleValue = calculateTip(rounded, totalBillState.doubleValue)
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.doubleValue,
                    splitBy.intValue
                )
            }
            val onSplitValueDecreased = {
                if (splitBy.intValue > 1) splitBy.intValue -= 1
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.doubleValue,
                    splitBy.intValue
                )
            }
            val onSplitValueIncreased = {
                splitBy.intValue += 1
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.doubleValue,
                    splitBy.intValue
                )
            }
            val onBillInputChanged = { newInput: String ->
                billInputTextState.value = newInput
            }
            val onBillInputCompleted = {
                keyboardController?.hide()
                totalBillState.doubleValue =
                    billInputTextState.value.toDoubleOrNull() ?: 0.0
                tipAmountState.doubleValue =
                    calculateTip(tipPercentState.floatValue, totalBillState.doubleValue)
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.doubleValue,
                    splitBy.intValue
                )
            }

            val orientation = LocalContext.current.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                PortraitOrientationLayout(
                    innerPadding = innerPadding,
                    totalBillState = totalBillState,
                    billInputTextState = billInputTextState,
                    splitBy = splitBy,
                    tipAmountState = tipAmountState,
                    amountPerPersonState = amountPerPersonState,
                    tipPercentState = tipPercentState,
                    onSliderValueChanged = onSliderValueChanged,
                    onSplitValueIncreased = onSplitValueIncreased,
                    onSplitValueDecreased = onSplitValueDecreased,
                    onBillInputChanged = onBillInputChanged,
                    onBillInputCompleted = onBillInputCompleted
                )
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeOrientationLayout(
                    innerPadding = innerPadding,
                    totalBillState = totalBillState,
                    billInputTextState = billInputTextState,
                    splitBy = splitBy,
                    tipAmountState = tipAmountState,
                    amountPerPersonState = amountPerPersonState,
                    tipPercentState = tipPercentState,
                    onSliderValueChanged = onSliderValueChanged,
                    onSplitValueIncreased = onSplitValueIncreased,
                    onSplitValueDecreased = onSplitValueDecreased,
                    onBillInputChanged = onBillInputChanged,
                    onBillInputCompleted = onBillInputCompleted
                )
            }
        }
    }
}

@Composable
private fun LandscapeOrientationLayout(
    innerPadding: PaddingValues,
    amountPerPersonState: MutableDoubleState,
    totalBillState: MutableDoubleState,
    billInputTextState: MutableState<String>,
    splitBy: MutableIntState,
    tipAmountState: MutableDoubleState,
    tipPercentState: MutableFloatState,
    onSliderValueChanged: (Float) -> Unit,
    onSplitValueIncreased: () -> Unit,
    onSplitValueDecreased: () -> Unit,
    onBillInputChanged: (String) -> Unit,
    onBillInputCompleted: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val verticalSpacing = dimensionResource(R.dimen.vertical_space_between_rows)
    val landscapeOrientationModifier =
        Modifier
            .width(screenWidth / 2)
            .fillMaxHeight()
            .padding(vertical = verticalSpacing)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
    Row(modifier = Modifier.padding(innerPadding)) {
        Spacer(modifier = Modifier.width(16.dp))
        InfoCard(
            modifier = landscapeOrientationModifier,
            amountPerPerson = amountPerPersonState
        )
        Spacer(modifier = Modifier.width(16.dp))
        InputCard(
            modifier = landscapeOrientationModifier,
            totalBillState = totalBillState,
            billInputTextState = billInputTextState,
            splitBy = splitBy,
            tipAmountState = tipAmountState,
            amountPerPersonState = amountPerPersonState,
            tipPercentState = tipPercentState,
            onSliderValueChanged = onSliderValueChanged,
            onSplitValueIncreased = onSplitValueIncreased,
            onSplitValueDecreased = onSplitValueDecreased,
            onBillInputChanged = onBillInputChanged,
            onBillInputCompleted = onBillInputCompleted
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
private fun PortraitOrientationLayout(
    innerPadding: PaddingValues,
    amountPerPersonState: MutableDoubleState,
    totalBillState: MutableDoubleState,
    billInputTextState: MutableState<String>,
    splitBy: MutableIntState,
    tipAmountState: MutableDoubleState,
    tipPercentState: MutableFloatState,
    onSliderValueChanged: (Float) -> Unit,
    onSplitValueIncreased: () -> Unit,
    onSplitValueDecreased: () -> Unit,
    onBillInputChanged: (String) -> Unit,
    onBillInputCompleted: () -> Unit
) {
    val portraitOrientationModifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = dimensionResource(R.dimen.card_padding_horizontal))
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
    Column(modifier = Modifier.padding(innerPadding)) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_padding_vertical)))
        InfoCard(
            modifier = portraitOrientationModifier,
            amountPerPerson = amountPerPersonState
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_padding_vertical)))
        InputCard(
            modifier = portraitOrientationModifier,
            totalBillState = totalBillState,
            billInputTextState = billInputTextState,
            splitBy = splitBy,
            tipAmountState = tipAmountState,
            amountPerPersonState = amountPerPersonState,
            tipPercentState = tipPercentState,
            onSliderValueChanged = onSliderValueChanged,
            onSplitValueIncreased = onSplitValueIncreased,
            onSplitValueDecreased = onSplitValueDecreased,
            onBillInputChanged = onBillInputChanged,
            onBillInputCompleted = onBillInputCompleted
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.card_padding_vertical)))
    }
}





