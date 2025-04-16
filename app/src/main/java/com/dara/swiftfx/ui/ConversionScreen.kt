package com.dara.swiftfx.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
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
import com.dara.swiftfx.utils.formatTimestampToUtc

@Composable
fun ConversionScreen(modifier: Modifier) {
    val viewModel = hiltViewModel<ConversionViewModel>()
    val uiState by viewModel.uiState
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .alpha(if (uiState.isLoadingConversion) 0.5f else 1f)
                .verticalScroll(
                    rememberScrollState(),
                )
        ) {
            Toolbar()
            HeaderText()
            AmountColumn(
                currencyFrom = uiState.selectedCurrencyFrom,
                currencyTo = uiState.selectedCurrencyTo,
                amountTo = uiState.amountTo,
                onAmountFromChanged = { viewModel.updateAmountFrom(it) },
            )
            CurrencyRow(
                currencies = uiState.currencies,
                onCurrencyToSelected = { viewModel.updateCurrencyTo(it) })
            ConvertButton(uiState, viewModel, context)
            if (uiState.timestamp != null) {
                RateAtTimeText(uiState.timestamp)
            }
            Spacer(Modifier.height(24.dp))
            if (uiState.historyDates.isNotEmpty() && uiState.historyRates.isNotEmpty()) {
                HistoryChart(
                    uiState.historyDates, uiState.historyRates, uiState.isLoadingGraph
                )
            }
        }
        if (uiState.isLoadingConversion) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (!uiState.errorMessage.isNullOrBlank()) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun RateAtTimeText(timestamp: Long?) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                R.string.mid_market_exchange_rate,
                formatTimestampToUtc(timestamp)
            ),
            color = BlueText,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.Info, contentDescription = null, tint = LightGray)
    }
}

@Composable
private fun ConvertButton(
    uiState: ConversionUiState,
    viewModel: ConversionViewModel,
    context: Context
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        enabled = (uiState.amountFrom.isNotBlank() && uiState.selectedCurrencyTo.isNotBlank()),
        onClick = {
            val (isValid, errorMessage) = isValidAmount(uiState.amountFrom)
            if (isValid) {
                viewModel.getExchangeRate(
                    uiState.selectedCurrencyFrom,
                    uiState.selectedCurrencyTo
                )
            } else {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GreenAccent)
    ) {
        Text(stringResource(R.string.convert), style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun Toolbar() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(end = 24.dp),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.padding(start = 12.dp)
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
        Modifier.padding(vertical = 48.dp, horizontal = 24.dp),
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
    currencyTo: String,
    amountTo: String,
    onAmountFromChanged: (String) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        AmountFromTextField(
            currency = currencyFrom,
            onAmountChanged = onAmountFromChanged
        )
        Spacer(Modifier.height(16.dp))
        AmountToTextField(
            amount = amountTo,
            currency = currencyTo,
        )
    }
}

@Composable
private fun AmountFromTextField(
    currency: String,
    onAmountChanged: (String) -> Unit,
) {
    var amount by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = amount,
        onValueChange = {
            amount = it
            onAmountChanged(it)
        },
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

@Composable
fun AmountToTextField(amount: String, currency: String) {
    Row(
        modifier = Modifier
            .background(
                color = Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(amount)
        Text(currency, color = Gray)
    }
}

@Composable
fun CurrencyRow(
    currencies: List<String>,
    onCurrencyToSelected: (String) -> Unit
) {
    val sortedCurrencies = currencies.sorted()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CurrencySpinner(
                currencies = sortedCurrencies,
                onCurrencySelected = {},
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
                onCurrencySelected = onCurrencyToSelected,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySpinner(
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit,
//    isBaseCurrency: Boolean,
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

fun isValidAmount(amountFrom: String): Pair<Boolean, String> {
    return when {
        amountFrom == "0" -> Pair(false, "Amount cannot be zero")
        amountFrom.toFloatOrNull() == null -> Pair(false, "Please enter a valid amount")
        else -> Pair(true, "")
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    ConversionScreen(Modifier)
}
