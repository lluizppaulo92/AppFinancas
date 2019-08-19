package br.com.lluizppaulo.financas.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import br.com.lluizppaulo.financas.R
import br.com.lluizppaulo.financas.delegate.TransacaoDelegate
import br.com.lluizppaulo.financas.model.Tipo
import br.com.lluizppaulo.financas.model.Transacao
import br.com.lluizppaulo.financas.ui.ResumoView
import br.com.lluizppaulo.financas.ui.adapter.ListTransacoesAdapter
import br.com.lluizppaulo.financas.ui.dialog.AdicionaTransacaoDialog
import br.com.lluizppaulo.financas.ui.dialog.AlteraTransacaoDialog
import kotlinx.android.synthetic.main.activity_lista_transacoes.*

class ListaTransacoesActivity : AppCompatActivity() {

    private var transacaoes: MutableList<Transacao> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)
        configuraResumo()
        configuraLista()
        configuraFab()
    }

    private fun configuraFab() {
        lista_transacoes_adiciona_receita
            .setOnClickListener {
                chamaDialogAddTransacao(Tipo.RECEITA)
            }

        lista_transacoes_adiciona_despesa
            .setOnClickListener {
                chamaDialogAddTransacao(Tipo.DESPESA)
            }
    }

    private fun chamaDialogAddTransacao(tipo: Tipo) {
        AdicionaTransacaoDialog(tipo = tipo, context = this, viewGroup = window.decorView as ViewGroup)
            .dialogAddTransacao(object : TransacaoDelegate {
                override fun delafate(transacao: Transacao) {
                    transacaoes.add(transacao)
                    atualizaTransacao()
                    lista_transacoes_adiciona_menu.close(true)
                }

            })
    }

    private fun atualizaTransacao() {

        configuraLista()
        configuraResumo()
    }

    private fun configuraResumo() {
        ResumoView(window.decorView, transacaoes).totalizaRezumo()
    }


    private fun configuraLista() {
        with(lista_transacoes_listview) {
            adapter = ListTransacoesAdapter(transacaoes, this@ListaTransacoesActivity)
            setOnItemClickListener { _, _, position, _ ->
                val transacao =  transacaoes[position]
                AlteraTransacaoDialog(window.decorView as ViewGroup,this@ListaTransacoesActivity,transacao)
                    .dialogAlteraTransacao(object : TransacaoDelegate{
                        override fun delafate(transacao: Transacao) {
                            transacaoes[position] = transacao
                            atualizaTransacao()
                        }

                    })

            }
        }
    }


}