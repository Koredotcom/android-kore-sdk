package kore.botssdk.common

import kotlin.math.roundToInt

data class AspectRatio(

    val width: Int,

    val height: Int
) {

    companion object {

        private const val MULTIPLE = 10000

        private const val ASPECT_RATIO_16_9 = 16f / 9f

        private const val ASPECT_RATIO_9_16 = 9f / 16f

        private const val ASPECT_RATIO_1_1 = 1f

        fun Float.floatToRatio(
            maxHorizontalAspectRatio: Float = ASPECT_RATIO_16_9,
            maxVerticalAspectRatio: Float = ASPECT_RATIO_9_16,
            defaultAspectRatio: Float = ASPECT_RATIO_1_1
        ): AspectRatio = when {
            this == 0f -> defaultAspectRatio.floatToRatio()
            this > maxHorizontalAspectRatio -> maxHorizontalAspectRatio.floatToRatio()
            this < maxVerticalAspectRatio -> maxVerticalAspectRatio.floatToRatio()
            else -> AspectRatio((this * MULTIPLE).roundToInt(), MULTIPLE)
        }
    }
}