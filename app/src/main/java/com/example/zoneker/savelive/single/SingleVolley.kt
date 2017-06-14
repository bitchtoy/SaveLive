package com.example.zoneker.savelive.single

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by Zoneker on 2017/6/13.
 */
class SingleVolley private constructor(var ctx : Context){

    private var requestQueue : RequestQueue? = null
    //实例化请求队列
    private fun getRequestQueue() : RequestQueue? {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(ctx)
        return requestQueue
    }
    //添加求到请求队列
    fun <T> addToQueue(request: Request<T>){
        getRequestQueue()!!.add(request)
    }
    fun cancelAll(tag : String){
        getRequestQueue()!!.cancelAll(tag)
    }
    //同步代码块
    companion object{
        private var singleVolley : SingleVolley? = null
        @Synchronized fun getSingleVolley(ctx: Context) : SingleVolley{
            if (singleVolley == null) singleVolley = SingleVolley(ctx)
            return singleVolley as SingleVolley
        }
    }

}