package com.gustavo.projeto

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.gustavo.projeto.databinding.ActivityCrud1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Crud1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCrud1Binding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")

        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        binding.btnCreateFunc.setOnClickListener {
            if (binding.btnCreateFunc.isEnabled)
            {
                binding.txtName.visibility = View.VISIBLE
                binding.txtIdade.visibility = View.VISIBLE
                binding.txtEmail.visibility = View.VISIBLE
                binding.btnCreateFunc.visibility = View.INVISIBLE

                binding.btnCreate.visibility = View.VISIBLE
            }
        }

        binding.btnCreate.setOnClickListener {
            storeCrud1()
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