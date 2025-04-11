package com.example.jettippapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettippapp.cards.BottomCard
import com.example.jettippapp.cards.TopCard
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
                totalBillState.doubleValue = billInputTextState.value.toDoubleOrNull() ?: 0.0
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.doubleValue,
                    splitBy.intValue
                )
            }
            Column(modifier = Modifier.padding(innerPadding)) {
                Spacer(modifier = Modifier.height(16.dp))
                TopCard(amountPerPerson = amountPerPersonState)
                Spacer(modifier = Modifier.height(16.dp))
                BottomCard(
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}





