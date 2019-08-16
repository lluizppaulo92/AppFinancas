package br.com.lluizppaulo.financas.extension

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Locale

fun BigDecimal.formatoMoedaBR(): String {
    return DecimalFormat.getCurrencyInstance(Locale("pt", "br"))
        .format(this)
        .replace("R$", " R$ ")
        .replace("- R$ ", "R$ - ")
}