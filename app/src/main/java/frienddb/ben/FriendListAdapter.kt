package frienddb.ben

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class FriendListAdapter( private val friendList: List<Friend> ) :
    RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

        private var dataset: MutableList<Friend> = friendList.toMutableList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.adapter_friend_list, parent, false)
            )
        }


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val firstName = view.findViewById<EditText>(R.id.name)!!
            val rating = view.findViewById<EditText>(R.id.rating)!!
            val btnModifier = view.findViewById<Button>(R.id.btnModifier)!!
            val btnSupprimer = view.findViewById<Button>(R.id.btnSupprimer)!!
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val friend = dataset[position]
            holder.firstName.setText( friend.firstName )
            holder.rating.setText( friend.rating.toString() )

            holder.btnModifier.setOnClickListener {
                val editFriend = Friend(
                    uid = dataset[position].uid,
                    firstName = holder.firstName.text.toString(),
                    rating = holder.rating.text.toString().toInt()
                )
                eventListener?.onFriendEdit(editFriend)
            }

            holder.btnSupprimer.setOnClickListener { eventListener?.onFriendDelete(dataset[position]) }
        }

        fun updateData( friendList: List<Friend> ) {
            dataset.clear()
            dataset.addAll(friendList)
            notifyDataSetChanged()
        }

        override fun getItemCount() = dataset.size

        private var eventListener: EventListener? = null
        fun setEventListener(eventListener: EventListener) {
            this.eventListener = eventListener
        }


        interface EventListener {
            fun onFriendEdit(friend: Friend)
            fun onFriendDelete(friend: Friend)
        }
}