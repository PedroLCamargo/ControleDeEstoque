package com.pedrocamargo.controledeestoque.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.pedrocamargo.controledeestoque.config.ConfiguracaoFirebase

data class Usuario (

    @get:Exclude var idUsuario: String = "",
    var email: String = "",
    var senha: String = "",
    var nome: String = "",
    var cpf: String = "",

    //garante que a senha não seja salva no banco de dados realtime
    @get:Exclude val senhaTemp: String = ""
){
    fun salvar(){
        val firebase: DatabaseReference = ConfiguracaoFirebase().getFirebase()
        //verifica se o idUsuario não é nulo antes de salvar
        if (idUsuario.isNotEmpty()) {
            firebase.child("usuarios")
                .child(idUsuario)
                .setValue(this)
        }
    }
}





