package com.dicoding.githubusers.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.databinding.ActivityFavoriteBinding
import com.dicoding.githubusers.db.data.FavoriteUser
import com.dicoding.githubusers.model.FavoriteViewModel
import com.dicoding.githubusers.db.data.User
import com.dicoding.githubusers.ui.adapter.UserAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favBinding: ActivityFavoriteBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favBinding.root)

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        favBinding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUsers.adapter = userAdapter
        }

        viewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                userAdapter.setListUser(list)
            }
        }

        supportActionBar?.title = "Favorite Users"
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for ((id, login, avatar_url) in users) {
            val userMapped = User(login, id, avatar_url)
            listUsers.add(userMapped)
        }
        return listUsers
    }
}