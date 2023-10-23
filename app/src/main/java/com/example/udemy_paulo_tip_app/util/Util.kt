package com.example.udemy_paulo_tip_app.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) {
        totalBill * tipPercentage / 100
    } else {
        0.0
    }
}

fun calculateTotalPerPerson(
    totalBill : Double,
    splitBy : Int,
    tipByPercentage : Int
) : Double{

    val bill = totalBill + calculateTotalTip(totalBill, tipByPercentage)

    return bill / splitBy

}