package com.gustavo.projeto.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gustavo.projeto.databinding.ActivityCrud2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gustavo.projeto.model.Crud2Model

class Crud2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCrud2Binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        setupSpinner()

        firebaseAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Crud2")

        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, Crud2ListActivity::class.java)
            startActivity(intent)
        }

        binding.btnCreate.setOnClickListener {
            storeCrud2()
        }
    }

    private fun setupSpinner() {
        val spinnerCategory: Spinner = binding.spinnerCategoria

        val categories = listOf(
            "Reciclagem e Gerenciamento de Resíduos",
            "Conservação de Recursos Naturais",
            "Mobilidade Sustentável",
            "Consumo Consciente e Local",
            "Educação e Engajamento"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun storeCrud2() {
        val quantidade = binding.txtQuantidade.text.toString()
        val category = binding.spinnerCategoria.selectedItem.toString()

        if (quantidade.isNotEmpty() && category.isNotEmpty()) {
            val userId = firebaseAuth.currentUser?.uid
            val crudId = dbRef.push().key!!

            val crudObject = Crud2Model(crudId, quantidade, category, userId)

            dbRef.child(crudId).setValue(crudObject)
                .addOnCompleteListener {
                    Toast.makeText(this, "Inserido com sucesso", Toast.LENGTH_LONG).show()
                    binding.txtQuantidade.text?.clear()
                }.addOnCanceledListener {
                    Toast.makeText(this, "Operação cancelada", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
        }
    }
}