package com.gustavo.projeto.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.gustavo.projeto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnCrud1.setOnClickListener {
            val intent = Intent(this, Crud1Activity::class.java)

            startActivity(intent)
        }

        binding.btnSair.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this, SignInActivity::class.java)

            startActivity(intent)
        }
    }
}