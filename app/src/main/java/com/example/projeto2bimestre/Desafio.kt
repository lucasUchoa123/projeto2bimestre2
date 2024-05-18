package com.example.projeto2bimestre
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Desafio : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reviewRef: DatabaseReference

    private val MapaDeDesafios = mapOf(

        "Deixe um bilhete carinhoso para o seu parceiro" to "Expresse seu amor e gratidão em palavras escritas.",
        "Faça uma caminhada de 30 minutos ao ar livre" to "Aproveite o ar fresco e a natureza ao seu redor para relaxar e rejuvenescer.",
        "Cozinhem juntos uma nova receita de culinária" to "Experimente sabores novos e expanda seu repertório culinário.",
        "Tirem um tempo para meditar por 10 minutos" to "Acalme sua mente e pratique a atenção plena para reduzir o estresse.",
        "Limpem e organizem um espaço da sua casa" to "Crie um ambiente mais harmonioso e produtivo ao seu redor.",
        "Tentem aprender uma nova habilidade, como tocar um instrumento musical" to "Desenvolva uma nova habilidade que traga alegria e satisfação à sua vida.",
        "Faça um jantar romântico em casa para o seu parceiro" to "Prepare uma refeição especial e crie um ambiente aconchegante com velas e música suave.",
        "Assistam juntos ao pôr do sol em um local tranquilo" to "Desfrutem de um momento tranquilo e romântico enquanto observam as cores do pôr do sol juntos.",
        "Escrevam juntos uma lista de sonhos e objetivos para o futuro" to "Compartilhem suas aspirações e construam planos em conjunto para o futuro.",
        "Façam um piquenique em um parque ou praia" to "Preparem uma cesta de alimentos deliciosos e desfrutem de uma refeição ao ar livre em meio à natureza.",
        "Escolham um livro para ler juntos e discutam sobre ele depois" to "Compartilhem o prazer da leitura e troquem ideias e reflexões sobre a história.",
        "Façam uma lista de reprodução de músicas que são significativas para vocês" to "Relembrem momentos especiais e criem novas memórias ao som das músicas que amam.",
        "Visitem um local turístico na cidade onde nunca foram juntos" to "Descubram novos lugares e explorem juntos a beleza e a história da sua cidade."
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)


        database = FirebaseDatabase.getInstance()
        reviewRef = database.getReference("review")
        val textViewReminder: TextView = findViewById(R.id.textViewReminder)
        textViewReminder.visibility = View.VISIBLE

        val entradaDesafio = MapaDeDesafios.entries.random()
        val Desafio = entradaDesafio.key
        val dicaDesafio = entradaDesafio.value


        val textViewDesafio: TextView = findViewById(R.id.textViewDesafio)
        textViewDesafio.text = Desafio

        val TextoDica: TextView = findViewById(R.id.textViewDica)
        TextoDica.text = "Dica: $dicaDesafio"


        val botaoEnviar: Button = findViewById(R.id.buttonSubmit)
        botaoEnviar.setOnClickListener {
            enviarAvaliacao()
        }
    }

    private fun enviarAvaliacao() {

        val editTextReview: EditText = findViewById(R.id.editTextReview)
        val review = editTextReview.text.toString()


        val barraAvaliacao: RatingBar = findViewById(R.id.ratingBar)
        val avaliacao = barraAvaliacao.rating


        enviarReviewParaFirebase(review, avaliacao)
    }

    private fun enviarReviewParaFirebase(review: String, avaliacao: Float) {

        val reviewData = hashMapOf(
            "review" to review,
            "avaliacao" to avaliacao
        )


        val key = reviewRef.push().key ?: return
        reviewRef.child(key).setValue(reviewData)
    }
}