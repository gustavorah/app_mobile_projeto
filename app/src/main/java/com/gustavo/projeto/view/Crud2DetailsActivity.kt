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
import com.gustavo.projeto.databinding.ActivityCrud2DetailsBinding

class Crud2DetailsActivity : AppCompatActivity() {
    private lateinit var etCrud2Id: EditText
    private lateinit var etCrud2Quantidade: EditText
    private lateinit var spCrud2Categoria: Spinner
    private lateinit var btnUpdate: Button
    private lateinit var btnVoltar: Button
    private lateinit var binding: ActivityCrud2DetailsBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud2DetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("Crud2")

        initView()

        setupSpinner()

        btnVoltar.setOnClickListener {
            val intent = Intent(this, Crud2ListActivity::class.java)
            startActivity(intent)
        }

        btnUpdate.setOnClickListener {
            // Obtenha os novos valores dos campos editáveis
            val id = etCrud2Id.text.toString()
            val quantidade = etCrud2Quantidade.text.toString()
            val categoria = spCrud2Categoria.selectedItem.toString()

            updateCrud2(id, quantidade, categoria)

            // Após salvar as alterações, você pode retornar à lista ou mostrar uma mensagem de sucesso
            val intent = Intent(this, Crud2ListActivity::class.java)
            startActivity(intent)
        }

        setValuesToView()
    }

    private fun updateCrud2(id: String, quantidade: String, categoria: String) {
        val crud2Details = mapOf(
            "quantidade" to quantidade,
            "category" to categoria
        )

        dbRef.child(id).updateChildren(crud2Details).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Update successful")
            } else {
                task.exception?.let {
                    println("Update failed: ${it.message}")
                }
            }
        }
    }

    private fun initView() {
        etCrud2Id = binding.tvCrud2Id
        etCrud2Quantidade = binding.tvCrud2Quantidade
        spCrud2Categoria = binding.spinnerCategory

        btnUpdate = binding.btnUpdate
        btnVoltar = binding.btnVoltar
    }

    private fun setValuesToView() {
        etCrud2Id.setText(intent.getStringExtra("id"))
        etCrud2Quantidade.setText(intent.getStringExtra("quantidade"))
        spCrud2Categoria.setSelection(adapter.getPosition(intent.getStringExtra("category")))
    }

    private fun setupSpinner() {
        val spinnerCategory: Spinner = binding.spinnerCategory

        val categories = listOf(
            "Reciclagem e Gerenciamento de Resíduos",
            "Conservação de Recursos Naturais",
            "Mobilidade Sustentável",
            "Consumo Consciente e Local",
            "Educação e Engajamento"
        )

        adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }
}