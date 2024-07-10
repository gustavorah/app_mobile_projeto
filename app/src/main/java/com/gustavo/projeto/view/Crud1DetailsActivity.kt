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
    private var localizacao: String? = null
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

        binding.btnLocalizacao.setOnClickListener {
            val intent = Intent(this, Crud1MapActivity::class.java)
            startActivityForResult(intent, Crud1Activity.REQUEST_CODE_MAP)
        }

        btnUpdate.setOnClickListener {
            // Obtenha os novos valores dos campos editáveis
            val id = etCrud1Id.text.toString()
            val nome = etCrud1Name.text.toString()
            val descricao = etCrud1Descricao.text.toString()
            val localizacaoFinal = localizacao ?: etCrud1Localizacao.text.toString()
            val categoria = spCrud1Categoria.selectedItem.toString()

            updateCrud1(id, nome, descricao, localizacaoFinal, categoria)

            // Após salvar as alterações, você pode retornar à lista ou mostrar uma mensagem de sucesso
            val intent = Intent(this, Crud1ListActivity::class.java)
            startActivity(intent)
        }

        setValuesToView()
    }

    private fun updateCrud1(id: String, nome: String, descricao: String, localizacaoFinal: String, categoria: String) {
        val impacto = calcularImpactoPegadaCarbono(categoria)

        val crud1Details = mapOf(
            "nome" to nome,
            "descricao" to descricao,
            "localizacao" to localizacaoFinal,
            "category" to categoria,
            "impacto" to impacto
        )

        dbRef.child(id).updateChildren(crud1Details).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Update successful")
            } else {
                task.exception?.let {
                    println("Update failed: ${it.message}")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Crud1Activity.REQUEST_CODE_MAP && resultCode == RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", -29.444310)
            val longitude = data?.getDoubleExtra("longitude", -51.955876)
            localizacao = "$latitude, $longitude"
            etCrud1Localizacao.setText(localizacao)
        }
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
