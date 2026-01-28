package com.pedrocamargo.controledeestoque.model

import com.google.firebase.database.Exclude

data class Usuario (

    var idusuario: String? = null,
    var email: String? = null,
    var senha: String? = null,
    var nome: String? = null,
    var cpf: Int? = null,

    //garante que a senha não seja salva no banco de dados realtime
    @get:Exclude val senhaTemp: String? = null

)



