package com.example.jettippapp.cards

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettippapp.R
import java.text.NumberFormat
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun TotalPerPersonLabelText() {
    Text(
        text = stringResource(R.string.total_per_person),
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview(showBackground = true)
@Composable
fun TotalPerPersonAmountText(amount: MutableDoubleState = mutableDoubleStateOf(0.0)) {
    val formattedPrice = stringResource(R.string.price_format).format(amount.doubleValue)
    Text(
        text = "$$formattedPrice",
        style = MaterialTheme.typography.headlineLarge
    )
}

@Preview(showBackground = true)
@Composable
fun AmountInputField(
    totalBillState: MutableDoubleState = mutableDoubleStateOf(0.0),
    billInputTextState: MutableState<String> = mutableStateOf(""),
    onBillInputChanged: (String) -> Unit = {},
    onBillInputCompleted: () -> Unit = {},
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = billInputTextState.value,
        onValueChange = { newValue: String -> onBillInputChanged(newValue) },
        label = { Text(text = stringResource(R.string.enter_bill)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.AttachMoney,
                contentDescription = "dollar icon"
            )
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onBillInputCompleted() }),
    )
}

@Preview(showBackground = true)
@Composable
fun SplitSection(
    splitBy: MutableIntState = mutableIntStateOf(1),
    onSplitValueIncreased: () -> Unit = {},
    onSplitValueDecreased: () -> Unit = {},
) {
    Row {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 24.dp)
        ) {
            BottomCardText(value = stringResource(R.string.split))
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            IconImageButton(
                icon = Icons.Default.Remove,
                contentDescription = R.string.remove_icon,
                onClick = { onSplitValueDecreased() })
            BottomCardText(
                modifier = Modifier.align(Alignment.CenterVertically),
                value = splitBy.intValue.toString()
            )
            IconImageButton(
                icon = Icons.Default.Add,
                contentDescription = R.string.add_icon,
                onClick = { onSplitValueIncreased() })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipAmountSection(tipAmountState: MutableDoubleState = mutableDoubleStateOf(0.0)) {
    Row {
        BottomCardText(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = stringResource(R.string.tip)
        )
        BottomCardText(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = "$ ${stringResource(R.string.price_format).format(tipAmountState.doubleValue)}"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SliderSection(
    tipPercentState: MutableFloatState = mutableFloatStateOf(0.0f),
    onSliderValueChanged: (Float) -> Unit = {}
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TipSlider(
            sliderPosition = tipPercentState,
            onSliderValueChanged
        )
        Spacer(modifier = Modifier.height(16.dp))
        BottomCardText(
            value = NumberFormat.getPercentInstance(Locale.US).format(tipPercentState.floatValue)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomCardText(modifier: Modifier = Modifier, value: String = "text") {
    Text(
        modifier = modifier,
        text = value,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Preview(showBackground = true)
@Composable
fun TipSlider(
    sliderPosition: MutableFloatState = mutableFloatStateOf(0.4f),
    onSliderValueChanged: (Float) -> Unit = {}
) {
    Slider(
        value = sliderPosition.floatValue,
        valueRange = 0f..1f,
        onValueChange = { newValue -> onSliderValueChanged(newValue) }
    )
}

@Preview(showBackground = true)
@Composable
fun IconImageButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Remove,
    @StringRes contentDescription: Int = R.string.remove_icon,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .clickable { onClick.invoke() },
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp, pressedElevation = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp),
                imageVector = icon,
                contentDescription = stringResource(contentDescription)
            )
        }
    }
}

@Composable
fun ErrorMessage(@StringRes errorMessageRes: Int) {
    Text(
        modifier = Modifier.padding(16.dp),
        text = stringResource(errorMessageRes),
        style = MaterialTheme.typography.headlineSmall.copy(color = Color.Red)
    )
}