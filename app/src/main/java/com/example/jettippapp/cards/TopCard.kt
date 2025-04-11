package com.example.jettippapp.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TopCard(amountPerPerson: MutableDoubleState = mutableDoubleStateOf(0.0)) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = Color.Magenta,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            TotalPerPersonLabelText()
            Spacer(modifier = Modifier.height(6.dp))
            TotalPerPersonAmountText(amountPerPerson)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}