package com.development.messangerchatapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.development.messangerchatapp.messages.ChatLogActivity
import com.development.messangerchatapp.messages.NewMessageActivity
import com.development.messangerchatapp.models.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class NewMessageAdapter(val context: Context): RecyclerView.Adapter<NewMessageAdapter.MyViewHolder>() {
    var mnewMessageList: MutableList<User> = ArrayList()

    private lateinit var mListner:onItemClickListner
    interface onItemClickListner{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListner(listner:onItemClickListner){
        mListner=listner
    }


    fun setNewMessageList(newMessageList: MutableList<User>) {
        mnewMessageList = newMessageList
        Log.d("size", mnewMessageList.size.toString())
    }


     inner class MyViewHolder(itemView: View,listner: onItemClickListner) :RecyclerView.ViewHolder(itemView) {
        val imageViewNewMessage:CircleImageView
         val usernameTextView:TextView

         init {
             imageViewNewMessage = itemView.findViewById(R.id.imageview_new_message)
             usernameTextView = itemView.findViewById(R.id.username_textview_new_message)
         }
         init {
             itemView.setOnClickListener(View.OnClickListener {
                 listner.onItemClick(adapterPosition)
             })
         }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewMessageAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.user_row_new_message,parent,false),mListner
        )
    }

    override fun onBindViewHolder(holder: NewMessageAdapter.MyViewHolder, position: Int) {
        val model: User = mnewMessageList.get(position)
        Log.d("onbind1", model.username.toString())
        holder.usernameTextView.setText(model.username)
        Picasso.get().load(model.profileImageUrl).into(holder.imageViewNewMessage)

    }

    override fun getItemCount(): Int {
         return mnewMessageList.size
    }
}