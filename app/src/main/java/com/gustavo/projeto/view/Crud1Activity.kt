package com.gustavo.projeto.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gustavo.projeto.databinding.ActivityCrud1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gustavo.projeto.model.Crud1Model

class Crud1Activity(
    val criarCrud: Boolean = false
) : AppCompatActivity() {
    private lateinit var binding: ActivityCrud1Binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var localizacao: String? = null

    private var reiniciarView: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        setupSpinner()

        firebaseAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")

        binding.btnVoltar.setOnClickListener {
            if (reiniciarView) {
                val intent = Intent(this, Crud1Activity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        if (criarCrud) {
            inserirView()
            reiniciarView = true
        }

        binding.btnCreateFunc.setOnClickListener {
            if (binding.btnCreateFunc.isEnabled) {
                inserirView()
                reiniciarView = true
            }
        }

        binding.btnCreate.setOnClickListener {
            storeCrud1()
        }

        binding.btnList.setOnClickListener {
            val intent = Intent(this, Crud1ListActivity::class.java)
            startActivity(intent)
        }

        binding.btnLocalizacao.setOnClickListener {
            val intent = Intent(this, Crud1MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAP && resultCode == RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", -29.444310)
            val longitude = data?.getDoubleExtra("longitude", -51.955876)
            localizacao = "$latitude, $longitude"
        }
    }

    private fun inserirView() {
        binding.txtName.visibility = View.VISIBLE
        binding.txtDescricao.visibility = View.VISIBLE
        binding.spinnerAtividade.visibility = View.VISIBLE
        binding.btnLocalizacao.visibility = View.VISIBLE
        binding.btnCreate.visibility = View.VISIBLE

        binding.btnCreateFunc.visibility = View.INVISIBLE
        binding.btnList.visibility = View.INVISIBLE
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

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, activities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActivityType.adapter = adapter
    }

    private fun storeCrud1() {
        val crudName = binding.txtName.text.toString()
        val crudDescricao = binding.txtDescricao.text.toString()
        val crudLocalizacao = localizacao.toString()
        val crudCategoria = binding.spinnerAtividade.selectedItem.toString()
        val crudImpacto = calcularImpactoPegadaCarbono(crudCategoria)
        val crudUserId = firebaseAuth.currentUser?.uid
        val crudId = dbRef.push().key!!

        val crudObject = Crud1Model(crudId, crudName, crudDescricao, crudLocalizacao, crudCategoria, crudImpacto, crudUserId)

        dbRef.child(crudId).setValue(crudObject)
            .addOnCompleteListener {
                Toast.makeText(this, "Inserido com sucesso", Toast.LENGTH_LONG).show()

                binding.txtName.text?.clear()
                binding.txtDescricao.text?.clear()
            }.addOnCanceledListener {
                Toast.makeText(this, "Operação cancelada", Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        const val REQUEST_CODE_MAP = 1
    }

    fun calcularImpactoPegadaCarbono(atividade: String): Double {
        return when (atividade) {
            "Reciclagem e Gerenciamento de Resíduos" -> 0.5 // Exemplo de impacto em toneladas de CO2
            "Conservação de Recursos Naturais" -> 1.0
            "Mobilidade Sustentável" -> 0.8
            "Consumo Consciente e Local" -> 0.6
            "Educação e Engajamento" -> 0.3
            else -> 0.0 // Valor padrão caso a atividade não seja encontrada
        }
    }
}
