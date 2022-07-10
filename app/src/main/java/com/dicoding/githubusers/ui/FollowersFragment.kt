package com.dicoding.githubusers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.R
import com.dicoding.githubusers.databinding.FragmentFollowersBinding
import com.dicoding.githubusers.model.FollowersViewModel
import com.dicoding.githubusers.ui.adapter.UserAdapter

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val viewModelFragment: FollowersViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        _binding = FragmentFollowersBinding.inflate(inflater, container, false)


        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(activity)
            rvUsers.adapter = adapter
        }

        showLoading(true)
        viewModelFragment.setListFollowersUser(username)
        viewModelFragment.getFollowersUser().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setListUser(it)
                showLoading(false)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}