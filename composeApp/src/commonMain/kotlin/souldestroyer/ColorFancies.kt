package souldestroyer

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

object ColorFancies {
    fun modColor(color: Color, brightenBy: Float? = null, desaturateBy: Float? = null): Color {
        // Convert Color to HSV
        val hsv = rgbToHsv(color.red, color.green, color.blue)

        // Get current hue, saturation, and value (brightness)
        val hue = hsv[0]
        val saturation = hsv[1]
        val value = hsv[2]

        // Adjust brightness (value)
        val brighterValue = brightenBy?.let { (value * (1f + it)).coerceIn(0f..1f) } ?: value

        // Adjust saturation
        val desaturatedSaturation = desaturateBy?.let { (saturation * (1f - it)).coerceIn(0f..1f) } ?: saturation

        // Convert modified HSV back to Color
        return hsvToColor(hue, desaturatedSaturation, brighterValue)
    }

    private fun rgbToHsv(r: Float, g: Float, b: Float): FloatArray {
        val max = max(r, max(g, b))
        val min = min(r, min(g, b))
        val delta = max - min

        val hue = when {
            delta == 0f -> 0f
            max == r -> 60 * (((g - b) / delta) % 6)
            max == g -> 60 * (((b - r) / delta) + 2)
            else -> 60 * (((r - g) / delta) + 4)
        }

        val saturation = if (max == 0f) 0f else delta / max
        val value = max

        return floatArrayOf(hue.coerceIn(0f, 360f), saturation.coerceIn(0f, 1f), value.coerceIn(0f, 1f))
    }

    private fun hsvToColor(h: Float, s: Float, v: Float): Color {
        val c = v * s
        val x = c * (1 - kotlin.math.abs((h / 60) % 2 - 1))
        val m = v - c

        val (r, g, b) = when {
            h in 0f..60f -> Triple(c, x, 0f)
            h in 60f..120f -> Triple(x, c, 0f)
            h in 120f..180f -> Triple(0f, c, x)
            h in 180f..240f -> Triple(0f, x, c)
            h in 240f..300f -> Triple(x, 0f, c)
            h in 300f..360f -> Triple(c, 0f, x)
            else -> Triple(0f, 0f, 0f)
        }

        return Color((r + m), (g + m), (b + m), 1f)
    }
}
