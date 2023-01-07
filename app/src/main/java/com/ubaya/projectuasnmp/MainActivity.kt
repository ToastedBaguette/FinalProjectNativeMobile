package com.ubaya.projectuasnmp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_meme.view.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_layout.view.*

class MainActivity : AppCompatActivity() {
    val fragments:ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up drawer layout
        setContentView(R.layout.drawer_layout)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        var drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,
                R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = this.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )

        val headView = navView.getHeaderView(0)
        headView.txtFullName.text = shared.getString("firstName", "") + " " + shared.getString("lastName", "")
        headView.txtUsername.text = shared.getString("username", "")

        val urlProf = "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1085&q=80"
        Picasso.get().load(urlProf).into(headView.imgProfile)

        val urlBack = "https://www.tutorialspoint.com/opencv/images/gaussian_blur.jpg"
        Picasso.get().load(urlBack).into(headView.imgBackground)

        navView.setNavigationItemSelectedListener {
            viewpager.currentItem  = when(it.itemId) {
                R.id.itemHome -> 0
                R.id.itemMyCreation -> 1
                R.id.itemLeaderboard -> 2
                R.id.itemSettings -> 3
                else -> 0
            }
            true
        }



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