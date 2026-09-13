package com.example.presentation.auth

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.common.GitHubLogo
import com.example.presentation.common.GoogleLogo
import com.example.presentation.common.OmigramLogo
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramErrorRed
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("alex.mercer@omigram.app") }
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onNavigateToHome()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("login_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Instagram-Style Script Logo
            OmigramLogo(fontSize = 44.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Connect with your friends, photos, and moments",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = OmigramSecondaryText
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Error banner
            AnimatedVisibility(visible = uiState is AuthUiState.Error) {
                val errorMsg = (uiState as? AuthUiState.Error)?.message ?: ""
                Surface(
                    color = OmigramErrorRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = errorMsg,
                        color = OmigramErrorRed,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Phone number, username, or email", fontSize = 13.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = OmigramSecondaryBackground,
                    unfocusedContainerColor = OmigramSecondaryBackground,
                    focusedBorderColor = OmigramBorder,
                    unfocusedBorderColor = OmigramBorder,
                    focusedTextColor = OmigramPrimaryText,
                    unfocusedTextColor = OmigramPrimaryText
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_email_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", fontSize = 13.sp) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = OmigramSecondaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = OmigramSecondaryBackground,
                    unfocusedContainerColor = OmigramSecondaryBackground,
                    focusedBorderColor = OmigramBorder,
                    unfocusedBorderColor = OmigramBorder,
                    focusedTextColor = OmigramPrimaryText,
                    unfocusedTextColor = OmigramPrimaryText
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_password_input")
            )

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onNavigateToForgotPassword,
                    modifier = Modifier.testTag("login_forgot_password_button")
                ) {
                    Text(
                        text = "Forgot password?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OmigramAccentBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Log In Button (Instagram Blue Pill)
            Button(
                onClick = { viewModel.login(email, password) },
                enabled = email.isNotBlank() && password.length >= 6 && uiState !is AuthUiState.Loading,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OmigramAccentBlue,
                    disabledContainerColor = OmigramAccentBlue.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_submit_button")
            ) {
                if (uiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Log In",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "OR" Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = OmigramBorder)
                Text(
                    text = "OR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmigramSecondaryText,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = OmigramBorder)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Continue with Google Button
            Surface(
                onClick = {
                    // Fast OAuth Mock Flow: authenticates as Google user
                    viewModel.login("alex.mercer@gmail.com", "password123")
                },
                shape = RoundedCornerShape(999.dp),
                color = OmigramBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, OmigramBorder, RoundedCornerShape(999.dp))
                    .testTag("login_google_button")
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GoogleLogo(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Continue with Google",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OmigramPrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Continue with GitHub Button
            Surface(
                onClick = {
                    // Fast OAuth Mock Flow: authenticates as GitHub user
                    viewModel.login("alex.mercer@github.com", "password123")
                },
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFF24292E),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_github_button")
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GitHubLogo(modifier = Modifier.size(20.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Continue with GitHub",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom bar: Sign Up link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 13.sp,
                    color = OmigramSecondaryText
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmigramAccentBlue,
                    modifier = Modifier
                        .clickable { onNavigateToRegister() }
                        .testTag("login_register_link")
                )
            }
        }
    }
}
