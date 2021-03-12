package com.andy8786.poor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.andy8786.poor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        intent?.handleIntent()
    }

    private fun Intent.handleIntent(){
        when(action){
            Intent.ACTION_VIEW -> handleDeepLink(data)
            else->{
                Log.d("action", action.toString())
            }
        }
    }

    private fun handleDeepLink(data: Uri?){
        //handle the actions
        when(data?.path)
        {
            "/access" -> {   //for open app features
                val featureType = data?.getQueryParameter("featureName").orEmpty()
                mainBinding.inputField.setText("Feature Type: $featureType");
            }
            "/navigation" -> {  //for custom query location features
                val locationName = data?.getQueryParameter("locationName").orEmpty()
                mainBinding.inputField.setText("Location Name: $locationName");
            }
        }

    }
}