package com.njves.schoolnetwork.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.R


class ReceiversAdapter(val context : Context?, private val listUsers : List<Profile>) :
    RecyclerView.Adapter<ReceiversAdapter.ReceiverHolder>() {
    private var selectedItems = ArrayList<Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_receiver_user, parent, false)
        return ReceiverHolder(view)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    override fun onBindViewHolder(holder: ReceiverHolder, position: Int) {
        holder.bind(position)
        selectedItems.add(position, false)

    }
    fun getUser() : Profile?
    {

        val index = selectedItems.indexOf(true)
        Log.d("ReceiversAdapter", "$index")
        return if(index==-1) {
            null
        } else{
            listUsers[index]
        }

    }
    inner class ReceiverHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val tvAvatar = itemView.findViewById<ImageView>(R.id.ivAva)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvPos = itemView.findViewById<TextView>(R.id.tvPos)

        fun bind(i : Int)
        {
            val positions = listUsers[i].position
            var pos = ""
            when(positions){
                1->pos="Учитель"
                2->pos="Завуч"
            }
            val namePlaceholder = String.format(context?.resources?.getString(R.string.name_placeholder)!!,listUsers[i].firstName,listUsers[i].lastName)
            tvName.text = namePlaceholder
            tvPos.text = pos
            itemView.setOnClickListener{
                Log.d("ReceiversAdapter", selectedItems.toString())
                Log.d("ReceiversAdapter", listUsers.toString())
                if(selectedItems[adapterPosition] == false) {
                    itemView.setBackgroundColor(Color.GRAY)
                    Toast.makeText(context, "OnClickFocus, element " + adapterPosition, Toast.LENGTH_SHORT).show()
                    selectedItems[adapterPosition] = true
                }else{
                    itemView.setBackgroundColor(Color.WHITE)
                    Toast.makeText(context, "OnClickUnFocus", Toast.LENGTH_SHORT).show()
                    selectedItems[adapterPosition] = false
                }
            }
        }

    }



}