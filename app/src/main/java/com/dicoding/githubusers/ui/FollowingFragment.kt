package com.dicoding.githubusers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.R
import com.dicoding.githubusers.databinding.FragmentFollowingBinding
import com.dicoding.githubusers.model.FollowingViewModel
import com.dicoding.githubusers.ui.adapter.UserAdapter

class FollowingFragment : Fragment(R.layout.fragment_following) {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val viewModelFragment: FollowingViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        _binding = FragmentFollowingBinding.inflate(inflater, container, false)


        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(activity)
            rvUsers.adapter = adapter
        }

        showLoading(true)
        viewModelFragment.setListFollowingUsers(username)
        viewModelFragment.getFollowingUsers().observe(viewLifecycleOwner) {
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