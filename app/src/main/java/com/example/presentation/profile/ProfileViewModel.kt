package com.example.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isEditProfileDialogOpen: Boolean = false,
    val isChangingAvatar: Boolean = false
)

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun openEditProfileDialog(open: Boolean) {
        _uiState.value = _uiState.value.copy(isEditProfileDialogOpen = open)
    }

    fun updateProfile(fullName: String, bio: String, phone: String) {
        val current = currentUser.value ?: return
        val updated = current.copy(
            fullName = fullName.trim(),
            bio = bio.trim(),
            phone = phone.trim(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            userRepository.updateProfile(updated)
            _uiState.value = _uiState.value.copy(isEditProfileDialogOpen = false)
        }
    }

    fun changeAvatar(newAvatarUrl: String) {
        val current = currentUser.value ?: return
        val updated = current.copy(
            avatarUrl = newAvatarUrl,
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            userRepository.updateProfile(updated)
        }
    }

    fun toggleOnlineStatus(isOnline: Boolean) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            userRepository.setOnlineStatus(current.id, isOnline)
        }
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(authRepository, userRepository) as T
        }
    }
}
