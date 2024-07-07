package com.gustavo.projeto.view

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gustavo.projeto.databinding.ActivityCrud1DetailsBinding

class Crud1DetailsActivity : AppCompatActivity() {
    private lateinit var etCrud1Id: EditText
    private lateinit var etCrud1Name: EditText
    private lateinit var etCrud1Descricao: EditText
    private lateinit var etCrud1Localizacao: EditText
    private lateinit var spCrud1Categoria: Spinner
    private lateinit var btnUpdate: Button
    private lateinit var btnVoltar: Button
    private lateinit var binding: ActivityCrud1DetailsBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1DetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")

        initView()

        setupSpinner()

        btnVoltar.setOnClickListener {
            val intent = Intent(this, Crud1ListActivity::class.java)
            startActivity(intent)
        }

        btnUpdate.setOnClickListener {
            // Obtenha os novos valores dos campos editáveis
            val id = etCrud1Id.text.toString()
            val nome = etCrud1Name.text.toString()
            val descricao = etCrud1Descricao.text.toString()
            val localizacao = etCrud1Localizacao.text.toString()
            val categoria = spCrud1Categoria.selectedItem.toString()

            // FirebaseRepository.updateCrud1(id, nome, descricao, localizacao, categoria)

            // Após salvar as alterações, você pode retornar à lista ou mostrar uma mensagem de sucesso
            val intent = Intent(this, Crud1ListActivity::class.java)
            startActivity(intent)
        }

        setValuesToView()
    }

    private fun initView() {
        etCrud1Id = binding.tvCrud1Id
        etCrud1Name = binding.tvCrud1Name
        etCrud1Descricao = binding.tvCrud1Descricao
        etCrud1Localizacao = binding.tvCrud1Localizacao
        spCrud1Categoria = binding.spinnerAtividade

        btnUpdate = binding.btnUpdate
        btnVoltar = binding.btnVoltar
    }

    private fun setValuesToView() {
        etCrud1Id.setText(intent.getStringExtra("id"))
        etCrud1Name.setText(intent.getStringExtra("nome"))
        etCrud1Descricao.setText(intent.getStringExtra("descricao"))
        etCrud1Localizacao.setText(intent.getStringExtra("localizacao"))
        spCrud1Categoria.setSelection(adapter.getPosition(intent.getStringExtra("category")))
    }

    private fun setupSpinner() {
        val spinnerActivityType: Spinner = binding.spinnerAtividade

        val activities = listOf(
            "Reciclagem e Gerenciamento de Resíduos",
            "Conservação de Recursos Naturais",
            "Mobilidade Sustentável",
            "Consumo Consciente e Local",
            "Educação e Engajamento"
        )

        adapter = ArrayAdapter(this, R.layout.simple_spinner_item, activities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActivityType.adapter = adapter
    }
}
