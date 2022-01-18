package com.development.messangerchatapp.messages

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.development.messangerchatapp.LatestMessageAdapter
import com.development.messangerchatapp.R
import com.development.messangerchatapp.models.ChatMessage
import com.development.messangerchatapp.models.User
import com.development.messangerchatapp.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class LatestMessagesActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }

    val mLatestMessageList: MutableList<ChatMessage> = ArrayList()
    private var mLatestMessageRV: androidx.recyclerview.widget.RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        findViewById<RecyclerView>(R.id.recyclerview_latest_messages)

        listenForLatestMessages()

        initViews(mLatestMessageList)

        fetchCurrentUser()

        verifyUserIsLoggedIn()

    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        //mLatestMessageList.clear()
        latestMessagesMap.values.forEach {
            mLatestMessageList.add(it)
        }
    }
    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                mLatestMessageList.add(chatMessage)
              //  initViews(mLatestMessageList)
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                mLatestMessageList.add(chatMessage)
                // initViews(mLatestMessageList)
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    public fun initViews(mLatestMessageList: MutableList<ChatMessage>){

        mLatestMessageRV?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        var mAdapter = LatestMessageAdapter(this,mLatestMessageList)
      //  mAdapter.setNewMessageList(mLatestMessageList)
        mLatestMessageRV?.adapter = mAdapter
//        mAdapter.setOnItemClickListner(object: NewMessageAdapter.onItemClickListner{
//            override fun onItemClick(position: Int) {
//                val userItem = mNewMessageList1
//
//
//                val gson = Gson()
//                val intent = Intent(applicationContext, ChatLogActivity::class.java)
//
//                intent.putExtra(NewMessageActivity.USER_KEY, mNewMessageList1[position])
//                startActivity(intent)
//                finish()
//            }
//
//        })

    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
