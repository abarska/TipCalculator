package com.example.jettippapp.util

fun calculateTip(tipPercent: Float, billAmount: Double): Double {
    return if (billAmount > 1) billAmount * tipPercent / 100 else 0.0
}

fun calculateAmountPerPerson(tipAmount: Double, billAmount: Double, splitBy: Int): Double {
    return (billAmount + tipAmount) / splitBy
}