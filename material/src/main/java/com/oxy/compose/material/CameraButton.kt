package com.oxy.compose.material

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CameraButton(
    shooting: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: CameraButtonColors = CameraButtonDefaults.colors(),
    scale: CameraButtonScale = CameraButtonDefaults.scale(),
    size: Dp = CameraButtonDefaults.size,
    strokeWidthPercentInRadius: Float = CameraButtonDefaults.strokeWidthPercentInRadius
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentColor by colors.current(shooting)
    val currentScale by scale.current(shooting, isPressed)

    val actualPercent = strokeWidthPercentInRadius.coerceIn(0f..1f)

    Canvas(
        Modifier
            .clickable(
                interactionSource = interactionSource,
                // show no indication
                indication = null,
                onClick = onClick
            )
            .size(size)
            .then(modifier)
    ) {
        val shortSide = with(this.size) { min(width, height) }
        drawCircle(
            color = currentColor,
            radius = currentScale * shortSide / 2
        )
        drawCircle(
            color = currentColor,
            style = Stroke(
                width = shortSide * actualPercent * 2
            )
        )
    }
}

@Immutable
data class CameraButtonColors(
    val color: Color,
    val shootColor: Color,
    val animationSpec: AnimationSpec<Color>
) {
    @Composable
    fun current(
        shooting: Boolean,
        label: String = "camera-button-colors-current"
    ): State<Color> {
        return animateColorAsState(
            targetValue = if (shooting) shootColor else color,
            label = label,
            animationSpec = animationSpec
        )
    }
}

@Immutable
data class CameraButtonScale(
    val default: Float,
    val target: Float,
    val animationSpec: AnimationSpec<Float>
) {
    @Composable
    fun current(
        shooting: Boolean,
        isPressed: Boolean,
        label: String = "camera-button-current-scale"
    ): State<Float> {
        return animateFloatAsState(
            targetValue = if (isPressed || shooting) 0.65f else 0.85f,
            label = label,
            animationSpec = animationSpec
        )
    }
}

object CameraButtonDefaults {
    val size: Dp = 56.dp
    const val strokeWidthPercentInRadius: Float = 0.02f
    fun colors(
        color: Color = Color.White,
        shootColor: Color = Color(0xffffc773),
        animationSpec: AnimationSpec<Color> = spring()
    ): CameraButtonColors {
        return CameraButtonColors(color, shootColor, animationSpec)
    }

    fun scale(
        default: Float = 0.85f,
        target: Float = 0.65f,
        animationSpec: AnimationSpec<Float> = spring()
    ): CameraButtonScale {
        return CameraButtonScale(default, target, animationSpec)
    }
}

@Composable
@Preview
private fun CameraButtonPreview() {
    val scope = rememberCoroutineScope()
    var shooting by remember { mutableStateOf(false) }
    fun shoot() {
        shooting = true
        scope.launch {
            delay(800.milliseconds)
            shooting = false
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        CameraButton(
            shooting = shooting,
            onClick = ::shoot
        )
    }
}
