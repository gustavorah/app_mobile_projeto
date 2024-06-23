package com.gustavo.projeto.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gustavo.projeto.databinding.ActivityCrud1DetailsBinding

class Crud1DetailsActivity : AppCompatActivity() {
    private lateinit var tvCrud1Id: TextView
    private lateinit var tvCrud1Name: TextView
    private lateinit var tvCrud1Idade: TextView
    private lateinit var tvCrud1Email: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var binding: ActivityCrud1DetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1DetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setValuesToView()
    }

    private fun initView() {
        tvCrud1Id = binding.tvCrud1Id // findViewById(binding.tvCrud1Id.id)
        tvCrud1Name = binding.tvCrud1Name // findViewById(binding.tvCrud1Name.id)
        tvCrud1Idade = binding.tvCrud1Idade //findViewById(binding.tvCrud1Idade.id)
        tvCrud1Email = binding.tvCrud1Email //findViewById(binding.tvCrud1Email.id)

        btnUpdate = binding.btnUpdate
        btnDelete = binding.btnDelete
    }

    private fun setValuesToView() {
        tvCrud1Id.text = intent.getStringExtra("id")
        tvCrud1Name.text = intent.getStringExtra("nome")
        tvCrud1Idade.text = intent.getStringExtra("idade")
        tvCrud1Email.text = intent.getStringExtra("email")
    }
}
