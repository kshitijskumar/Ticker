package com.example.ticker.utils

fun Int.toDoubleDigitString(): String {
    val directStringFromInt = this.toString()
    return when {
        directStringFromInt.length < 2 -> "0$directStringFromInt"
        else -> directStringFromInt
    }
}