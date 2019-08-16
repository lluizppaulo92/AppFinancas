package br.com.lluizppaulo.financas.extension

fun String.LimitaEmAte(caracteres : Int) : String {
    return if (this.length > caracteres) "${this.substring(0,caracteres)}..."
           else this
}