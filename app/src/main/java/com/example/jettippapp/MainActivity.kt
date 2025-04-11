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
            val totalBillState = rememberSaveable {
                mutableStateOf("")
            }
            val validBillState = rememberSaveable(totalBillState.value) {
                totalBillState.value.toDoubleOrNull() != null
            }
            val splitBy = rememberSaveable {
                mutableIntStateOf(1)
            }
            val tipAmountState = rememberSaveable {
                mutableDoubleStateOf(0.0)
            }
            val amountPerPersonState = rememberSaveable {
                mutableDoubleStateOf(0.0)
            }
            val tipPercentState = rememberSaveable {
                mutableFloatStateOf(0f)
            }

            // callbacks
            val onSliderValueChanged = { newSliderValue: Float ->
                val rounded =
                    BigDecimal(newSliderValue.toDouble())
                        .setScale(2, RoundingMode.HALF_UP)
                        .toFloat()
                tipPercentState.floatValue = rounded
                tipAmountState.doubleValue = calculateTip(rounded, totalBillState.value.toDouble())
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.value.toDouble(),
                    splitBy.intValue
                )
            }
            val onSplitValueDecreased = {
                if (splitBy.intValue > 1) splitBy.intValue -= 1
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.value.toDouble(),
                    splitBy.intValue
                )
            }
            val onSplitValueIncreased = {
                splitBy.intValue += 1
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = totalBillState.value.toDouble(),
                    splitBy.intValue
                )
            }
            val onBillInputChanged = { newInput: String ->
                totalBillState.value = newInput.trimStart('0').ifEmpty { "" }
                amountPerPersonState.doubleValue = calculateAmountPerPerson(
                    tipAmountState.doubleValue,
                    billAmount = if (validBillState) totalBillState.value.toDouble() else 0.0,
                    splitBy.intValue
                )
            }
            Column(modifier = Modifier.padding(innerPadding)) {
                Spacer(modifier = Modifier.height(16.dp))
                TopCard(amountPerPerson = amountPerPersonState)
                Spacer(modifier = Modifier.height(16.dp))
                BottomCard(
                    totalBillState = totalBillState,
                    validBillState = validBillState,
                    splitBy = splitBy,
                    tipAmountState = tipAmountState,
                    amountPerPersonState = amountPerPersonState,
                    tipPercentState = tipPercentState,
                    onSliderValueChanged = onSliderValueChanged,
                    onSplitValueIncreased = onSplitValueIncreased,
                    onSplitValueDecreased = onSplitValueDecreased,
                    onBillInputChanged = onBillInputChanged,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}





