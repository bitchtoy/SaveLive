package com.example.zoneker.savelive.modle

import java.io.Serializable

/**
 * Created by Zoneker on 2017/6/13.
 */
data class UserData(var shift : String,var shift_cn : String,var date : String,var msgs: ArrayList<Msgs>) : Serializable