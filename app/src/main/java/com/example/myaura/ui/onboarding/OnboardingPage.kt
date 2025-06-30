package com.example.myaura.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.myaura.R

data class OnboardingPage(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        image = R.drawable.onboarding1,
        title = R.string.onboarding_title_1,
        description = R.string.onboarding_desc_1
    ),
    OnboardingPage(
        image = R.drawable.onboarding2,
        title = R.string.onboarding_title_2,
        description = R.string.onboarding_desc_2
    ),
    OnboardingPage(
        image = R.drawable.onboarding3,
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_desc_3
    )
)