package com.pedrocamargo.controledeestoque.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfiguracaoFirebase {

    private  var autenticacao: FirebaseAuth? = null
    private  var firebaseDatabase: DatabaseReference? = null


    //retorna a instancia do FirebaseAuth
    fun getFirebaseAutenticacao(): FirebaseAuth{
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance()
        }
        return autenticacao!!
    }

    //retorna a instancia do FirebaseDatabase
    fun getFirebase(): DatabaseReference{
        if(firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance().reference
        }
        return firebaseDatabase!!
    }

    }

