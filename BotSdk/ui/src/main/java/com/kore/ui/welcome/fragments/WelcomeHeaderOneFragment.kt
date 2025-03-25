package com.kore.ui.welcome.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.databinding.WelcomeHeaderBinding

class WelcomeHeaderOneFragment : Fragment() {

    lateinit var binding: WelcomeHeaderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = WelcomeHeaderBinding.inflate(layoutInflater)

        if (arguments != null) {
            binding.tvWelcomeHeader.text = arguments?.getString(BotResponseConstants.KEY_TITLE)
            binding.tvWelcomeTitle.text = arguments?.getString(BotResponseConstants.KEY_SUB_TITLE)
            binding.tvWelcomeDescription.text = arguments?.getString(BotResponseConstants.NOTE)
            binding.rlHeader.backgroundTintList =
                arguments?.getString(BotResponseConstants.BACKGROUND_COLOR)?.toColorInt()?.let { ColorStateList.valueOf(it) };
        };

        return binding.root
    }
}