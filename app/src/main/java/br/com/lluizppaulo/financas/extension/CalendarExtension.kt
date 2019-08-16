package br.com.lluizppaulo.financas.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formataDataBr(formato: String): String {
    return SimpleDateFormat(formato).format(this.time)
}
