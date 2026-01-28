package com.pedrocamargo.controledeestoque.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pedrocamargo.controledeestoque.R
import com.pedrocamargo.controledeestoque.config.ConfiguracaoFirebase
import com.pedrocamargo.controledeestoque.databinding.ActivityLoginBinding
import com.pedrocamargo.controledeestoque.model.Usuario
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var autenticarLogin: FirebaseAuth

    private var usuarioLogin: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    //validar os dados passados para logar o usuario
    fun validarLogin(){

        autenticarLogin = ConfiguracaoFirebase().getFirebaseAutenticacao()

        autenticarLogin.signInWithEmailAndPassword(
            usuarioLogin?.email.toString(),
            usuarioLogin?.senha.toString()
        ).addOnCompleteListener { task ->
            if(task.isSuccessful){
            Log.i("TESTE", "aqui também foi")
               abrirTelaPrincipal()
            }else{
                //reporta o erro
                var excecao: String = ""
                try {
                    throw task.exception as Throwable
                }catch (e: FirebaseAuthInvalidUserException){
                    excecao = "Usuário não cadastrado!"
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    excecao = "E-mail e senha não correspondem ao usuário cadastrado!"
                }catch (e: Exception){
                    excecao ="Erro ao conectar o usuário: " + e.message
                }
                Toast.makeText(this, excecao, Toast.LENGTH_SHORT).show()
            }
        }
    }


    //logar conta do usuario
    fun entrar(view: View){

        //listener para logar usuario
        binding.btnLogar.setOnClickListener {
            val email = binding.campoEmail.text.toString()
            val senha = binding.campoSenha.text.toString()
            Log.i("TESTE", "aqui foi")
            validarLogin()

            //validação para garantir que os campos estão preenchidos
            if(!email.isEmpty()){
                if(!senha.isEmpty()){
                    usuarioLogin?.email = binding.campoEmail.text.toString()
                    usuarioLogin?.senha = binding.campoSenha.text.toString()
                }else{
                    Toast.makeText(this, "Preencha a senha!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Preencha o email!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun abrirTelaPrincipal(){
        startActivity(Intent(this, TelaPrincipalActivity::class.java))
        finish()
    }

}