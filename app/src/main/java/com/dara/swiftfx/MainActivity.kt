package com.dara.swiftfx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dara.swiftfx.ui.ConversionScreen
import com.dara.swiftfx.ui.theme.SwiftFXTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwiftFXTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 32.dp, horizontal = 24.dp)
                ) { innerPadding ->
                    ConversionScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftFXTheme {
        ConversionScreen(Modifier)
    }
}