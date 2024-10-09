package com.kore.ui.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.kore.ui.R

class WelcomeHeaderFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.welcome_header, container, false)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivWelcomeLogo -> navController.navigate(R.id.welcomeHeaderTwo)
        }
    }

}