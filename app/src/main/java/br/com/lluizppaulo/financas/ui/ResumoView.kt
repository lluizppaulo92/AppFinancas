package br.com.lluizppaulo.financas.ui

import android.support.v4.content.ContextCompat
import android.view.View
import br.com.lluizppaulo.financas.R
import br.com.lluizppaulo.financas.extension.formatoMoedaBR
import br.com.lluizppaulo.financas.model.Tipo
import br.com.lluizppaulo.financas.model.Transacao
import kotlinx.android.synthetic.main.resumo_card.view.*

class ResumoView(
    private val view: View,
    private val transacoes: List<Transacao>
) {

    fun totalizaRezumo() {

        val totalReceita: Double = transacoes
            .filter { it.tipo == Tipo.RECEITA }
            .sumByDouble { it.valor.toDouble() }

        val totalDespesa: Double = transacoes
            .filter { it.tipo == Tipo.DESPESA }
            .sumByDouble { it.valor.toDouble() }


        view.resumo_card_receita.text = totalReceita.formatoMoedaBR()
        view.resumo_card_despesa.text = totalDespesa.formatoMoedaBR()

        val total = totalReceita - totalDespesa
        with(view.resumo_card_total) {
            setTextColor(
                if (total < 0) ContextCompat.getColor(view.context, R.color.despesa)
                else ContextCompat.getColor(view.context, R.color.receita)
            )
            text = total.formatoMoedaBR()
        }

    }
}