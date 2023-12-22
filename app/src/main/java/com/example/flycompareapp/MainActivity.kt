@file:Suppress("DEPRECATION")

package com.example.flycompareapp

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.flycompareapp.injectors.HomeFragment

class MainActivity : ComponentActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Inject HomeFrament in place of home_content Frame Layout in MainActivity.xml
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.home_content, HomeFragment())
        transaction.addToBackStack(null);
        transaction.commit()


   }
}