package com.onenote.activity

import com.onenote.prefereces.Preferences
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.onenote.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(), View.OnClickListener {

    val preferences = Preferences()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Bereits zugestimmt?
        if(preferences.getAgreed(this) == "1")
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }else {
            //init button
            btnAgree.setOnClickListener(this)

            //set Text
            tvTitel.setText(R.string.app_name)
        }
    }

    override fun onClick(v: View?) {

    val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        preferences.setAgreed(this,"1")

}

}




