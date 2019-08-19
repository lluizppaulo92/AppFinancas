package br.com.lluizppaulo.financas.delegate

import br.com.lluizppaulo.financas.model.Transacao

interface TransacaoDelegate {
    fun delafate(transacao : Transacao)
}