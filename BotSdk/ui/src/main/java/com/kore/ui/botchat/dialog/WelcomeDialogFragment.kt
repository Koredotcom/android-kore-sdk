package com.kore.ui.botchat.dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection.COLUMN
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_CAROUSEL
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_LIST
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.network.api.responsemodels.branding.BrandingWelcomeModel
import com.kore.ui.BR
import com.kore.ui.R
import com.kore.ui.adapters.WelcomePromotionsAdapter
import com.kore.ui.adapters.WelcomeStarterButtonsAdapter
import com.kore.ui.adapters.WelcomeStaticLinksAdapter
import com.kore.ui.adapters.WelcomeStaticLinksListAdapter
import com.kore.ui.base.BaseDialogFragment
import com.kore.ui.databinding.WelcomeScreenBinding

class WelcomeDialogFragment(private val botBrandingModel: BotBrandingModel) :
    BaseDialogFragment<WelcomeScreenBinding, WelcomeDialogView, WelcomeDialogViewModel>(),
    WelcomeDialogView {
    private val welcomeDialogViewModel: WelcomeDialogViewModel by viewModels()
    private var listener: WelcomeDialogListener? = null

    override fun getLayoutID(): Int = R.layout.welcome_screen

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): WelcomeDialogViewModel = welcomeDialogViewModel

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.llStartConversation.setOnClickListener {
            listener?.onUpdateUI()
            dismiss()
        }

        if (!botBrandingModel.welcomeScreen?.layout.isNullOrEmpty()) {
            if (botBrandingModel.general.colors.useColorPaletteOnly == true) {
                botBrandingModel.welcomeScreen?.background?.color = botBrandingModel.general.colors.primary
                binding.llStartConversation.backgroundTintList =
                    botBrandingModel.general.colors.primary?.toColorInt()?.let { ColorStateList.valueOf(it) }
                binding.svWelcome.backgroundTintList = botBrandingModel.general.colors.secondary?.toColorInt()?.let { ColorStateList.valueOf(it) }
                binding.llStarterLogo.backgroundTintList = botBrandingModel.general.colors.primary?.toColorInt()?.let { ColorStateList.valueOf(it) }
            }

            when (botBrandingModel.welcomeScreen?.layout) {
                BotResponseConstants.HEADER_SIZE_LARGE -> applyFragment(2, botBrandingModel.welcomeScreen)
                BotResponseConstants.HEADER_SIZE_MEDIUM -> applyFragment(3, botBrandingModel.welcomeScreen)
                else -> applyFragment(1, botBrandingModel.welcomeScreen)
            }
        }

        if (!botBrandingModel.welcomeScreen?.promotionalContent?.promotions.isNullOrEmpty()) {
            binding.lvPromotions.isVisible = true
            binding.lvPromotions.adapter = WelcomePromotionsAdapter(
                requireContext(), botBrandingModel.welcomeScreen?.promotionalContent?.promotions!!
            )
        }

        if (!botBrandingModel.welcomeScreen?.starterBox?.title.isNullOrEmpty()) {
            binding.tvStarterTitle.isVisible = true
            binding.tvStarterTitle.text = botBrandingModel.welcomeScreen?.starterBox?.title
        }

        if (!botBrandingModel.welcomeScreen?.starterBox?.subText.isNullOrEmpty()) {
            binding.tvStarterDesc.isVisible = true
            binding.tvStarterDesc.text = botBrandingModel.welcomeScreen?.starterBox?.subText
        }

        if (botBrandingModel.header.icon != null) {
            if (botBrandingModel.header.icon?.type == (BotResponseConstants.CUSTOM)) {
                binding.llStarterLogo.setBackgroundResource(0)
                Glide.with(requireActivity()).load(botBrandingModel.header.icon?.iconUrl)
                    .apply(RequestOptions.bitmapTransform(CircleCrop())).into(binding.ivStarterLogo)
                    .onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_foreground, context?.theme))
                binding.ivStarterLogo.layoutParams = LinearLayout.LayoutParams((40.dpToPx(binding.root.context)), (40.dpToPx(binding.root.context)))
            } else {
                when (botBrandingModel.header.icon?.iconUrl) {
                    BotResponseConstants.ICON_1 -> binding.ivStarterLogo.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_1, context?.theme)
                    )

                    BotResponseConstants.ICON_2 -> binding.ivStarterLogo.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_2, context?.theme)
                    )

                    BotResponseConstants.ICON_3 -> binding.ivStarterLogo.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_3, context?.theme)
                    )

                    BotResponseConstants.ICON_4 -> binding.ivStarterLogo.setImageDrawable(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_4, context?.theme)
                    )
                }
            }
        }

        if (!botBrandingModel.welcomeScreen?.starterBox?.quickStartButtons?.buttons.isNullOrEmpty()) {
            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.justifyContent = JustifyContent.FLEX_START

            when (botBrandingModel.welcomeScreen?.starterBox?.quickStartButtons?.style) {
                TEMPLATE_TYPE_LIST -> layoutManager.flexDirection = COLUMN
                else -> layoutManager.flexDirection = ROW
            }

            binding.rvStarterButtons.layoutManager = layoutManager
            val adapter = WelcomeStarterButtonsAdapter(requireContext(), botBrandingModel.welcomeScreen?.starterBox?.quickStartButtons?.style)
            adapter.setWelcomeStarterButtonsArrayList(botBrandingModel.welcomeScreen?.starterBox?.quickStartButtons?.buttons!!)
            binding.rvStarterButtons.adapter = adapter
        }

        if (!botBrandingModel.welcomeScreen?.staticLinks?.links.isNullOrEmpty()) {
            binding.rlLinks.isVisible = true

            when (botBrandingModel.welcomeScreen?.staticLinks?.layout) {
                TEMPLATE_TYPE_CAROUSEL -> {
                    binding.hvpLinks.isVisible = true
                    binding.rvLinks.isVisible = false
                    binding.hvpLinks.adapter = WelcomeStaticLinksAdapter(requireContext(), botBrandingModel.welcomeScreen?.staticLinks?.links!!)
                }

                else -> {
                    binding.hvpLinks.isVisible = false
                    binding.rvLinks.isVisible = true
                    binding.rvLinks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.rvLinks.adapter =
                        WelcomeStaticLinksListAdapter(requireContext(), botBrandingModel.welcomeScreen?.staticLinks?.links!!)
                }
            }
        } else {
            binding.rvLinks.isVisible = false
        }
    }

    private fun applyFragment(key: Int, header: BrandingWelcomeModel?) {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bundle = Bundle()
        bundle.putString(BotResponseConstants.KEY_TITLE, header?.title?.name)
        bundle.putString(BotResponseConstants.KEY_SUB_TITLE, header?.subTitle?.name)
        bundle.putString(BotResponseConstants.NOTE, header?.note?.name)
        bundle.putString(BotResponseConstants.BACKGROUND_COLOR, header?.background?.color)
        bundle.putString(BotResponseConstants.BACKGROUND_IMAGE, header?.background?.img)
        bundle.putString(BotResponseConstants.LOGO_URL, header?.logo?.logoUrl)

        when (key) {
            1 -> navController.navigate(R.id.welcomeHeaderOne, bundle)
            2 -> navController.navigate(R.id.welcomeHeaderTwo, bundle)
            3 -> navController.navigate(R.id.welcomeHeaderThree, bundle)
        }
    }

    fun setListener(listener: WelcomeDialogListener) {
        this.listener = listener
    }

    interface WelcomeDialogListener {
        fun onUpdateUI()
    }
}