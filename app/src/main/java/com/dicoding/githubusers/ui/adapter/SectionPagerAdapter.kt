package com.dicoding.githubusers.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubusers.ui.FollowersFragment
import com.dicoding.githubusers.ui.FollowingFragment

class SectionPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle, data: Bundle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragmentBundle: Bundle = data

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }

        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
        return fragment
    }
}