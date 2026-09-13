package com.example.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.model.User
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Please enter both email and password")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(email, password)
            result.onSuccess { user ->
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Authentication failed")
            }
        }
    }

    fun register(fullName: String, username: String, email: String, password: String) {
        if (fullName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Please fill in all registration fields")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.register(fullName, username, email, password)
            result.onSuccess { user ->
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Registration failed")
            }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit) {
        if (email.isBlank()) {
            _uiState.value = AuthUiState.Error("Please enter your registered email")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.resetPassword(email)
            result.onSuccess {
                _uiState.value = AuthUiState.Idle
                _toastEvent.emit("Password reset instructions sent to $email")
                onSuccess()
            }.onFailure { error ->
                _uiState.value = AuthUiState.Error(error.message ?: "Failed to send reset email")
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState.Idle
            onComplete()
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authRepository) as T
        }
    }
}
