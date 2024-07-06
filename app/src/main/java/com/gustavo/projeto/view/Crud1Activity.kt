package com.gustavo.projeto.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gustavo.projeto.databinding.ActivityCrud1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gustavo.projeto.model.Crud1Model
import kotlin.properties.Delegates

class Crud1Activity(
    val criarCrud: Boolean = false
) : AppCompatActivity() {
    private lateinit var binding: ActivityCrud1Binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private var reiniciarView: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")

//        val crud1 = intent.getSerializableExtra("crud1") as? Crud1Model
//
//        crud1?.let {
//            val id = it.id
//            val nome = it.nome
//            val email = it.email
//            val idade = it.idade
//        }
//        reiniciarView = false
        binding.btnVoltar.setOnClickListener {
            if (reiniciarView)
            {
                val intent = Intent(this, Crud1Activity::class.java)

                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
            }
        }

        if (criarCrud)
        {
            binding.txtName.visibility = View.VISIBLE
            binding.txtIdade.visibility = View.VISIBLE
            binding.txtEmail.visibility = View.VISIBLE
            binding.btnCreateFunc.visibility = View.INVISIBLE
            binding.btnList.visibility = View.INVISIBLE

            binding.btnCreate.visibility = View.VISIBLE
            reiniciarView = true
        }

        binding.btnCreateFunc.setOnClickListener {
            if (binding.btnCreateFunc.isEnabled)
            {
                binding.txtName.visibility = View.VISIBLE
                binding.txtIdade.visibility = View.VISIBLE
                binding.txtEmail.visibility = View.VISIBLE
                binding.btnCreateFunc.visibility = View.INVISIBLE
                binding.btnList.visibility = View.INVISIBLE

                binding.btnCreate.visibility = View.VISIBLE
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
    }

    private fun storeCrud1()
    {
        val crudName = binding.txtName.text.toString()
        val crudEmail = binding.txtEmail.text.toString()
        val crudIdade = binding.txtIdade.text.toString()

        val crudId = dbRef.push().key!!

        val crudObject = Crud1Model(crudId, crudName, crudEmail, crudIdade)

        dbRef.child(crudId).setValue(crudObject)
            .addOnCompleteListener {
                Toast.makeText(this, "Inserido com sucesso", Toast.LENGTH_LONG).show()

                binding.txtName.text?.clear()
                binding.txtEmail.text?.clear()
                binding.txtIdade.text?.clear()

            }.addOnCanceledListener {
                Toast.makeText(this, "Operação cancelada", Toast.LENGTH_LONG).show()
            }
    }


}