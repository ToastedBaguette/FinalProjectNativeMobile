package com.ubaya.projectuasnmp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val fragments:ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragments.add(HomeFragment())
        fragments.add(MyCreationFragment())
        fragments.add(LeaderboardFragment())
        fragments.add(SettingsFragment())

        val adapter = MenuAdapter(this, fragments)
        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu.getItem(position).itemId
            }
        })

        bottomNav.setOnItemSelectedListener {
            viewpager.currentItem  = when(it.itemId) {
                R.id.itemHome -> 0
                R.id.itemMyCreation -> 1
                R.id.itemLeaderboard -> 2
                R.id.itemSettings -> 3
                else -> 0
            }
            true
        }
    }
}