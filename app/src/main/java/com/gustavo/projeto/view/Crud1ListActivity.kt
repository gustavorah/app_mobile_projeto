package com.gustavo.projeto.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gustavo.projeto.adapter.Crud1Adapter
import com.gustavo.projeto.databinding.ActivityCrud1ListBinding
import com.gustavo.projeto.model.Crud1Model

class Crud1ListActivity : AppCompatActivity(), Crud1Adapter.OnItemClickListener {
    private lateinit var crudRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var crudList: ArrayList<Crud1Model>
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: ActivityCrud1ListBinding
    private lateinit var mAdapter: Crud1Adapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1ListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        crudRecyclerView = binding.rvCrud1
        crudRecyclerView.layoutManager = LinearLayoutManager(this)
        crudRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(binding.tvLoadingData.id)

        crudList = arrayListOf<Crud1Model>()

        getCrudData()

        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, Crud1Activity::class.java)
            startActivity(intent)
        }
    }

    private fun getCrudData() {
        val currentUser = firebaseAuth.currentUser?.uid
        crudRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")

        val query = dbRef.orderByChild("userId").equalTo(currentUser)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                crudList.clear()
                if (snapshot.exists()) {
                    for (crudSnap in snapshot.children) {
                        val crudData = crudSnap.getValue(Crud1Model::class.java)
                        crudList.add(crudData!!)
                    }
                    mAdapter = Crud1Adapter(crudList)
                    crudRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(this@Crud1ListActivity)

                    crudRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(position: Int, viewHolder: RecyclerView.ViewHolder, itemId: String) {
        val selectedItem = crudList[position]
        val btnDeletar = getDeleteButton(viewHolder as Crud1Adapter.ViewHolder)
        val btnEditar = getEditButton(viewHolder)
        btnDeletar.setOnClickListener {
            deleteCrudItem(itemId, position)
        }

        btnEditar.setOnClickListener {
            editCrudItem(selectedItem)

        }
    }

    fun editCrudItem(selectedItem: Crud1Model) {
        val intent = Intent(this, Crud1DetailsActivity::class.java)
        intent.putExtra("id", selectedItem.id)
        intent.putExtra("nome", selectedItem.nome)
        intent.putExtra("descricao", selectedItem.descricao)
        intent.putExtra("localizacao", selectedItem.localizacao)
        intent.putExtra("category", selectedItem.category)
        startActivity(intent)
    }

    fun deleteCrudItem(itemId: String, position: Int) {
        dbRef.child(itemId).removeValue().addOnSuccessListener {
            crudList.removeAt(position)
            mAdapter.notifyItemRemoved(position)
        }.addOnFailureListener {
            // Handle deletion failure (optional)
            // For example, display a toast message
        }
    }

    private fun getDeleteButton(viewHolder: Crud1Adapter.ViewHolder): ImageButton {
        return viewHolder.btnDeletar
    }

    private fun getEditButton(viewHolder: Crud1Adapter.ViewHolder): ImageButton {
        return viewHolder.btnEditar
    }
}
