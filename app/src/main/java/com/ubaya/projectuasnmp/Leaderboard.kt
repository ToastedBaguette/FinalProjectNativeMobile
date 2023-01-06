package com.ubaya.projectuasnmp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Leaderboard(val fullname:String, val imgAvatar:String, val nums_like:Int, val privacy:Int) : Parcelable
