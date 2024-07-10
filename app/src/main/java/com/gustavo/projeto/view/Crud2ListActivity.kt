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
import com.gustavo.projeto.adapter.Crud2Adapter
import com.gustavo.projeto.databinding.ActivityCrud2ListBinding
import com.gustavo.projeto.model.Crud1Model
import com.gustavo.projeto.model.Crud2Model

class Crud2ListActivity : AppCompatActivity(), Crud2Adapter.OnItemClickListener {
    private lateinit var crudRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var crud2List: ArrayList<Crud2Model>
    private lateinit var crud1List: ArrayList<Crud1Model>
    private lateinit var dbRefCrud2: DatabaseReference
    private lateinit var dbRefCrud1: DatabaseReference
    private lateinit var binding: ActivityCrud2ListBinding
    private lateinit var mAdapter: Crud2Adapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud2ListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        crudRecyclerView = binding.rvCrud2
        crudRecyclerView.layoutManager = LinearLayoutManager(this)
        crudRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(binding.tvLoadingData.id)

        crud2List = arrayListOf()
        crud1List = arrayListOf()

        getCrud1Data {
            getCrud2Data()
        }

        binding.btnVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnInserir.setOnClickListener {
            val intent = Intent(this, Crud2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun getCrud1Data(onComplete: () -> Unit) {
        val currentUser = firebaseAuth.currentUser?.uid
        dbRefCrud1 = FirebaseDatabase.getInstance().getReference("Crud1")

        val query = dbRefCrud1.orderByChild("userId").equalTo(currentUser)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                crud1List.clear()
                if (snapshot.exists()) {
                    for (crudSnap in snapshot.children) {
                        val crudData = crudSnap.getValue(Crud1Model::class.java)
                        crud1List.add(crudData!!)
                    }
                    onComplete()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getCrud2Data() {
        val currentUser = firebaseAuth.currentUser?.uid
        crudRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRefCrud2 = FirebaseDatabase.getInstance().getReference("Crud2")

        val query = dbRefCrud2.orderByChild("userId").equalTo(currentUser)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                crud2List.clear()
                if (snapshot.exists()) {
                    for (crudSnap in snapshot.children) {
                        val crudData = crudSnap.getValue(Crud2Model::class.java)
                        crud2List.add(crudData!!)
                    }
                    mAdapter = Crud2Adapter(crud2List, crud1List)
                    crudRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(this@Crud2ListActivity)

                    crudRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(position: Int, viewHolder: RecyclerView.ViewHolder, itemId: String) {
        val selectedItem = crud2List[position]
        val btnDeletar = getDeleteButton(viewHolder as Crud2Adapter.ViewHolder)
        val btnEditar = getEditButton(viewHolder)
        btnDeletar.setOnClickListener {
            deleteCrudItem(itemId, position)
        }

        btnEditar.setOnClickListener {
            editCrudItem(selectedItem)
        }
    }

    fun editCrudItem(selectedItem: Crud2Model) {
        val intent = Intent(this, Crud2DetailsActivity::class.java)
        intent.putExtra("id", selectedItem.id)
        intent.putExtra("quantidade", selectedItem.quantidade)
        intent.putExtra("category", selectedItem.category)
        startActivity(intent)
    }

    fun deleteCrudItem(itemId: String, position: Int) {
        dbRefCrud2.child(itemId).removeValue().addOnSuccessListener {
            crud2List.removeAt(position)
            mAdapter.notifyItemRemoved(position)
        }.addOnFailureListener {
            // Handle deletion failure (optional)
            // For example, display a toast message
        }
    }

    private fun getDeleteButton(viewHolder: Crud2Adapter.ViewHolder): ImageButton {
        return viewHolder.btnDeletar
    }

    private fun getEditButton(viewHolder: Crud2Adapter.ViewHolder): ImageButton {
        return viewHolder.btnEditar
    }
}