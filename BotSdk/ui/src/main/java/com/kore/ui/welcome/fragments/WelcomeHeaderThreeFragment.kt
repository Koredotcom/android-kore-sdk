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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.databinding.WelcomeHeader3Binding

class WelcomeHeaderThreeFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    lateinit var binding: WelcomeHeader3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeHeader3Binding.inflate(layoutInflater)

        if (arguments != null) {
            binding.tvWelcomeHeader.text = arguments?.getString(BotResponseConstants.KEY_TITLE)
            binding.tvWelcomeTitle.text = arguments?.getString(BotResponseConstants.KEY_SUB_TITLE)
            binding.tvWelcomeDescription.text = arguments?.getString(BotResponseConstants.NOTE)
            binding.llInnerHeader.backgroundTintList =
                arguments?.getString(BotResponseConstants.BACKGROUND_COLOR)?.toColorInt()?.let { ColorStateList.valueOf(it) };

            if (arguments?.getString(BotResponseConstants.LOGO_URL) != null) {
                Glide.with(requireActivity()).load(arguments?.getString(BotResponseConstants.LOGO_URL))
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.ivWelcomeLogo))
            }
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