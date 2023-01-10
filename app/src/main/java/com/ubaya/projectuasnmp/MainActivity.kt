package com.ubaya.projectuasnmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_layout.*

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
        var editor:SharedPreferences.Editor = shared.edit()

        val headView = navView.getHeaderView(0)
        headView.txtFullName.text = shared.getString("firstName", "") + " " + shared.getString("lastName", "")
        headView.txtUsername.text = shared.getString("username", "")
        headView.fabLogOutProf.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            editor.clear()
            editor.apply()
            this.startActivity(intent)
            this.finish()
        }

        val urlProf = shared.getString("avatarImg", "")
        if(urlProf != ""){
            Picasso.get().load(urlProf).into(headView.imgProfile)
        }

        val urlBack = "https://lumiere-a.akamaihd.net/v1/images/sa_pixar_virtualbg_up_16x9_1f36fba7.jpeg"

        Glide.with(this).load(urlBack)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(headView.imgBackground)

        navView.setNavigationItemSelectedListener {
            viewpager.currentItem  = when(it.itemId) {
                R.id.itemHome -> 0
                R.id.itemMyCreation -> 1
                R.id.itemLeaderboard -> 2
                R.id.itemSettings -> 3
                else -> 0
            }
            drawerLayout.closeDrawers()
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
                navView.setCheckedItem(bottomNav.menu.getItem(position).itemId)
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