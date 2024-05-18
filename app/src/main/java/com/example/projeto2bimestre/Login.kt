package com.example.projeto2bimestre
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    private lateinit var profileRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val database = FirebaseDatabase.getInstance()
        profileRef = database.getReference("usuarios")

        val usuarioEditText = findViewById<EditText>(R.id.editar_usuario)
        val senhaEditText = findViewById<EditText>(R.id.editar_senha)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnVoltar = findViewById<Button>(R.id.btn_voltar)

        btnLogin.setOnClickListener {
            val nome = usuarioEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if (nome.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUsuario(nome, senha)
        }


        btnVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUsuario(username: String, senha: String) {
        profileRef.orderByChild("nome").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("LoginActivity", "Usuário encontrado")
                        for (profileSnapshot in dataSnapshot.children) {
                            val profile = profileSnapshot.getValue(Usuarios::class.java)
                            Log.d("LoginActivity", "Perfil: $profile")
                            if (profile != null && profile.senha == senha) {
                                Log.d("LoginActivity", "Login bem-sucedido")
                                val intent = Intent(this@Login, Desafio::class.java)
                                startActivity(intent)
                                finish()
                                return
                            }
                        }
                        Log.d("LoginActivity", "Senha incorreta")
                        Toast.makeText(this@Login, "Senha incorreta", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("LoginActivity", "Usuário não encontrado")
                        Toast.makeText(this@Login, "Usuário não encontrado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("LoginActivity", "Erro de banco de dados: ${databaseError.message}")
                    Toast.makeText(this@Login, "Erro de banco de dados", Toast.LENGTH_SHORT).show()
                }
            })
    }
}