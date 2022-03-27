package com.example.firebasetopics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        supportActionBar?.hide()

        val name = intent.getStringExtra("Name")
        val age = intent.getStringExtra("Age")
        val city = intent.getStringExtra("City")

        updateName.setText(name)
        updateAge.setText(age)
        updateCity.setText(city)

        // Getting New Values
        val newName = updateName.text
        val newAge = updateAge.text
        val newCity = updateCity.text

        updateBtn.setOnClickListener {
            updateData(newName.toString(), newAge.toString(), newCity.toString())
        }

    }

    fun updateData(name: String, age: String, city: String) {
        val updatedData = hashMapOf<String, Any>("Name" to name, "Age" to age, "City" to city)
        var userId: String?

        db = FirebaseFirestore.getInstance()

        // Getting Current user id
        db.collection("Users").whereEqualTo("Name", intent.getStringExtra("Name")).get()
            .addOnCompleteListener {
                userId = it.result?.documents?.get(0)?.id
//                Toast.makeText(this, "id = $userId", Toast.LENGTH_LONG).show()

                db.collection("Users").document(userId!!)
                    .update(updatedData)
                    .addOnCompleteListener {
                        Log.d("updatedData", "name = $name , city = $city , age = $age")
                        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.d("updatedData", "name = $name , city = $city , age = $age")
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                    }

            }
    }

}