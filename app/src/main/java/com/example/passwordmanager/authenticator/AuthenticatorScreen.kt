package com.example.passwordmanager.authenticator

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.passwordmanager.R
import kotlinx.coroutines.runBlocking

@Composable
fun LockScreen(
    onNextButtonClicked: () -> Unit
) {
    val pin = remember {
        mutableStateListOf<Int>(
        )
    }
    val useBio = remember {
        mutableStateOf(false)
    }

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lock))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(dimensionResource(R.dimen.image_size)),
                        composition = composition
                    )
                    Text(
                        text = "Spass",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.weight(0.1f))
            Text(text = "Verify 4-digit security PIN")

            Spacer(modifier = Modifier.height(10.dp))

            InputDots(pin)

            Text(text = "Use Touch ID", color = Color(0xFF00695C), modifier = Modifier.clickable {
                useBio.value = true
            })
            Spacer(modifier = Modifier.weight(0.1f))

            NumberBoard(
                onNumberClick = { mynumber ->
                    when (mynumber) {
                        " " -> {}
                        "X" -> {
                            if (pin.isNotEmpty()) pin.removeLast()
                        }

                        else -> {
                            if (pin.size < 4)
                                pin.add(mynumber.toInt())
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (pin.size == 4) {
                CheckUserAuth(pin.toList(), onNextButtonClicked)
                pin.clear()
            }

            if (useBio.value) {
                if (Biometric.status(LocalContext.current)) {
                    UseBiometric(onNextButtonClicked)
                } else {
                    Toast.makeText(LocalContext.current, Biometric.statusName(LocalContext.current), Toast.LENGTH_SHORT).show()
                }

                useBio.value = false
            }

        }
    }
}

@Preview
@Composable
fun InputDots(
    numbers: List<Int> = listOf(1, 2),
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        PinIndicator(filled = numbers.isNotEmpty())
        PinIndicator(filled = numbers.size > 1)
        PinIndicator(filled = numbers.size > 2)
        PinIndicator(filled = numbers.size > 3)
    }
}

@Composable
private fun PinIndicator(
    filled: Boolean,
) {
    Box(
        modifier = Modifier
            .padding(15.dp)
            .size(15.dp)
            .clip(CircleShape)
            .background(if (filled) Color.Black else Color.Transparent)
            .border(2.dp, Color.Black, CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun NumberButton(
    modifier: Modifier = Modifier,
    number: String = "1",
    onClick: (number: String) -> Unit = {},
    isImage: Boolean = false
) {
    if (isImage) {
        Box(
            modifier = modifier
                .size(58.dp)
                .clickable { onClick(number) },
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_backspace),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    } else {
        Button(
            onClick = { onClick(number) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = modifier
                .size(90.dp)
                .padding(0.dp),
            shape = RectangleShape,
        ) {
            Text(
                text = number, color = Color.Black, fontSize = 22.sp
            )
        }
    }
}

@Composable
fun NumberBoard(onNumberClick: (String) -> Unit) {
    NumberBoardRow(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", " ", "X"),
        onNumberClick
    )

}

@Composable
fun NumberBoardRow(num: List<String>, onNumberClick: (String) -> Unit) {
    val list = (1..9).map { it.toString() }.toMutableList()
    list.addAll(mutableListOf(" ", "0", "X"))

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            itemsIndexed(items = list) { index, item ->
                NumberButton(
                    modifier = Modifier,
                    number = item,
                    onClick = { onNumberClick(it) },
                    isImage = item == "X"
                )
            }
        }
    )
}

@Composable
fun CheckUserAuth(pin: List<Int>, onLoginSuccess: () -> Unit) {
    val isPinCorrect = pin == listOf(1, 2, 3, 4)
    if (isPinCorrect) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    } else {
        Toast.makeText(LocalContext.current, "Login Failed", Toast.LENGTH_SHORT).show()
    }
}

//code 5  to define UseBioMetric
@Composable
fun UseBiometric(onLoginSuccess: () -> Unit) {
    val activity = LocalContext.current
    LaunchedEffect(key1 = true) {
        Biometric.authenticate(
            activity as FragmentActivity,
            title = "Sharp Wallet",
            subtitle = "Please Authenticate in order to use Sharp Wallet",
            description = "Authentication is must",
            negativeText = "Cancel",
            onSuccess = {
                runBlocking {
                    Toast.makeText(
                        activity,
                        "Authenticated successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    onLoginSuccess()
                }
            },
            onError = { errorCode, errorString ->
                runBlocking {
                    Toast.makeText(
                        activity,
                        "Authentication error: $errorCode, $errorString",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            },
            onFailed = {
                runBlocking {
                    Toast.makeText(
                        activity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        )

    }
}

