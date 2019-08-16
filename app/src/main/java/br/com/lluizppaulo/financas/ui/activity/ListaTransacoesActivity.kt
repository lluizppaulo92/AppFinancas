package br.com.lluizppaulo.financas.ui.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.com.lluizppaulo.financas.R
import br.com.lluizppaulo.financas.extension.formataDataBr
import br.com.lluizppaulo.financas.model.Tipo
import br.com.lluizppaulo.financas.model.Transacao
import br.com.lluizppaulo.financas.ui.ResumoView
import br.com.lluizppaulo.financas.ui.adapter.ListTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.DataFormatException

class ListaTransacoesActivity : AppCompatActivity() {

    private var transacaoes: MutableList<Transacao> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)
        configuraResumo()
        configuraLista()

        lista_transacoes_adiciona_receita
            .setOnClickListener {
                dialogAddTransacao(Tipo.RECEITA)
            }

        lista_transacoes_adiciona_despesa
            .setOnClickListener {
                dialogAddTransacao(Tipo.DESPESA)
            }


    }

    private fun dialogAddTransacao( tipo : Tipo){
        val layout = LayoutInflater.from(this).inflate(R.layout.form_transacao, window.decorView as ViewGroup, false)
        with(layout.form_transacao_data) {
            val dataAtaual = Calendar.getInstance()

            setText(dataAtaual.formataDataBr("dd/MM/yyyy"))
            setOnClickListener {
                android.app.DatePickerDialog(this@ListaTransacoesActivity,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(year, month, dayOfMonth)
                        layout.form_transacao_data.setText(dataSelecionada.formataDataBr("dd/MM/yyyy"))
                    }
                    , dataAtaual.get(Calendar.YEAR), dataAtaual.get(Calendar.MONTH),
                      dataAtaual.get(Calendar.DAY_OF_MONTH))
                    .show()
            }
        }

        val adapter = ArrayAdapter.createFromResource(
            this,
           if(tipo == Tipo.RECEITA) R.array.categorias_de_receita else R.array.categorias_de_despesa,
            android.R.layout.simple_spinner_dropdown_item
        )

        layout.form_transacao_categoria.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle(R.string.adiciona_receita)
            .setView(layout)
            .setPositiveButton(
                "Adicionar"
            ) { dialog, which ->
                val valorT = layout.form_transacao_valor.text.toString()
                val dataT = layout.form_transacao_data.text.toString()
                val categoriaT = layout.form_transacao_categoria.selectedItem.toString()

                if (valorT.equals("")){
                    layout.form_transacao_valor.error = "Informe o valor"
                    return@setPositiveButton
                }

                val data = Calendar.getInstance()

                try {


                    data.time = SimpleDateFormat("dd/MM/yyyy").parse(dataT)
                }catch (exception : DataFormatException){
                    layout.form_transacao_data.error = "Data inválida"
                    return@setPositiveButton
                }
                val transacao = Transacao(
                    tipo = tipo,
                    valor = valorT.toBigDecimal(),
                    data = data,
                    categoria = categoriaT
                )
                atualizaTransacao(transacao)
                lista_transacoes_adiciona_menu.close(true)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun atualizaTransacao(
        transacao: Transacao
    ) {
        transacaoes.add(transacao)
        configuraLista()
        configuraResumo()
    }

    private fun configuraResumo() {
        ResumoView(window.decorView, transacaoes).totalizaRezumo()
    }


    private fun configuraLista() {
        lista_transacoes_listview.adapter = ListTransacoesAdapter(transacaoes, this)
    }


}