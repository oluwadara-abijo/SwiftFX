package com.dara.swiftfx.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dara.swiftfx.R
import com.dara.swiftfx.ui.theme.BlueText
import com.dara.swiftfx.ui.theme.GreenAccent

@Composable
fun ConversionScreen(modifier: Modifier) {
    val viewModel = hiltViewModel<ConversionViewModel>()
    val uiState by viewModel.uiState

    val time = remember { System.currentTimeMillis().toString().substring(1..4) }

    Box(Modifier.fillMaxWidth()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .scrollable(
                    rememberScrollState(),
                    orientation = Orientation.Vertical,
                )
                .alpha(if (uiState.isLoading) 0.5f else 1f)
        ) {
            Toolbar()
            HeaderText()
            AmountColumn(uiState.selectedCurrencyFrom, uiState.selectedCurrencyTo)
            CurrencyRow(
                currencies = uiState.currencies,
                onCurrencyFromSelected = { viewModel.updateCurrencyFrom(it) },
                onCurrencyToSelected = { viewModel.updateCurrencyTo(it) })
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenAccent)
            ) {
                Text(stringResource(R.string.convert))
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.mid_market_exchange_rate, time),
                    color = BlueText,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Info, contentDescription = null, tint = LightGray)
            }
        }
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Toolbar() {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {
        IconButton(
            onClick = {},
        ) {
            Icon(
                painterResource(R.drawable.ic_menu),
                contentDescription = stringResource(R.string.menu),
                tint = GreenAccent
            )
        }
        Text(
            text = stringResource(R.string.sign_up),
            color = GreenAccent,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun HeaderText() {
    Row(
        Modifier.padding(top = 48.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            "Currency \nCalculator",
            color = BlueText,
            style = MaterialTheme.typography.headlineLarge
        )
        Image(
            painterResource(R.drawable.ic_dot),
            contentDescription = "Dot",
            Modifier.padding(bottom = 8.dp, start = 2.dp)
        )
    }
}

@Composable
fun AmountColumn(
    currencyFrom: String,
    currencyTo: String
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        AmountTextField(currency = currencyFrom, isReadOnly = false)
        Spacer(Modifier.height(16.dp))
        AmountTextField(currency = currencyTo, isReadOnly = true)
    }
}

@Composable
fun CurrencyRow(
    currencies: List<String>,
    onCurrencyFromSelected: (String) -> Unit,
    onCurrencyToSelected: (String) -> Unit
) {
    val sortedCurrencies = currencies.sorted()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CurrencySpinner(
                currencies = sortedCurrencies,
                onCurrencySelected = onCurrencyFromSelected
            )
        }
        Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            painter = painterResource(R.drawable.ic_swap),
            contentDescription = null
        )
        Box(modifier = Modifier.weight(1f)) {
            CurrencySpinner(
                sortedCurrencies,
                onCurrencySelected = onCurrencyToSelected
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySpinner(
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    var selectedCurrency by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                value = selectedCurrency,
                onValueChange = { },
                readOnly = true,
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_flag),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (isDropdownExpanded) Icon(
                        painterResource(R.drawable.ic_expand_less),
                        contentDescription = null
                    ) else {
                        Icon(
                            painterResource(R.drawable.ic_expand_more),
                            contentDescription = null
                        )
                    }
                }

            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            selectedCurrency = currency
                            isDropdownExpanded = false
                            onCurrencySelected(currency)
                        })
                }
            }
        }
    }
}

@Composable
private fun AmountTextField(currency: String, isReadOnly: Boolean) {
    val focusRequester = remember { FocusRequester() }
    var amount by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        value = amount,
        onValueChange = { amount = it },
        readOnly = isReadOnly,
        trailingIcon = { Text(currency, color = Gray) },
        singleLine = true,
        placeholder = { Text("0") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Gray.copy(alpha = 0.2f),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    ConversionScreen(Modifier)
}
