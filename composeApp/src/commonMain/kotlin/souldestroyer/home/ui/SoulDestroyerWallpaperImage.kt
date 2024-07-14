package souldestroyer.home.ui

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.imageResource
import souldestroyer.composeapp.generated.resources.Res
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_1
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_2
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_3
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_4
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_5
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_6
import souldestroyer.composeapp.generated.resources.souldestroyer_wallpaper_7

@Composable
fun SoulDestroyerWallpaperImage(
    modifier: Modifier = Modifier
) {
    // List of drawable wallpaper resource IDs
    val wallpaperIds = listOf(
        Res.drawable.souldestroyer_wallpaper_1,
        Res.drawable.souldestroyer_wallpaper_2,
        Res.drawable.souldestroyer_wallpaper_3,
        Res.drawable.souldestroyer_wallpaper_4,
        Res.drawable.souldestroyer_wallpaper_5,
        Res.drawable.souldestroyer_wallpaper_6,
        Res.drawable.souldestroyer_wallpaper_7
    )

    // Select a random wallpaper ID
    val randomWallpaperId = remember { wallpaperIds.random() }
    val imageResource = imageResource(randomWallpaperId)

    Image(
        imageResource,
        contentDescription = "Soul Destroyer Wallpaper",
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
    )
}