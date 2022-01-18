package com.development.messangerchatapp.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.development.messangerchatapp.NewMessageAdapter
import com.development.messangerchatapp.R
import com.development.messangerchatapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.io.Serializable
import java.util.*

class NewMessageActivity : AppCompatActivity() {


    val mNewMessageList1: MutableList<User> = ArrayList()

//    @BindView(R.id.recyclerview_newmessage)
//    var recyclerViewOutstanding: RecyclerView? = null
    private var mNewMessageRV: androidx.recyclerview.widget.RecyclerView? = null
    //private lateinit var mAdapter: NewMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
        mNewMessageRV=findViewById(R.id.recyclerview_newmessage)

      //  val recyclerview_newmessage=findViewById<RecyclerView>(R.id.recyclerview_newmessage)

        fetchUsers()
    }
    companion object {
        val USER_KEY = "USER_KEY"
    }


    // Configuring both RecyclerViews
    fun initViews(mNewMessageList1: MutableList<User>){

        mNewMessageRV?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        var mAdapter = NewMessageAdapter(this)
        mAdapter.setNewMessageList(mNewMessageList1)
        mNewMessageRV?.adapter = mAdapter
        mAdapter.setOnItemClickListner(object:NewMessageAdapter.onItemClickListner{
            override fun onItemClick(position: Int) {
                val userItem = mNewMessageList1


                val gson = Gson()
                val intent = Intent(applicationContext, ChatLogActivity::class.java)

                intent.putExtra(USER_KEY, mNewMessageList1[position])
                startActivity(intent)
                finish()

              //  val intent = Intent(this@NewMessageActivity, ChatLogActivity::class.java)
//                val args = Bundle()
//                args.putParcelableArrayList("ARRAYLIST", mNewMessageList1)
//                intent.putExtra(USER_KEY, args)
//                startActivity(intent)
//                finish()

               // val intent = Intent(this@NewMessageActivity, ChatLogActivity::class.java)
//          intent.putExtra(USER_KEY,  userItem.user.username)
               // intent.putExtra(USER_KEY, userItem)
               // intent.putParcelableArrayListExtra(USER_KEY,mNewMessageList1 as Serializable)
                //startActivity(intent)

               // finish()
            }

        })

    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
//                val adapter = Adapter<ViewHolder>()
//                val arrayList= ArrayList<User>()
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    var user = it.getValue(User::class.java)
                    Log.d("NewMessage", user!!.username+" "+user.uid)
                    if (user != null) {
                        //NewMessageAdapter(applicationContext,user)
                        mNewMessageList1.add(user)
                        Log.d("msg1", mNewMessageList1.toString())
                    }
                }
                initViews(mNewMessageList1)


                }

//                val recyclerview_newmessage=findViewById<Adapter>(R.id.recyclerview_newmessage)
//                recyclerview_newmessage.adapter= adapter


            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

//class UserItem(val user: User): Item<ViewHolder>() {
//    override fun bind(viewHolder: ViewHolder, position: Int) {
//
//        viewHolder.itemView.username_textview_new_message.text = user.username
//
//        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
//    }

//    override fun getLayout(): Int {
//        return R.layout.user_row_new_message
//    }


// this is super tedious

//class CustomAdapter: RecyclerView.Adapter<ViewHolder> {
//  override fun onBindViewHolder(p0:, p1: Int) {
//    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//  }
//}
