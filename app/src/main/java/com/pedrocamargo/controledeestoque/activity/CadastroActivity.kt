package com.pedrocamargo.controledeestoque.activity

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.colman.simplecpfvalidator.isCpf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pedrocamargo.controledeestoque.R
import com.pedrocamargo.controledeestoque.config.ConfiguracaoFirebase
import com.pedrocamargo.controledeestoque.databinding.ActivityCadastroBinding
import com.pedrocamargo.controledeestoque.helper.Base64Custom
import com.pedrocamargo.controledeestoque.model.Usuario
import kotlin.math.log

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var autenticarCadastro: FirebaseAuth

    private lateinit var usuarioCadastro: Usuario

    private var casoSenhaFraca: Int = 0

    private var senhaVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSenhaValida.visibility = View.INVISIBLE

        autenticarCadastro = ConfiguracaoFirebase().getFirebaseAutenticacao()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.txtSenha.setOnTouchListener { v,event ->

            val edtSenha = binding.txtSenha

            if(event.action == MotionEvent.ACTION_UP){
                val drawbleEnd = 2
                val drawble = edtSenha.compoundDrawables[drawbleEnd]

                if(drawble != null){
                    val drawbleWidth = drawble.bounds.width()
                    val touchAreaStart = edtSenha.width - edtSenha.paddingEnd - drawbleWidth

                    if(event.x >= touchAreaStart){
                        mostrarSenha()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }


        validarDados()

    }

    fun validarDados() {

        //pega os dados passados para salvar
        binding.btnCadastrar.setOnClickListener {
            var txtNome: String = binding.txtNome.text.toString()
            var txtCpf: String = binding.txtCpf.text.toString()
            var txtEmail: String = binding.txtEmail.text.toString()
            var txtSenha: String = binding.txtSenha.text.toString()

            //verifica se os campos foram preenchidos
            if (!txtNome.isEmpty()) {
                if (!txtCpf.isEmpty()) {
                    if (txtCpf.isCpf()) {
                        if (!txtEmail.isEmpty()) {
                            if (!txtSenha.isEmpty()) {
                                if(validaSenha(txtSenha)){
                                    usuarioCadastro = Usuario()
                                    usuarioCadastro.nome = txtNome
                                    usuarioCadastro.cpf = txtCpf
                                    usuarioCadastro.email = txtEmail
                                    usuarioCadastro.senha = txtSenha
                                    cadastrarUsuario()
                                }else{
                                    when (casoSenhaFraca){
                                        1 -> {binding.txtSenhaValida.visibility = View.VISIBLE
                                              binding.txtSenhaValida.text = "Senha deve conter ao menos 8 caracteres"}
                                        2 -> {binding.txtSenhaValida.visibility = View.VISIBLE
                                              binding.txtSenhaValida.text = "Senha deve conter ao menos 1 letra maiúis"}
                                        3 -> {binding.txtSenhaValida.visibility = View.VISIBLE
                                             binding.txtSenhaValida.text = "Senha deve conter ao menos 1 número"}
                                        4 -> {binding.txtSenhaValida.visibility = View.VISIBLE
                                              binding.txtSenhaValida.text = "Senha deve conter ao menos 1 caractere especial"}
                                        else -> binding.txtSenhaValida.visibility = View.INVISIBLE
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Preencha a Senha", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Preencha o E-mail", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Coloque um CPF válido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Preencha o CPF", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha o Nome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cadastrarUsuario() {
        autenticarCadastro.createUserWithEmailAndPassword(
            usuarioCadastro.email.toString(), usuarioCadastro?.senha.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var idUsuario: String =
                    Base64Custom().codificarBase64(usuarioCadastro.cpf.toString())
                usuarioCadastro.idUsuario = idUsuario
                usuarioCadastro.salvar()
                finish()
            } else {
                var excecao: String = ""
                try {
                    throw task.exception as Throwable
                } catch (e: FirebaseAuthWeakPasswordException) {
                    excecao = "Digite uma senha mais forte!"
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    excecao = "Por favor, digite um e-mail válido!"
                } catch (e: FirebaseAuthUserCollisionException) {
                    excecao = "Essa conta ja foi cadastrada!"
                } catch (e: Exception) {
                    excecao = "Erro ao cadastrar usuário: " + e.message
                    Log.i("Teste", e.message.toString())
                }
                Toast.makeText(this, excecao, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun validaSenha(senha: String): Boolean{

        //garante que a senha vai ter pelo menos 8 digitos, 1 letra maiúscula, 1 numero e 1 caractere especial
        if(senha.length < 8) {
            casoSenhaFraca = 1
            return false
        }
        if(!senha.any {it.isUpperCase()}){
            casoSenhaFraca = 2
            return false
        }
        if(!senha.any {it.isDigit()}){
            casoSenhaFraca =3
        return false
        }
        if (!senha.any {!it.isLetterOrDigit()}){
            casoSenhaFraca = 4
            return false
        }
        else {
            casoSenhaFraca = 0
            return true
        }
    }

    fun mostrarSenha(){

        senhaVisivel = !senhaVisivel

        var edtSenha = binding.txtSenha

        if(senhaVisivel){
            edtSenha.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }else {
            edtSenha.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }
}