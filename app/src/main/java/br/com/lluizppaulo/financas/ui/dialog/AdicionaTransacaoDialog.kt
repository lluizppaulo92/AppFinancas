package br.com.lluizppaulo.financas.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.lluizppaulo.financas.R
import br.com.lluizppaulo.financas.delegate.TransacaoDelegate
import br.com.lluizppaulo.financas.extension.formataDataBr
import br.com.lluizppaulo.financas.model.Tipo
import br.com.lluizppaulo.financas.model.Transacao
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.zip.DataFormatException

class AdicionaTransacaoDialog(
    private val viewGroup: ViewGroup,
    private val context: Context,
    private val tipo: Tipo
) {

    private val layout = criaLayout()

    private fun criaLayout() = LayoutInflater.from(context).inflate(R.layout.form_transacao, viewGroup, false)


    fun dialogAddTransacao(transacaoDelegate: TransacaoDelegate) {
        configuraCampoData()
        layout.form_transacao_categoria.adapter = configuraAdapterTipos()
        configuraDialog(transacaoDelegate)
    }

    private fun configuraDialog(transacaoDelegate: TransacaoDelegate) {
        AlertDialog.Builder(context)
            .setTitle(if (tipo == Tipo.RECEITA) R.string.adiciona_receita else R.string.adiciona_despesa)
            .setView(layout)
            .setPositiveButton(
                "Adicionar"
            ) { _, _ ->
                val valorT = layout.form_transacao_valor.text.toString()
                val dataT = layout.form_transacao_data.text.toString()
                val categoriaT = layout.form_transacao_categoria.selectedItem.toString()
                var isCadastraTransacao = true


                if (valorT == "") {
                    Toast.makeText(context, "Tramsação não cadastrada, valor não informado!", Toast.LENGTH_LONG).show()
                    isCadastraTransacao = false
                }

                val data = Calendar.getInstance()

                try {
                    data.time = SimpleDateFormat("dd/MM/yyyy").parse(dataT)
                } catch (exception: DataFormatException) {
                    Toast.makeText(context, "Tramsação não cadastrada, data informada inválida!", Toast.LENGTH_LONG)
                        .show()
                    isCadastraTransacao = false
                }

                if (isCadastraTransacao) {
                    val transacao = Transacao(
                        tipo = tipo,
                        valor = valorT.toBigDecimal(),
                        data = data,
                        categoria = categoriaT
                    )
                    transacaoDelegate.delafate(transacao)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun configuraAdapterTipos(): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            context,
            if (tipo == Tipo.RECEITA) R.array.categorias_de_receita else R.array.categorias_de_despesa,
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    private fun configuraCampoData() {
        with(layout.form_transacao_data) {
            val dataAtaual = Calendar.getInstance()

            setText(dataAtaual.formataDataBr("dd/MM/yyyy"))
            setOnClickListener {
                DatePickerDialog(context,
                     { _, year, month, dayOfMonth ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(year, month, dayOfMonth)
                        setText(dataSelecionada.formataDataBr("dd/MM/yyyy"))
                    }
                    , dataAtaual.get(Calendar.YEAR), dataAtaual.get(Calendar.MONTH),
                    dataAtaual.get(Calendar.DAY_OF_MONTH))
                    .show()
            }
        }
    }
}