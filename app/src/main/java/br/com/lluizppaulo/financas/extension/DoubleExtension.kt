package br.com.lluizppaulo.financas.extension

import java.text.DecimalFormat
import java.util.Locale

fun Double.formatoMoedaBR(): String {
    return DecimalFormat.getCurrencyInstance(Locale("pt", "br"))
        .format(this)
        .replace("R$", " R$ ")
        .replace("- R$ ", "R$ - ")
}