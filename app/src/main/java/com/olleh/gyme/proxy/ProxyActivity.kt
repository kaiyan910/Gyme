package com.olleh.gyme.proxy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.olleh.gyme.splashing.ui.SplashingActivity

class ProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val intent = Intent(this, SplashingActivity::class.java)
        startActivity(intent)
    }
}