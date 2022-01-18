import com.development.messangerchatapp.R

//val context:Context.messangerchatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.messangerchatapp.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth



 class ChatLogAdapter(val context: Context, val listitemChat: MutableList<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_SENDER = 1
    private val TYPE_RECIVER = 2

     class toViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chatMessage: ChatMessage) {
            itemView.findViewById<TextView>(R.id.textView)?.text = chatMessage.text
            //itemView.findViewById<TextView>(R.id.tvRelationship)?.text = item.relationship
            }
        }
     class fromViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         fun bind(chatMessage: ChatMessage) {
             itemView.findViewById<TextView>(R.id.textView)?.text = chatMessage.text

         }
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         if (viewType == TYPE_SENDER) {
             val v: View = LayoutInflater.from(parent.context)
                 .inflate(R.layout.chat_sender_row, parent, false)
             return fromViewHolder(v)
         }
//         } elif(viewType == TYPE_FROM) {
         else {
             val v: View = LayoutInflater.from(parent.context)
                 .inflate(R.layout.chat_reciver_row, parent, false)
             return toViewHolder(v)
         }
     }

     override fun getItemViewType(position: Int): Int {
         if (listitemChat.get(position).id.equals(FirebaseAuth.getInstance().uid))
             return TYPE_SENDER
         else
             return TYPE_RECIVER
     }

     override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         val chatMessage=listitemChat.get(position)
         if(getItemViewType(position)==TYPE_SENDER){
             (holder as fromViewHolder).bind(chatMessage)
         }
         else{
             (holder as toViewHolder).bind(chatMessage)
         }

//        if (getItemViewType(position) == TYPE_FROM) {
//            (holder as fromViewHolder).bind(listitemChat[position])
//        } else {
//
//        }
     }

     override fun getItemCount(): Int {
         return listitemChat.size
     }
 }


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_FROM) {
//            val v: View = LayoutInflater.from(parent.context)
//                .inflate(R.layout.chat_from_row, parent, false)
//            return fromViewHolder(v)
//        }
////         } elif(viewType == TYPE_FROM) {
//        val v: View = LayoutInflater.from(parent.context)
//            .inflate(R.layout.chat_to_row, parent, false)
//        return toViewHolder(v)
//    }

//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//           val chatMessage=listitemChat.get(position)
//           if(getItemViewType(position)==TYPE_FROM){
//               (holder as fromViewHolder).bind(chatMessage)
//
//           }
//
//
//
//
//
////        if (getItemViewType(position) == TYPE_FROM) {
////            (holder as fromViewHolder).bind(listitemChat[position])
////        } else {
////            (holder as toViewHolder).bind(listitemChat[position])
////        }
//    }

//    override fun getItemCount(): Int {
//         return listitemChat.size
//    }

//    override fun getItemViewType(position: Int): Int {
//
//        if (listitemChat.get(position).id.equals(FirebaseAuth.getInstance().uid))
//            return TYPE_FROM
//        else
//            return TYPE_TO
//    }

//}


//public class ChatLogAdapter {
//
//    public class rcv:RecyclerView.ViewHolder {
//        public rcv(View:itemView){
//    }
//
////    public class ReciverViewHolder : RecyclerView.Adapter<ReciverViewHolder.MyViewHolder>()  {
////        override fun onCreateViewHolder(
////            parent: ViewGroup,
////            viewType: Int
////        ): ReciverViewHolder.MyViewHolder {
////
////        }
////
//
//    }
//
//}