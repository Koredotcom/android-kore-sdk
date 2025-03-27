package com.kore.ui.welcome.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.databinding.WelcomeHeader2Binding

class WelcomeHeaderTwoFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    lateinit var binding: WelcomeHeader2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = WelcomeHeader2Binding.inflate(layoutInflater)

        if (arguments != null) {
            binding.tvWelcomeHeader.text = arguments?.getString(BotResponseConstants.KEY_TITLE)
            binding.tvWelcomeTitle.text = arguments?.getString(BotResponseConstants.KEY_SUB_TITLE)
            binding.tvWelcomeDescription.text = arguments?.getString(BotResponseConstants.NOTE)
            binding.llInnerHeader.backgroundTintList =
                arguments?.getString(BotResponseConstants.BACKGROUND_COLOR)?.toColorInt()?.let { ColorStateList.valueOf(it) };
        };

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
    }

}