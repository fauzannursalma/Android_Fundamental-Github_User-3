package com.dicoding.githubusers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.githubusers.R
import com.dicoding.githubusers.databinding.ActivityDetailUserBinding
import com.dicoding.githubusers.model.DetailUserViewModel
import com.dicoding.githubusers.ui.adapter.SectionPagerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailUserBinding
    private val viewModel: DetailUserViewModel by viewModels()
    private lateinit var sectionPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        showLoading(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                showLoading(false)
                detailBinding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvRepository.text = " ${it.publicRepos} Repos"
                    tvLocation.text = it.location
                    tvFollowers.text = " ${it.followers} Followers"
                    tvFollowing.text = " ${it.following} Following"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivAvatar)
                }
            }
        }

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkFavoriteUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        detailBinding.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        detailBinding.toggleFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager, lifecycle, bundle)
        val viewPager: ViewPager2 = detailBinding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = detailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TABLAYOUT_TITLES[position])
        }.attach()

        detailBinding.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                if (username != null && avatarUrl != null) {
                    viewModel.addFavoriteUser(username, id, avatarUrl)
                    showSnackbarMessage("Save to Favorite Users")
                }
            } else {
                viewModel.removeFavoriteUser(id)
                showSnackbarMessage("Remove from Favorite Users")
            }
            detailBinding.toggleFavorite.isChecked = isChecked
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail User"
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            detailBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(detailBinding.toggleFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

        @StringRes
        private val TABLAYOUT_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)

    }

}