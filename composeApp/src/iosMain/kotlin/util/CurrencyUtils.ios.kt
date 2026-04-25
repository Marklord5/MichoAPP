package util

import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber

actual fun formatCurrency(amount: Double): String {
    val formatter = NSNumberFormatter()
    formatter.numberStyle = NSNumberFormatterCurrencyStyle
    formatter.locale = NSLocale(localeIdentifier = "es_MX")
    return formatter.stringFromNumber(NSNumber(amount)) ?: "$0.00"
}
