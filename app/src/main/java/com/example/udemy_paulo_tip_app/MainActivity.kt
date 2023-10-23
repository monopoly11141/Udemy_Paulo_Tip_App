package com.example.udemy_paulo_tip_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udemy_paulo_tip_app.components.InputField
import com.example.udemy_paulo_tip_app.ui.theme.Udemy_Paulo_Tip_AppTheme
import com.example.udemy_paulo_tip_app.util.calculateTotalPerPerson
import com.example.udemy_paulo_tip_app.util.calculateTotalTip
import com.example.udemy_paulo_tip_app.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Udemy_Paulo_Tip_AppTheme {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(
                shape = RoundedCornerShape(corner = CornerSize(12.dp))
            ),
        color = Color.LightGray
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "$${total}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    BillForm() { billAmount ->
        Log.d(TAG, "MainContent: $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    val totalBillState = remember { mutableStateOf("") }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    var splitByState by remember { mutableStateOf(1) }

    var sliderPosition by remember { mutableStateOf(0f) }
    var tipPercentage = (sliderPosition * 100).toInt()

    var tipAmountState by remember { mutableStateOf(0.0) }

    var totalPerPersonStateValue by remember { mutableStateOf(0.0) }

    Column() {
        TopHeader(totalPerPerson = totalPerPersonStateValue)

        Surface(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) {
                            return@KeyboardActions
                        }
                        onValueChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    }
                )

                if (validState) {
                    Row(
                        modifier = Modifier
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(
                            modifier = Modifier
                                .width(120.dp)
                        )

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    if (splitByState > 1) {
                                        splitByState -= 1
                                    } else {
                                        splitByState = 1
                                    }

                                    totalPerPersonStateValue = calculateTotalPerPerson(
                                        totalBillState.value.toDouble(),
                                        splitByState,
                                        tipPercentage
                                    )
                                }
                            )

                            Text(
                                text = "$splitByState",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 10.dp, end = 10.dp)
                            )

                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = {
                                    splitByState += 1

                                    totalPerPersonStateValue = calculateTotalPerPerson(
                                        totalBillState.value.toDouble(),
                                        splitByState,
                                        tipPercentage
                                    )
                                }

                            )
                        }

                    }

                    //Tip Row
                    Row(
                        modifier = Modifier
                            .padding(
                                horizontal = 3.dp,
                                vertical = 12.dp
                            )
                    ) {
                        Text(
                            text = "Tip",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(
                            modifier = Modifier
                                .width(200.dp)
                        )

                        Text(
                            text = "$ ${tipAmountState}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }

                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${tipPercentage}%")

                        Spacer(
                            modifier = Modifier
                                .height(14.dp)
                        )

                        Slider(
                            value = sliderPosition,
                            onValueChange = { newValue ->
                                sliderPosition = newValue
                                tipAmountState = calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)
                                totalPerPersonStateValue = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitByState,
                                    tipPercentage
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            steps = 5
                        )
                    }

                } else {
                    Box(

                    ) {

                    }
                }


            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Udemy_Paulo_Tip_AppTheme {
        MyApp {
            TopHeader()
        }

    }
}