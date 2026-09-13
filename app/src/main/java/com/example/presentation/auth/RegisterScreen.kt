package com.example.presentation.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
import com.example.data.remote.supabase.SupabaseClientProvider
import com.example.presentation.common.OmigramLogo
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramErrorRed
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText
import com.example.ui.theme.OmigramSuccessGreen
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPeachGradientTop
import com.example.ui.theme.SocialPeachGradientMid
import com.example.ui.theme.SocialPeachGradientBottom
import com.example.ui.theme.SocialPillDark
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Multi-Step State: 1 = Email+Password, 2 = Verification, 3 = Profile Info, 4 = Terms & Complete
    var currentStep by remember { mutableIntStateOf(1) }

    // Step 1 fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Step 2 fields: 6-digit code
    var otpDigits by remember { mutableStateOf(List(6) { "" }) }
    var resendTimer by remember { mutableIntStateOf(45) }

    // Step 3 fields
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedAvatarUrl by remember {
        mutableStateOf("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80")
    }

    // Step 4 fields
    var termsAgreed by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onNavigateToHome()
        }
    }

    // Timer countdown for Step 2
    LaunchedEffect(currentStep, resendTimer) {
        if (currentStep == 2 && resendTimer > 0) {
            delay(1000L)
            resendTimer -= 1
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SocialPeachGradientTop,
                        SocialPeachGradientMid,
                        SocialPeachGradientBottom
                    )
                )
            )
            .testTag("register_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.06f)),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    IconButton(
                        onClick = {
                            if (currentStep > 1) {
                                currentStep -= 1
                            } else {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.testTag("register_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Step indicator pill
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.shadow(3.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.05f))
                ) {
                    Text(
                        text = "Step $currentStep of 4",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Omigram Logo
            OmigramLogo(fontSize = 36.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // White Card Container for Registration Form
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(26.dp),
                        spotColor = Color(0xFFD4A373).copy(alpha = 0.25f)
                    ),
                shape = RoundedCornerShape(26.dp),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Animated step container
                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { width -> width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> -width } + fadeOut()
                            } else {
                                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                        slideOutHorizontally { width -> width } + fadeOut()
                            }
                        },
                        label = "step_transition"
                    ) { step ->
                        when (step) {
                            1 -> StepOneEmailPassword(
                                email = email,
                                onEmailChange = { email = it },
                                password = password,
                                onPasswordChange = { password = it },
                                passwordVisible = passwordVisible,
                                onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                                onNext = { currentStep = 2 },
                                onNavigateToLogin = onNavigateBack
                            )
                            2 -> StepTwoVerification(
                                email = email.ifBlank { "your email" },
                                otpDigits = otpDigits,
                                onDigitChange = { index, value ->
                                    val updated = otpDigits.toMutableList()
                                    updated[index] = value
                                    otpDigits = updated
                                    if (updated.all { it.isNotBlank() }) {
                                        currentStep = 3
                                    }
                                },
                                resendTimer = resendTimer,
                                onResend = { resendTimer = 45 },
                                onVerify = { currentStep = 3 }
                            )
                            3 -> StepThreeProfile(
                                fullName = fullName,
                                onFullNameChange = { fullName = it },
                                username = username,
                                onUsernameChange = { username = it },
                                bio = bio,
                                onBioChange = { bio = it },
                                avatarUrl = selectedAvatarUrl,
                                onChangeAvatar = {
                                    selectedAvatarUrl = if (selectedAvatarUrl.contains("photo-1534528741775")) {
                                        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&auto=format&fit=crop&q=80"
                                    } else {
                                        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80"
                                    }
                                },
                                onNext = { currentStep = 4 }
                            )
                            4 -> StepFourTerms(
                                termsAgreed = termsAgreed,
                                onTermsChange = { termsAgreed = it },
                                isLoading = uiState is AuthUiState.Loading,
                                onSubmit = {
                                    viewModel.register(
                                        fullName = fullName.ifBlank { "Alex Mercer" },
                                        username = username.ifBlank { "alexmercer" },
                                        email = email.ifBlank { "alex.mercer@omigram.app" },
                                        password = password.ifBlank { "password123" }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StepOneEmailPassword(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onNext: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val isEmailValid = email.contains("@") && email.contains(".")
    val isPasswordValid = password.length >= 6
    val isValid = isEmailValid && isPasswordValid

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Sign up to connect, share moments, and explore.",
            fontSize = 13.5.sp,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email address", fontSize = 13.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedBorderColor = SocialBrandBlue,
                unfocusedBorderColor = Color(0xFFE5E5EA),
                focusedTextColor = Color(0xFF1A1A1A),
                unfocusedTextColor = Color(0xFF1A1A1A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_email_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password (min. 6 characters)", fontSize = 13.sp) },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password visibility",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedBorderColor = SocialBrandBlue,
                unfocusedBorderColor = Color(0xFFE5E5EA),
                focusedTextColor = Color(0xFF1A1A1A),
                unfocusedTextColor = Color(0xFF1A1A1A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_password_input")
        )

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = onNext,
            enabled = isValid,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SocialPillDark,
                disabledContainerColor = SocialPillDark.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("register_next_step1")
        ) {
            Text(
                text = "Next",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                fontSize = 13.sp,
                color = Color(0xFF7A7A7A)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Log in",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SocialBrandBlue,
                modifier = Modifier
                    .clickable { onNavigateToLogin() }
                    .testTag("register_login_link")
            )
        }
    }
}

@Composable
private fun StepTwoVerification(
    email: String,
    otpDigits: List<String>,
    onDigitChange: (Int, String) -> Unit,
    resendTimer: Int,
    onResend: () -> Unit,
    onVerify: () -> Unit
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val isComplete = otpDigits.all { it.isNotBlank() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Verification Code",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Enter the 6-digit code sent to $email",
            fontSize = 13.5.sp,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(26.dp))

        // 6 distinct input boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until 6) {
                val digit = otpDigits[i]
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFAFAFA))
                        .border(
                            width = 1.dp,
                            color = if (digit.isNotBlank()) SocialBrandBlue else Color(0xFFE5E5EA),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = digit,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {
                                onDigitChange(i, newValue)
                                if (newValue.isNotEmpty() && i < 5) {
                                    focusRequesters[i + 1].requestFocus()
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A),
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .focusRequester(focusRequesters[i])
                            .testTag("otp_digit_$i")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onVerify,
            enabled = isComplete || otpDigits.any { it.isNotBlank() },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SocialPillDark,
                disabledContainerColor = SocialPillDark.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("register_verify_button")
        ) {
            Text(
                text = "Verify Code",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (resendTimer > 0) {
            Text(
                text = "Resend code in ${resendTimer}s",
                fontSize = 13.sp,
                color = Color(0xFF8E8E93)
            )
        } else {
            TextButton(onClick = onResend) {
                Text(
                    text = "Resend code",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SocialBrandBlue
                )
            }
        }
    }
}

@Composable
private fun StepThreeProfile(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    avatarUrl: String,
    onChangeAvatar: () -> Unit,
    onNext: () -> Unit
) {
    val isUsernameAvailable = username.length >= 3

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Choose your photo and public username.",
            fontSize = 13.5.sp,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Avatar with Camera Icon Overlay
        Box(
            modifier = Modifier
                .size(92.dp)
                .clickable { onChangeAvatar() }
                .testTag("register_avatar_picker"),
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFE5E5EA), CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(SocialBrandBlue)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change photo",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Full Name Field
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text("Full Name", fontSize = 13.sp) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedBorderColor = SocialBrandBlue,
                unfocusedBorderColor = Color(0xFFE5E5EA),
                focusedTextColor = Color(0xFF1A1A1A),
                unfocusedTextColor = Color(0xFF1A1A1A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_fullname_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Username with availability indicator
        OutlinedTextField(
            value = username,
            onValueChange = { onUsernameChange(it.lowercase().replace(" ", "_")) },
            label = { Text("Username", fontSize = 13.sp) },
            trailingIcon = {
                if (isUsernameAvailable) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Username available",
                        tint = Color(0xFF34C759),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedBorderColor = if (isUsernameAvailable) Color(0xFF34C759) else SocialBrandBlue,
                unfocusedBorderColor = Color(0xFFE5E5EA),
                focusedTextColor = Color(0xFF1A1A1A),
                unfocusedTextColor = Color(0xFF1A1A1A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_username_input")
        )

        if (isUsernameAvailable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✓ @$username is available",
                    fontSize = 12.sp,
                    color = Color(0xFF34C759),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Optional Bio Field
        OutlinedTextField(
            value = bio,
            onValueChange = onBioChange,
            label = { Text("Bio (optional)", fontSize = 13.sp) },
            maxLines = 3,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                focusedBorderColor = SocialBrandBlue,
                unfocusedBorderColor = Color(0xFFE5E5EA),
                focusedTextColor = Color(0xFF1A1A1A),
                unfocusedTextColor = Color(0xFF1A1A1A)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_bio_input")
        )

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = onNext,
            enabled = fullName.isNotBlank() && isUsernameAvailable,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SocialPillDark,
                disabledContainerColor = SocialPillDark.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("register_next_step3")
        ) {
            Text(
                text = "Next",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun StepFourTerms(
    termsAgreed: Boolean,
    onTermsChange: (Boolean) -> Unit,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Terms & Policies",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "By continuing, you agree to Omigram's Terms of Service and Privacy Policy.",
            fontSize = 13.5.sp,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFAFAFA))
                .border(1.dp, Color(0xFFE5E5EA), RoundedCornerShape(14.dp))
                .clickable { onTermsChange(!termsAgreed) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAgreed,
                onCheckedChange = onTermsChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = SocialBrandBlue,
                    uncheckedColor = Color(0xFF8E8E93)
                ),
                modifier = Modifier.testTag("terms_checkbox")
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "I agree to the Terms of Service, Community Standards, and Data Policy.",
                fontSize = 13.sp,
                color = Color(0xFF1A1A1A),
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = onSubmit,
            enabled = termsAgreed && !isLoading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SocialPillDark,
                disabledContainerColor = SocialPillDark.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("register_submit_button")
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Create Account",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
