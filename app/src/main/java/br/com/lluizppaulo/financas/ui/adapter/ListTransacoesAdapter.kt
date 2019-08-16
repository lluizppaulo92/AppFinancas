package br.com.lluizppaulo.financas.ui.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.com.lluizppaulo.financas.R
import br.com.lluizppaulo.financas.extension.LimitaEmAte
import br.com.lluizppaulo.financas.extension.formataDataBr
import br.com.lluizppaulo.financas.extension.formatoMoedaBR
import br.com.lluizppaulo.financas.model.Tipo
import br.com.lluizppaulo.financas.model.Transacao
import kotlinx.android.synthetic.main.transacao_item.view.*

class ListTransacoesAdapter(
    private val transacoes: List<Transacao>,
    private val context: Context
) : BaseAdapter() {

    private val limiteDaCategoria = 14


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.transacao_item, parent, false)

        val transacao = transacoes[position]

        var cor: Int
        var icone :Int

        if (transacao.tipo == Tipo.RECEITA) {
            cor = ContextCompat.getColor(context, R.color.receita)
            icone = R.drawable.icone_transacao_item_receita
        } else {
            cor = ContextCompat.getColor(context, R.color.despesa)
            icone = R.drawable.icone_transacao_item_despesa
        }
        view.transacao_valor.setTextColor(cor)
        view.transacao_icone.setBackgroundResource(icone)

        view.transacao_valor.text = transacao.valor.formatoMoedaBR()
        view.transacao_categoria.text = transacao.categoria.LimitaEmAte(limiteDaCategoria)
        view.transacao_data.text = transacao.data.formataDataBr("dd/MM/yyyy")

        return view
    }


    override fun getItem(position: Int): Transacao {
        return transacoes[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return transacoes.size
    }


}