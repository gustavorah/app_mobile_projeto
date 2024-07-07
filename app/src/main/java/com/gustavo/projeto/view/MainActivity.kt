package com.gustavo.projeto.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gustavo.projeto.databinding.ActivityMainBinding
import com.gustavo.projeto.model.Crud1ImpactoModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbRefImpacto: DatabaseReference
    private var saldoImpacto: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")
        dbRefImpacto = FirebaseDatabase.getInstance().getReference("Impacto")

        getUserImpacto()

        binding.btnCrud1.setOnClickListener {
            startActivity(Intent(this, Crud1Activity::class.java))
        }

        binding.btnCalcular.setOnClickListener {
            calcularPegadaCarbono()
        }

        binding.btnSair.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun getUserImpacto() {
        val query = dbRefImpacto.orderByChild("userId").equalTo(firebaseAuth.currentUser?.uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val impacto = data.child("impacto").getValue(Double::class.java)
                        if (impacto != null) {
                            saldoImpacto = impacto
                            binding.txtImpacto.text = "$saldoImpacto tCO2e"

                        }
                        else {
                            Toast.makeText(applicationContext, "Sem atividades registradas para calcular sua pegada de carbono!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Erro ao ler dados do Firebase: ${error.message}")
            }
        })
    }

    private fun calcularPegadaCarbono() {
        val currentUser = firebaseAuth.currentUser?.uid

        // Inicializar saldoImpacto antes de usar
        saldoImpacto = 0.0

        val query = dbRef.orderByChild("userId").equalTo(currentUser)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val impacto = data.child("impacto").getValue(Double::class.java)
                        if (impacto != null) {
                            saldoImpacto += impacto
                        }
                    }

                    // Atualizar ou criar o registro no banco de dados
                    if (currentUser != null) {
                        val impactoQuery = dbRefImpacto.orderByChild("userId").equalTo(currentUser)

                        impactoQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Já existe um registro, atualize-o
                                    val existingKey = dataSnapshot.children.firstOrNull()?.key
                                    if (existingKey != null) {
                                        dbRefImpacto.child(currentUser)
                                            .updateChildren(mapOf("impacto" to saldoImpacto))
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    binding.txtImpacto.text = "$saldoImpacto tCO2e"
                                                    Toast.makeText(applicationContext, "Pegada de carbono atualizada com sucesso", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(applicationContext, "Erro ao atualizar pegada de carbono", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    }
                                } else {
                                    // Não existe um registro, crie um novo
                                    val crudId = dbRefImpacto.push().key!!
                                    val impactoObject = Crud1ImpactoModel(crudId, saldoImpacto, currentUser)

                                    dbRefImpacto.child(currentUser).setValue(impactoObject)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                binding.txtImpacto.text = "$saldoImpacto tCO2e"
                                                Toast.makeText(applicationContext, "Pegada de carbono calculada e salva com sucesso", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(applicationContext, "Erro ao salvar pegada de carbono", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                println("Erro ao ler dados do Firebase: ${error.message}")
                            }
                        })
                    }
                } else {
                    Toast.makeText(applicationContext, "Não há dados para calcular a pegada de carbono", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Erro ao ler dados do Firebase: ${error.message}")
            }
        })
    }
}
