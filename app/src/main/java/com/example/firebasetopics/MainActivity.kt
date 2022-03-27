package com.example.firebasetopics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        db = FirebaseFirestore.getInstance()

        val etName = name.text
        val etAge = age.text
        val etCity = city.text

        register.setOnClickListener(View.OnClickListener {
            addData(etName.toString(), etCity.toString(), etAge.toString())
            name.text.clear()
            age.text.clear()
            city.text.clear()
        })

        showData.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@MainActivity, DataActivity::class.java))
        })

//        readData()

    }

    fun addData(Name: String, City: String, Age: String) {

        val data = HashMap<String, String>()
        data.put("Name", Name)
        data.put("Age", Age)
        data.put("City", City)

        db.collection("Users").add(data).addOnSuccessListener {
            Log.d("addingData", "$Name , $City , $Age")
            Toast.makeText(this, "Data added successful", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(OnFailureListener {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            Log.d("addingData", "Failed")
        })

    }

    fun readData() {

        db.collection("Users").whereEqualTo("Age", "20").get()
            .addOnSuccessListener {
                Toast.makeText(this, "Reading Successful", Toast.LENGTH_SHORT).show()
                for (document in it) {
                    Log.d("readingData", "${document.id} -> ${document.data}")
                }
            }
            .addOnFailureListener(OnFailureListener {
                Log.d("readingData", "")
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            })

    }

}