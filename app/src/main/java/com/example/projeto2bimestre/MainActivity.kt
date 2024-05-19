package com.example.projeto2bimestre

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var profileRef: DatabaseReference
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private lateinit var nomeEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var idadeEditText: EditText
    private lateinit var preferenciasEditText: EditText
    private lateinit var alergiasEditText: EditText
    private lateinit var gostosEditText: EditText
    private lateinit var desgostosEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização do Firebase
        database = FirebaseDatabase.getInstance()
        profileRef = database.getReference("usuarios")

        // Inicialização do AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        nomeEditText = findViewById(R.id.editarNome)
        senhaEditText = findViewById(R.id.EditTextSenha)
        idadeEditText = findViewById(R.id.editTextIdade)
        preferenciasEditText = findViewById(R.id.editTextPreferencias)
        alergiasEditText = findViewById(R.id.editTextAlergias)
        gostosEditText = findViewById(R.id.editTextGostos)
        desgostosEditText = findViewById(R.id.editTextDesgostos)

        val btnSalvarPerfil = findViewById<Button>(R.id.btnSalvarPerfil)
        btnSalvarPerfil.setOnClickListener {
            salvarPerfil()
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        setupAlarm()

    }


    private fun salvarPerfil() {
        val nome = nomeEditText.text.toString()
        val senha = senhaEditText.text.toString()
        val idade = idadeEditText.text.toString()
        val preferencias = preferenciasEditText.text.toString()
        val alergias = alergiasEditText.text.toString()
        val gostos = gostosEditText.text.toString()
        val desgostos = desgostosEditText.text.toString()

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(idade) || TextUtils.isEmpty(preferencias) || TextUtils.isEmpty(alergias) || TextUtils.isEmpty(gostos) || TextUtils.isEmpty(desgostos)) {
            Toast.makeText(baseContext, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val profileData = hashMapOf(
            "nome" to nome,
            "senha" to senha,
            "idade" to idade,
            "preferencias" to preferencias,
            "alergias" to alergias,
            "gostos" to gostos,
            "desgostos" to desgostos
        )

        val key = profileRef.push().key ?: return
        profileRef.child(key).setValue(profileData)

        val intent = Intent(this, Desafio::class.java)
        startActivity(intent)
    }


    private fun setupAlarm() {
        val intent = Intent(this, Alarme::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val intervalMillis = 12 * 60 * 60 * 1000L


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MILLISECOND, intervalMillis.toInt())


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intervalMillis, pendingIntent)
    }
}
