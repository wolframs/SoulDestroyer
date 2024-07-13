package souldestroyer.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector

interface Screen {
    val label: String
    val route: String
    val iconImageVector: ImageVector
    var selected: MutableState<Boolean>
}
