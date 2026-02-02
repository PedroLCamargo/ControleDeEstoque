package com.pedrocamargo.controledeestoque.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.pedrocamargo.controledeestoque.R
import com.pedrocamargo.controledeestoque.config.ConfiguracaoFirebase

class MainActivity : AppCompatActivity() {

    lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        autenticacao = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtCadastrar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        if(::autenticacao.isInitialized){
            isLoged()
        }

    }

    //função para verificar se o usuário já está logado
    fun isLoged(){
       // autenticacao.signOut()
        //se já etiver logado inicia a tela principal
        if(autenticacao.currentUser != null){
            startActivity(Intent(this, TelaPrincipalActivity::class.java))
        }else{
            //se não estiver logado inicia a tela de login
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}