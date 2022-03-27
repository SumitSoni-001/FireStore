package com.example.firebasetopics

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.camera2.params.LensShadingMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.sample_data.view.*
import java.util.zip.Inflater

class DataAdapter(private val context: Context, private val list: ArrayList<dataModel>) :
    RecyclerView.Adapter<DataAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_data, parent, false)
        return viewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val model = list[position]

        holder.name.text = model.Name
        holder.age.text = model.Age
        holder.city.text = model.City

        holder.layout.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("Name", model.Name.toString())
            intent.putExtra("Age", model.Age.toString())
            intent.putExtra("City", model.City.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun delete(pos : Int){

        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("Name", list[pos].Name)
            .whereEqualTo("City", list[pos].City).get()
            .addOnCompleteListener {
                val id = it.result?.documents?.get(0)?.id
                FirebaseFirestore.getInstance().collection("Users").document(id!!).delete()
            }

        list.removeAt(pos)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(pos : Int, data : dataModel){
        list.add(pos , data)
        notifyDataSetChanged()
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tvName
        val age = itemView.tvAge
        val city = itemView.tvCity
        val layout = itemView.parentLayout
    }

}