package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Colors for Miru default theme.
 * Teal accent (#35D0D0) over charcoal, based on the Miru brand reference.
 *
 * Key colors:
 * Primary / Accent #35D0D0 (cyan-teal)
 * Background #0E1113 (charcoal)
 * Tertiary #7ADC77 (downloaded badge)
 */
internal object TachiyomiColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        primary = Color(0xFF35D0D0), // Miru teal accent
        onPrimary = Color(0xFF00201F), // Text on teal
        primaryContainer = Color(0xFF00504E),
        onPrimaryContainer = Color(0xFFA7F3EE),
        inversePrimary = Color(0xFF35D0D0),
        secondary = Color(0xFF35D0D0), // Unread badge / Accent
        onSecondary = Color(0xFF00201F), // Unread badge text
        secondaryContainer = Color(0xFF14181A), // Navigation bar selector pill
        onSecondaryContainer = Color(0xFFFFFFFF), // Navigation bar selector icon
        tertiary = Color(0xFF7ADC77), // Downloaded badge
        onTertiary = Color(0xFF003909), // Downloaded badge text
        tertiaryContainer = Color(0xFF005312),
        onTertiaryContainer = Color(0xFF95F990),
        background = Color(0xFF0E1113), // Charcoal
        onBackground = Color(0xFFE3E2E6),
        surface = Color(0xFF14181A), // Surface
        onSurface = Color(0xFFE3E2E6),
        surfaceVariant = Color(0xFF191E20), // Navigation bar background
        onSurfaceVariant = Color(0xFFC5C6D0),
        surfaceTint = Color(0xFF35D0D0),
        inverseSurface = Color(0xFFE3E2E6),
        inverseOnSurface = Color(0xFF1B1B1F),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        outline = Color(0xFF8F9099),
        outlineVariant = Color(0xFF44464F),
        surfaceContainerLowest = Color(0xFF0A0D0E),
        surfaceContainerLow = Color(0xFF101416),
        surfaceContainer = Color(0xFF14181A), // Navigation bar background
        surfaceContainerHigh = Color(0xFF1A1F21),
        surfaceContainerHighest = Color(0xFF20262A),
    )

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF006A67),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF9CF1EB),
        onPrimaryContainer = Color(0xFF00201F),
        inversePrimary = Color(0xFF35D0D0),
        secondary = Color(0xFF006A67), // Unread badge
        onSecondary = Color(0xFFFFFFFF), // Unread badge text
        secondaryContainer = Color(0xFF9CF1EB), // Navigation bar selector pill & progress indicator (remaining)
        onSecondaryContainer = Color(0xFF00201F), // Navigation bar selector icon
        tertiary = Color(0xFF006E1B), // Downloaded badge
        onTertiary = Color(0xFFFFFFFF), // Downloaded badge text
        tertiaryContainer = Color(0xFF95F990),
        onTertiaryContainer = Color(0xFF002203),
        background = Color(0xFFFEFBFF),
        onBackground = Color(0xFF1B1B1F),
        surface = Color(0xFFFEFBFF),
        onSurface = Color(0xFF1B1B1F),
        surfaceVariant = Color(0xFFF3EDF7), // Navigation bar background (ThemePrefWidget)
        onSurfaceVariant = Color(0xFF44464F),
        surfaceTint = Color(0xFF006A67),
        inverseSurface = Color(0xFF303034),
        inverseOnSurface = Color(0xFFF2F0F4),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        outline = Color(0xFF757780),
        outlineVariant = Color(0xFFC5C6D0),
        surfaceContainerLowest = Color(0xFFF5F1F8),
        surfaceContainerLow = Color(0xFFF7F2FA),
        surfaceContainer = Color(0xFFF3EDF7), // Navigation bar background
        surfaceContainerHigh = Color(0xFFFCF7FF),
        surfaceContainerHighest = Color(0xFFFCF7FF),
    )
}
