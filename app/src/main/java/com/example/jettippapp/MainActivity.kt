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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettippapp.cards.BottomCard
import com.example.jettippapp.cards.TopCard
import com.example.jettippapp.ui.theme.JetTippAppTheme

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
            Column(modifier = Modifier.padding(innerPadding)) {
                Spacer(modifier = Modifier.height(16.dp))
                TopCard(amountPerPerson = amountPerPersonState.doubleValue)
                Spacer(modifier = Modifier.height(16.dp))
                BottomCard(
                    totalBillState = totalBillState,
                    validBillState = validBillState,
                    splitBy = splitBy,
                    tipAmountState = tipAmountState,
                    amountPerPersonState = amountPerPersonState
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}





