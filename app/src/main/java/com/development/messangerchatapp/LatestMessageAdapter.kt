package com.development.messangerchatapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.messangerchatapp.models.ChatMessage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class LatestMessageAdapter(val context: Context, val mLatestMessageList: MutableList<ChatMessage>): RecyclerView.Adapter<LatestMessageAdapter.MyViewHolder>(){

    private lateinit var mListner:onItemClickListner
    interface onItemClickListner{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListner(listner:onItemClickListner){
        mListner=listner
    }


//    fun setNewMessageList(latestMessageList: MutableList<ChatMessage>) {
//        mLatestMessageList = latestMessageList
//        Log.d("size", mLatestMessageList.size.toString())
//    }


    inner class MyViewHolder(itemView: View, listner: onItemClickListner) : RecyclerView.ViewHolder(itemView) {
        val imageViewNewMessage: CircleImageView
        val usernameTextView: TextView
        val latestMsgTextView:TextView

        init {
            imageViewNewMessage = itemView.findViewById(R.id.imageview_latest_message)
            usernameTextView = itemView.findViewById(R.id.username_textview_latest_message)
            latestMsgTextView = itemView.findViewById(R.id.message_textview_latest_message)
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
    ): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.latest_meesage_row,parent,false),mListner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: ChatMessage = mLatestMessageList.get(position)
       // Log.d("onbind1", model.username.toString())
        holder.usernameTextView.setText(model.text)
       // Picasso.get().load(model.).into(holder.imageViewNewMessage)

    }

    override fun getItemCount(): Int {
        return mLatestMessageList.size
    }


}