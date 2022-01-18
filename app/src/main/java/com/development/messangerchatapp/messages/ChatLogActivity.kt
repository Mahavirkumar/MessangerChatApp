package com.development.messangerchatapp.messages

import ChatLogAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.development.messangerchatapp.NewMessageAdapter
import com.development.messangerchatapp.R
import com.development.messangerchatapp.messages.LatestMessagesActivity.Companion.currentUser
import com.development.messangerchatapp.messages.NewMessageActivity.Companion.USER_KEY
import com.development.messangerchatapp.models.ChatMessage
import com.development.messangerchatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.util.*


class ChatLogActivity : AppCompatActivity() {
    companion object {
        val TAG = "ChatLog"
    }
    var mAdapter: ChatLogAdapter? = null

    var toUser: User? = null
    val mChatLogList: MutableList<ChatMessage> = ArrayList()

    //val mChatLogListTo: MutableList<ChatMessage> = ArrayList()
    private var mChatLogRV: androidx.recyclerview.widget.RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

//    val username = intent.getStringExtra(NewMessageActivity.USER_KEY)

        mChatLogRV=findViewById<RecyclerView>(R.id.recyclerview_chat_log)
        toUser = intent.getParcelableExtra<User>(USER_KEY)
        val toId = toUser?.uid

        if (toUser != null) {
            supportActionBar?.title = toUser!!.username
        }

        listenForMessages()
        initViews(mChatLogList)
//        mChatLogRV?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
//        val mAdapter = ChatLogAdapter(this, mChatLogList)
//        Log.d("listsize", mChatLogList.size.toString())
//        //mAdapter.setNewMessageList(mNewMessageList1)
//        mChatLogRV?.adapter = mAdapter

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message....")
            performSendMessage()
        }
    }

    fun initViews(mChatLogList: MutableList<ChatMessage>) {

        mChatLogRV?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
         mAdapter = ChatLogAdapter(this, mChatLogList)
        //mAdapter.setNewMessageList(mNewMessageList1)
        Log.d("listsize", mChatLogList.size.toString())
        mChatLogRV?.adapter = mAdapter

    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val senderRoom: String = fromId + toId
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")
        //mChatLogList.clear()
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    mChatLogList.add(chatMessage)

                }

                if (chatMessage?.fromId == FirebaseAuth.getInstance().uid)
                     currentUser = LatestMessagesActivity.currentUser ?: return
                mAdapter?.notifyDataSetChanged()
               // initViews(mChatLogList)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

    private fun performSendMessage() {
        // how do we actually send a message to firebase...
        val text = edittext_chat_log.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        //val user=getIntent().getExtras()?.getParcelable<User>(USER_KEY)
        //   val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(USER_KEY)
        val toId = user?.uid

        if (fromId == null) return

        val senderRoom: String = fromId + toId
        val reciverRoom: String = toId + fromId

        val reference = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/messages/$toId/$fromId").push()
        //  val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        // System.currentTimeMillis() / 1000

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId!!,System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                edittext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(mAdapter?.itemCount?.minus(1)!!)
//                val reference =
//                    FirebaseDatabase.getInstance().getReference("/messages/$reciverRoom").push()
//                reference.setValue(chatMessage).addOnSuccessListener {
//                    Log.d(TAG, "recived our chat message: ${reference.key}")
 //               }
                Log.d(TAG, "Send our chat message: ${reference.key}")
            }
        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }

    //       val adapter = GroupAdapter<ViewHolder>()
//    private fun setupDummyData() {
//        val adapter = GroupAdapter<ViewHolder>()
//
//        adapter.add(ChatFromItem("FROM MESSSSSSSSAAGE"))
//        adapter.add(ChatToItem("TO MESSAGE\nTOMESSAGE"))
//        adapter.add(ChatFromItem("FROM MESSSSSSSSAAGE"))
//        adapter.add(ChatToItem("TO MESSAGE\nTOMESSAGE"))
//        adapter.add(ChatFromItem("FROM MESSSSSSSSAAGE"))
//        adapter.add(ChatToItem("TO MESSAGE\nTOMESSAGE"))
//        var recyclerview_chat_log=findViewById<RecyclerView>(R.id.recyclerview_chat_log)
//        recyclerview_chat_log.adapter = adapter
//
//
//    }
}




