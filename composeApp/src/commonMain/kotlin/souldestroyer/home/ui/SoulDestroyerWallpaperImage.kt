package souldestroyer.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.imageResource
import souldestroyer.composeapp.generated.resources.Res
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_1
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_10
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_2
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_3
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_4
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_5
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_6
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_7
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_8
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_9

@Composable
fun SoulDestroyerWallpaperImage(
    modifier: Modifier = Modifier,
    onNextRandom: () -> Unit
) {
    // List of drawable wallpaper resource IDs
    val wallpaperIds = listOf(
        Res.drawable.souldestroyer_wallpaper_1,
        Res.drawable.souldestroyer_wallpaper_2,
        Res.drawable.souldestroyer_wallpaper_3,
        Res.drawable.souldestroyer_wallpaper_4,
        Res.drawable.souldestroyer_wallpaper_5,
        Res.drawable.souldestroyer_wallpaper_6,
        Res.drawable.souldestroyer_wallpaper_7,
        Res.drawable.souldestroyer_wallpaper_8,
        Res.drawable.souldestroyer_wallpaper_9,
        Res.drawable.souldestroyer_wallpaper_10
    )

    // Select a random wallpaper ID
    val randomWallpaperId = remember { mutableStateOf(wallpaperIds.random()) }
    var currentWallpaperId = randomWallpaperId.value

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {

        Image(
            imageResource(randomWallpaperId.value),
            contentDescription = "Soul Destroyer Wallpaper",
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.medium)
        )

        OutlinedButton(
            onClick = {
                randomWallpaperId.value = wallpaperIds.dropWhile { it == currentWallpaperId }.random()
                currentWallpaperId = randomWallpaperId.value
                onNextRandom()
            },
            modifier = Modifier.padding(6.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.175f),
                contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.63f)
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = SolidColor(MaterialTheme.colorScheme.background.copy(alpha = 0.35f))
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Casino,
                    contentDescription = "Random Home Wallpaper and Text"
                )
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}