package com.dicoding.githubusers.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.R
import com.dicoding.githubusers.databinding.ActivityMainBinding
import com.dicoding.githubusers.db.data.User
import com.dicoding.githubusers.model.UserViewModel
import com.dicoding.githubusers.ui.adapter.UserAdapter
import com.dicoding.mydatastore.model.MainViewModel
import com.dicoding.mydatastore.model.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(2000)
        installSplashScreen()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = SettingsPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        showLoading(true)
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
                showLoading(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
                showLoading(false)
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        mainBinding.apply {
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = userAdapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            etSearch.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                } else {
                    return@setOnKeyListener false
                }
            }
        }

        viewModel.getSearchUsers().observe(this) {
            if (it != null) {
                if (it.size == 0) {
                    showSnackbarMessage("User Not found")
                } else {
                    userAdapter.setListUser(it)
                }
                showLoading(false)
            }
        }
    }

    private fun searchUser() {
        mainBinding.apply {
            val search = etSearch.text.toString()
            if (search.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(search)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            mainBinding.progressBar.visibility = View.VISIBLE
        } else {
            mainBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(mainBinding.btnSearch, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}