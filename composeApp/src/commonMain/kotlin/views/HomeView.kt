package views

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.MainCard
import components.MainRow
import util.formatCurrency





@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeView(){

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Dividir Cuenta", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        ContentHomeView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
        )
    }


}
@Composable
fun ContentHomeView(modifier: Modifier) {
    var amount by rememberSaveable { mutableStateOf(value = "") }
    val options = listOf(0, 3, 5, 10, 15)
    var selectedTip by remember { mutableStateOf(value = 10) }
    var numberPerson by remember { mutableStateOf(1) }
    var TotalPropina by remember { mutableStateOf(0.00) }
    var Total by remember { mutableStateOf(0.00 )}
    var TotalPersonas by remember { mutableStateOf(0.00) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        MainCard("Ingrese el total de la cuenta") {
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("""^\d*\.?\d*$"""))) {
                        amount = it
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Monto") },
                shape = RoundedCornerShape(percent = 50),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                visualTransformation = CurrencyVisualTransformation()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                options.forEach { option ->
                    FilterChip(
                        selected = selectedTip == option,
                        onClick = { selectedTip = option },
                        label = { Text(text = "$option%") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

            }
            Text(text = "Numero de personas")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { if (numberPerson > 1) numberPerson-- }) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = "Remove",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(numberPerson.toString(), fontSize = 50.sp)

                IconButton(onClick = { numberPerson++ }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Button(onClick = {

                val resultado = Calcular(amount, selectedTip, numberPerson)

                TotalPropina = resultado.propina

                Total = resultado.total

                TotalPersonas = resultado.porPersona

                focusManager.clearFocus()
            }) {

                Text(text = "Calcular")

            }
        }
        MainCard("Cuenta Dividida"){
            MainRow("Propina", formatCurrency(TotalPropina))
            MainRow("Total", formatCurrency(Total))
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total por persona",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatCurrency(TotalPersonas),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "aplicacion desarrollada por Melany Castañeda",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )


    }



}
data class Resultado(
    val propina: Double,
    val total: Double,
    val porPersona: Double
)

fun Calcular(totalStr: String, propinaPorcentaje: Int, personas: Int): Resultado {
    val total = totalStr.toDoubleOrNull() ?: 0.00
    val propina = total * (propinaPorcentaje / 100.00)
    val totalFinal = total + propina
    val porPersona = totalFinal / personas

    return Resultado(propina, totalFinal, porPersona)
}

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val parts = originalText.split('.')
        val intPart = parts[0]
        val decPart = if (parts.size > 1) "." + parts[1] else ""

        val formattedInt = intPart.reversed().chunked(3).joinToString(",").reversed()
        val prefix = "$ "
        val output = prefix + formattedInt + decPart

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= intPart.length) {
                    var commasBefore = 0
                    for (i in 1 until offset) {
                        val digitsFromRight = intPart.length - i
                        if (digitsFromRight > 0 && digitsFromRight % 3 == 0) {
                            commasBefore++
                        }
                    }
                    return offset + prefix.length + commasBefore
                } else {
                    val totalCommas = if (intPart.length > 3) (intPart.length - 1) / 3 else 0
                    return offset + prefix.length + totalCommas
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= prefix.length) return 0
                val adjustedOffset = offset - prefix.length
                var originalOffset = 0
                val textToProcess = output.substring(prefix.length)
                for (i in 0 until adjustedOffset.coerceAtMost(textToProcess.length)) {
                    if (textToProcess[i] != ',') {
                        originalOffset++
                    }
                }
                return originalOffset.coerceAtMost(originalText.length)
            }
        }

        return TransformedText(AnnotatedString(output), offsetMapping)
    }
}

