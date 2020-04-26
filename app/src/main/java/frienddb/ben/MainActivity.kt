package frienddb.ben

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Room
            .databaseBuilder(
                this,
                FriendDatabase::class.java, "friend_database"
            ).allowMainThreadQueries()
            .build()


        val viewManager = LinearLayoutManager(this)
        val viewAdapter = FriendListAdapter(database.friendDao().getAllFriends())
        fun refreshAdapter() = viewAdapter.updateData(database.friendDao().getAllFriends())

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        findViewById<Button>(R.id.btnAjouter).setOnClickListener {
            var allFriends = database.friendDao().getAllFriends()
            allFriends.forEach{
                if (it.firstName == ""){
                    database.friendDao().deleteFriend(it)
                }
            }

            database.friendDao().insertFriend(Friend(firstName = "", rating = 0))
            refreshAdapter()
        }

        viewAdapter.setEventListener(
            object: FriendListAdapter.EventListener {
                override fun onFriendEdit(friend: Friend) {
                    database.friendDao().updateFriend(friend)
                    refreshAdapter()
                }

                override fun onFriendDelete(friend: Friend) {
                    database.friendDao().deleteFriend(friend)
                    refreshAdapter()
                }
            }
        )



    }
}
