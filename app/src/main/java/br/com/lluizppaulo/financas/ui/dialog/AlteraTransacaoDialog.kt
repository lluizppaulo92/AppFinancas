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
import java.util.*
import java.util.zip.DataFormatException

class AlteraTransacaoDialog(
    private val viewGroup: ViewGroup,
    private val context: Context,
    private val transacao: Transacao
) {

    private val layout = criaLayout()
    private val campoValor = layout.form_transacao_valor
    private val campoData = layout.form_transacao_data
    private val campoCategoria = layout.form_transacao_categoria

    private fun criaLayout() = LayoutInflater.from(context).inflate(R.layout.form_transacao, viewGroup, false)


    fun dialogAlteraTransacao(transacaoDelegate: TransacaoDelegate) {

        configuraCampoData()
        campoCategoria.adapter = configuraAdapterTipos()
        configuraDialog(transacaoDelegate)

        campoValor.setText(transacao.valor.toString())
        campoData.setText(transacao.data.formataDataBr("dd/MM/yyyy"))
        val posicaoCategoria = context.resources.getStringArray(categoriasPor(transacao.tipo)).indexOf(transacao.categoria)
        campoCategoria.setSelection(posicaoCategoria,true)


    }

    private fun configuraDialog(transacaoDelegate: TransacaoDelegate) {
        AlertDialog.Builder(context)
            .setTitle(if (transacao.tipo == Tipo.RECEITA) R.string.altera_receita else R.string.altera_despesa)
            .setView(layout)
            .setPositiveButton(
                "Alterar"
            ) { _, _ ->
                val valorT = campoValor.text.toString()
                val dataT = campoData.text.toString()
                val categoriaT = campoCategoria.selectedItem.toString()
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
                        tipo = transacao.tipo,
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
            categoriasPor(transacao.tipo),
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    private fun categoriasPor(pTipo : Tipo) = if (pTipo == Tipo.RECEITA) R.array.categorias_de_receita else R.array.categorias_de_despesa


    private fun configuraCampoData() {
        with(campoData) {
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