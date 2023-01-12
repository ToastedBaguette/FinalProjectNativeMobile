package com.ubaya.projectuasnmp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.drawer_layout.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    val fragments:ArrayList<Fragment> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up Shared prefereneces
        var sharedFile = "com.ubaya.projectuasnmp"
        var shared: SharedPreferences = this.getSharedPreferences(sharedFile,
            Context.MODE_PRIVATE )
        var editor:SharedPreferences.Editor = shared.edit()

        //set up drawer layout
        setContentView(R.layout.drawer_layout)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        var drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name, R.string.app_name)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        //get head view from navigation and set up head view data
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
            Picasso.get().load(urlProf).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(headView.imgProfile)
        }

        var urlBack = "https://ubaya.fun/native/160420041/images/background_default.jpg"
        val q = Volley.newRequestQueue(this)
        val url = "https://ubaya.fun/native/160420041/get_background.php"
        var stringRequest = object:StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if(obj.getString("result")=="OK"){
                    val data = obj.getJSONArray("data")
                    for(i in 0 until data.length()){
                        val playObj = data.getJSONObject(i)
                        urlBack = playObj.getString("image_url")
                    }
                }

                Glide.with(this).load(urlBack)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .into(headView.imgBackground)
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["users_id"] = shared.getInt("userId",0).toString()
                return params
            }
        }
        q.add(stringRequest)

        //add fragment to main activity
        fragments.add(HomeFragment())
        fragments.add(MyCreationFragment())
        fragments.add(LeaderboardFragment())
        fragments.add(SettingsFragment())

        //set up navigation ref to fragment
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

        //set up bottom navigation ref to fragment
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

        //sync selected item from navigation and bottom navigation
        val adapter = MenuAdapter(this, fragments)
        viewpager.adapter = adapter
        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = bottomNav.menu.getItem(position).itemId
                navView.setCheckedItem(bottomNav.menu.getItem(position).itemId)
            }
        })
    }
}