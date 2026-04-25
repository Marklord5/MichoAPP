package util

import java.text.NumberFormat
import java.util.Locale

actual fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return formatter.format(amount)
}
