package com.example.stopwatch

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatch.ui.theme.StopwatchTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var elapsedTime = remember{
                mutableStateOf(0)
            }

            var playCheck = remember{
                mutableStateOf(false)
            }

            StopwatchTheme {
                StopwatchApp(elapsedTime, playCheck)
            }
        }
    }
}

@Composable
fun StopwatchApp(elapsedTime: MutableState<Int>, playCheck: MutableState<Boolean>) {

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            // Stopwatch title
            Text(
                text = "Stopwatch",
                color = Color.White,
                fontSize = 50.sp
            )

            // Stopwatch time
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 250.dp)
                    .drawBehind {
                        this.drawArc(
                            color = Color.Gray,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(160f, -250f),
                            size = Size(700f, 700f),
                            style = Stroke(width = 10f)
                        )
                    },
                contentAlignment = Alignment.Center,

            ){
                Text(
                    text = formatTime(elapsedTime.value),
                    color = Color.White,
                    fontSize = 50.sp,
                )
            }

            // Stopwatch Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 230.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // Play Button
                Box(
                    contentAlignment = Alignment.Center
                ){
                    IconButton(
                        onClick = {
                                  playCheck.value = !playCheck.value
                                  },
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (playCheck.value){
                                    R.drawable.pause_button
                                }else {
                                    R.drawable.play_button
                                }
                            ),
                            contentDescription = "Play Button",
                            modifier = Modifier.size(200.dp),
                            tint = Color.Unspecified
                        )
                    }
                }


                Spacer(modifier = Modifier.width(40.dp))

                // Reset Button
                Box(
                    contentAlignment = Alignment.Center
                ){
                    IconButton(
                        onClick = {
                            elapsedTime.value = 0
                            playCheck.value = false
                        },
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.reset_button),
                            contentDescription = "Reset Button",
                            modifier = Modifier.size(200.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
        }

    LaunchedEffect(playCheck.value) {
        if (playCheck.value) {
            while (true) {
                delay(1000) // Delay for 1 second
                elapsedTime.value++
            }
        }
    }

    }


// Format the time in HH:MM:SS format
fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}
