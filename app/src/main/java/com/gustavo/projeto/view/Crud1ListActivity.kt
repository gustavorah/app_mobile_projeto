package com.gustavo.projeto.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gustavo.projeto.adapter.Crud1Adapter
import com.gustavo.projeto.databinding.ActivityCrud1ListBinding
import com.gustavo.projeto.model.Crud1Model

class Crud1ListActivity : AppCompatActivity() {
    private lateinit var crudRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var crudList: ArrayList<Crud1Model>
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: ActivityCrud1ListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrud1ListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        crudRecyclerView = binding.rvCrud1
        crudRecyclerView.layoutManager = LinearLayoutManager(this)
        crudRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(binding.tvLoadingData.id)

        crudList = arrayListOf<Crud1Model>()

        getCrudData()
    }

    private fun getCrudData()
    {
        crudRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Crud1")

        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                crudList.clear()
                if (snapshot.exists()) {
                    for (crudSnap in snapshot.children) {
                        val crudData = crudSnap.getValue(Crud1Model::class.java)
                        crudList.add(crudData!!)
                    }
                    val mAdapter = Crud1Adapter(crudList)
                    crudRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : Crud1Adapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@Crud1ListActivity, Crud1DetailsActivity::class.java)

                            intent.putExtra("id", crudList[position].id)
                            intent.putExtra("nome", crudList[position].nome)
                            intent.putExtra("email", crudList[position].email)
                            intent.putExtra("idade", crudList[position].idade)
                        }
                    })

                    crudRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}