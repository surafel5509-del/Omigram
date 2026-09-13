package com.example.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramPrimaryText

/**
 * Omigram Script Logo Composable
 * Renders the iconic script branding inspired by Instagram
 */
@Composable
fun OmigramLogo(
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 28.sp,
    color: Color = OmigramPrimaryText
) {
    Text(
        text = "Omigram",
        style = MaterialTheme.typography.headlineMedium.copy(
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = fontSize,
            letterSpacing = (-0.5).sp
        ),
        color = color,
        modifier = modifier
    )
}

/**
 * Google "G" official multi-color brand logo
 */
@Composable
fun GoogleLogo(modifier: Modifier = Modifier.size(20.dp)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val blue = Color(0xFF4285F4)
        val red = Color(0xFFEA4335)
        val yellow = Color(0xFFFBBC05)
        val green = Color(0xFF34A853)

        // Outer ring segments with stroke
        val strokeWidth = w * 0.22f
        val radius = (w - strokeWidth) / 2f
        val center = Offset(w / 2f, h / 2f)

        // Red top arch: ~210 to 330 deg
        drawArc(
            color = red,
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
            size = Size(w - strokeWidth, h - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )

        // Yellow left arch: ~120 to 220 deg
        drawArc(
            color = yellow,
            startAngle = 120f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
            size = Size(w - strokeWidth, h - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )

        // Green bottom arch: ~30 to 140 deg
        drawArc(
            color = green,
            startAngle = 20f,
            sweepAngle = 110f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
            size = Size(w - strokeWidth, h - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )

        // Blue horizontal bar and right arc
        drawArc(
            color = blue,
            startAngle = -45f,
            sweepAngle = 75f,
            useCenter = false,
            topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
            size = Size(w - strokeWidth, h - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )

        // Crossbar
        drawRect(
            color = blue,
            topLeft = Offset(w * 0.45f, h * 0.40f),
            size = Size(w * 0.52f, h * 0.20f)
        )
    }
}

/**
 * GitHub Octocat silhouette logo
 */
@Composable
fun GitHubLogo(
    modifier: Modifier = Modifier.size(20.dp),
    tint: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Outer circular head with ears
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            cubicTo(w * 0.22f, 0f, 0f, h * 0.22f, 0f, h * 0.5f)
            cubicTo(0f, h * 0.72f, w * 0.15f, h * 0.91f, w * 0.35f, h * 0.98f)
            cubicTo(w * 0.37f, h * 0.98f, w * 0.38f, h * 0.96f, w * 0.38f, h * 0.94f)
            lineTo(w * 0.38f, h * 0.84f)
            cubicTo(w * 0.24f, h * 0.87f, w * 0.21f, h * 0.77f, w * 0.21f, h * 0.77f)
            cubicTo(w * 0.19f, h * 0.72f, w * 0.16f, h * 0.70f, w * 0.16f, h * 0.70f)
            cubicTo(w * 0.11f, h * 0.67f, w * 0.16f, h * 0.67f, w * 0.16f, h * 0.67f)
            cubicTo(w * 0.22f, h * 0.67f, w * 0.25f, h * 0.73f, w * 0.25f, h * 0.73f)
            cubicTo(w * 0.30f, h * 0.81f, w * 0.37f, h * 0.79f, w * 0.40f, h * 0.77f)
            cubicTo(w * 0.40f, h * 0.73f, w * 0.42f, h * 0.70f, w * 0.44f, h * 0.68f)
            cubicTo(w * 0.33f, h * 0.67f, w * 0.21f, h * 0.63f, w * 0.21f, h * 0.44f)
            cubicTo(w * 0.21f, h * 0.39f, w * 0.23f, h * 0.34f, w * 0.27f, h * 0.31f)
            cubicTo(w * 0.26f, h * 0.29f, w * 0.24f, h * 0.23f, w * 0.27f, h * 0.16f)
            cubicTo(w * 0.27f, h * 0.16f, w * 0.32f, h * 0.14f, w * 0.43f, h * 0.22f)
            cubicTo(w * 0.48f, h * 0.20f, w * 0.52f, h * 0.20f, w * 0.57f, h * 0.20f)
            cubicTo(w * 0.68f, h * 0.14f, w * 0.73f, h * 0.16f, w * 0.73f, h * 0.16f)
            cubicTo(w * 0.76f, h * 0.23f, w * 0.74f, h * 0.29f, w * 0.73f, h * 0.31f)
            cubicTo(w * 0.77f, h * 0.34f, w * 0.79f, h * 0.39f, w * 0.79f, h * 0.44f)
            cubicTo(w * 0.79f, h * 0.63f, w * 0.67f, h * 0.67f, w * 0.56f, h * 0.68f)
            cubicTo(w * 0.58f, h * 0.70f, w * 0.60f, h * 0.74f, w * 0.60f, h * 0.80f)
            lineTo(w * 0.60f, h * 0.94f)
            cubicTo(w * 0.60f, h * 0.96f, w * 0.61f, h * 0.98f, w * 0.65f, h * 0.98f)
            cubicTo(w * 0.85f, h * 0.91f, w * 1.0f, h * 0.72f, w * 1.0f, h * 0.5f)
            cubicTo(w * 1.0f, h * 0.22f, w * 0.78f, 0f, w * 0.5f, 0f)
            close()
        }
        drawPath(path, color = tint)
    }
}

/**
 * Modern Reels / Clapperboard Icon
 */
@Composable
fun ReelsIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    filled: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()

        val r = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        if (filled) {
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.08f, h * 0.08f),
                size = Size(w * 0.84f, h * 0.84f),
                cornerRadius = r
            )
            // Play triangle in white
            val tri = Path().apply {
                moveTo(w * 0.42f, h * 0.36f)
                lineTo(w * 0.66f, h * 0.50f)
                lineTo(w * 0.42f, h * 0.64f)
                close()
            }
            drawPath(tri, color = Color.White)
        } else {
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.10f, h * 0.10f),
                size = Size(w * 0.80f, h * 0.80f),
                cornerRadius = r,
                style = Stroke(width = stroke)
            )
            // Slashes on top clapboard
            drawLine(
                color = tint,
                start = Offset(w * 0.10f, h * 0.35f),
                end = Offset(w * 0.90f, h * 0.35f),
                strokeWidth = stroke
            )
            // Play triangle inside
            val tri = Path().apply {
                moveTo(w * 0.44f, h * 0.48f)
                lineTo(w * 0.62f, h * 0.58f)
                lineTo(w * 0.44f, h * 0.68f)
                close()
            }
            drawPath(tri, color = tint)
        }
    }
}

/**
 * Modern 4-square / 9-square Grid Icon (Reference image 3)
 */
@Composable
fun GridIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    filled: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()
        val r = CornerRadius(3.dp.toPx(), 3.dp.toPx())
        val spacing = w * 0.12f
        val sqSize = (w - spacing - stroke * 2) / 2f

        val coords = listOf(
            Offset(stroke, stroke),
            Offset(stroke + sqSize + spacing, stroke),
            Offset(stroke, stroke + sqSize + spacing),
            Offset(stroke + sqSize + spacing, stroke + sqSize + spacing)
        )

        for (c in coords) {
            drawRoundRect(
                color = tint,
                topLeft = c,
                size = Size(sqSize, sqSize),
                cornerRadius = r,
                style = if (filled) Fill else Stroke(width = stroke)
            )
        }
    }
}

/**
 * Modern Messenger / Direct Message speech bubble icon with lightning/play
 */
@Composable
fun DmIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    filled: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()

        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.10f)
            cubicTo(w * 0.80f, h * 0.10f, w * 0.92f, h * 0.32f, w * 0.92f, h * 0.50f)
            cubicTo(w * 0.92f, h * 0.68f, w * 0.80f, h * 0.85f, w * 0.58f, h * 0.88f)
            lineTo(w * 0.35f, h * 0.95f)
            lineTo(w * 0.40f, h * 0.85f)
            cubicTo(w * 0.20f, h * 0.82f, w * 0.08f, h * 0.68f, w * 0.08f, h * 0.50f)
            cubicTo(w * 0.08f, h * 0.32f, w * 0.20f, h * 0.10f, w * 0.5f, h * 0.10f)
            close()
        }

        if (filled) {
            drawPath(path, color = tint)
            // Lightning bolt in center
            val bolt = Path().apply {
                moveTo(w * 0.58f, h * 0.30f)
                lineTo(w * 0.38f, h * 0.54f)
                lineTo(w * 0.48f, h * 0.54f)
                lineTo(w * 0.42f, h * 0.72f)
                lineTo(w * 0.62f, h * 0.48f)
                lineTo(w * 0.52f, h * 0.48f)
                close()
            }
            drawPath(bolt, color = Color.White)
        } else {
            drawPath(path, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
            val bolt = Path().apply {
                moveTo(w * 0.58f, h * 0.32f)
                lineTo(w * 0.38f, h * 0.54f)
                lineTo(w * 0.48f, h * 0.54f)
                lineTo(w * 0.42f, h * 0.70f)
                lineTo(w * 0.62f, h * 0.48f)
                lineTo(w * 0.52f, h * 0.48f)
                close()
            }
            drawPath(bolt, color = tint, style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

/**
 * Plus in Rounded Square Create Icon
 */
@Composable
fun CreateSquareIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    filled: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 2.dp.toPx()
        val r = CornerRadius(6.dp.toPx(), 6.dp.toPx())

        if (filled) {
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.08f, h * 0.08f),
                size = Size(w * 0.84f, h * 0.84f),
                cornerRadius = r
            )
            // Plus in white
            drawLine(
                color = Color.White,
                start = Offset(w * 0.5f, h * 0.30f),
                end = Offset(w * 0.5f, h * 0.70f),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(w * 0.30f, h * 0.5f),
                end = Offset(w * 0.70f, h * 0.5f),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        } else {
            drawRoundRect(
                color = tint,
                topLeft = Offset(w * 0.10f, h * 0.10f),
                size = Size(w * 0.80f, h * 0.80f),
                cornerRadius = r,
                style = Stroke(width = stroke)
            )
            drawLine(
                color = tint,
                start = Offset(w * 0.5f, h * 0.32f),
                end = Offset(w * 0.5f, h * 0.68f),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = tint,
                start = Offset(w * 0.32f, h * 0.5f),
                end = Offset(w * 0.68f, h * 0.5f),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun CreateProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    filled: Boolean = false
) {
    CreateSquareIcon(modifier = modifier, tint = tint, filled = filled)
}

@Composable
fun DmProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    filled: Boolean = false
) {
    DmIcon(modifier = modifier, tint = tint, filled = filled)
}

@Composable
fun HomeProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    isFilled: Boolean = false
) {
    Icon(
        imageVector = if (isFilled) Icons.Filled.Home else Icons.Outlined.Home,
        contentDescription = "Home",
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun SearchProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    isBold: Boolean = false
) {
    Icon(
        imageVector = Icons.Filled.Search,
        contentDescription = "Search",
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun ReelsProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    isFilled: Boolean = false
) {
    ReelsIcon(modifier = modifier, tint = tint, filled = isFilled)
}

@Composable
fun GridProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    isFilled: Boolean = false
) {
    GridIcon(modifier = modifier, tint = tint, filled = isFilled)
}

@Composable
fun TaggedProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText,
    isFilled: Boolean = false
) {
    Icon(
        imageVector = if (isFilled) Icons.Filled.Person else Icons.Outlined.Person,
        contentDescription = "Tagged",
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun CommentProIcon(
    modifier: Modifier = Modifier.size(24.dp),
    tint: Color = OmigramPrimaryText
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 1.8.dp.toPx()
        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            cubicTo(w * 0.78f, h * 0.12f, w * 0.90f, h * 0.32f, w * 0.90f, h * 0.50f)
            cubicTo(w * 0.90f, h * 0.68f, w * 0.78f, h * 0.84f, w * 0.58f, h * 0.86f)
            lineTo(w * 0.32f, h * 0.94f)
            lineTo(w * 0.38f, h * 0.84f)
            cubicTo(w * 0.20f, h * 0.80f, w * 0.10f, h * 0.68f, w * 0.10f, h * 0.50f)
            cubicTo(w * 0.10f, h * 0.32f, w * 0.22f, h * 0.12f, w * 0.5f, h * 0.12f)
            close()
        }
        drawPath(path, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
