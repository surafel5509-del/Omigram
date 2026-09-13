package com.example.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.model.Call
import com.example.core.model.CallStatus
import com.example.core.model.CallType
import com.example.core.model.Chat
import com.example.core.model.Contact
import com.example.core.model.Post
import com.example.core.model.Story
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.data.repository.toDomain
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.CallRepository
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.ContactRepository
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class OmigramTab {
    HOME,
    SEARCH,
    CREATE,
    REELS,
    PROFILE
}

data class HomeUiState(
    val selectedTab: OmigramTab = OmigramTab.HOME,
    val searchQuery: String = "",
    val isSearchOpen: Boolean = false,
    val isAddContactDialogOpen: Boolean = false,
    val activeCallModal: Call? = null,
    val isCallActive: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val contactRepository: ContactRepository,
    private val callRepository: CallRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _posts = MutableStateFlow(SampleData.samplePosts)
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _stories = MutableStateFlow(SampleData.sampleStories)
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _rawChats = chatRepository.getChats()
    val chats: StateFlow<List<Chat>> = combine(_rawChats, _uiState) { chatList, state ->
        val query = state.searchQuery.trim().lowercase()
        if (query.isEmpty()) {
            chatList
        } else {
            chatList.filter {
                it.participant.fullName.lowercase().contains(query) ||
                it.participant.username.lowercase().contains(query) ||
                (it.lastMessage?.content?.lowercase()?.contains(query) == true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val calls: StateFlow<List<Call>> = callRepository.getCallHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _rawContacts = contactRepository.getContacts()
    val contacts: StateFlow<List<Contact>> = combine(_rawContacts, _uiState) { contactList, state ->
        val query = state.searchQuery.trim().lowercase()
        val filtered = if (query.isEmpty()) {
            contactList
        } else {
            contactList.filter {
                it.contactUser.fullName.lowercase().contains(query) ||
                it.contactUser.username.lowercase().contains(query)
            }
        }
        filtered.sortedBy { it.contactUser.fullName.lowercase() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: OmigramTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun toggleLikePost(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val newLiked = !post.isLiked
                post.copy(
                    isLiked = newLiked,
                    likesCount = post.likesCount + if (newLiked) 1 else -1
                )
            } else {
                post
            }
        }
    }

    fun toggleSavePost(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(isBookmarked = !post.isBookmarked)
            } else {
                post
            }
        }
    }

    fun addNewPost(newPost: Post) {
        _posts.value = listOf(newPost) + _posts.value
        selectTab(OmigramTab.HOME)
    }

    fun toggleSearch() {
        val next = !_uiState.value.isSearchOpen
        _uiState.value = _uiState.value.copy(
            isSearchOpen = next,
            searchQuery = if (!next) "" else _uiState.value.searchQuery
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun openAddContactDialog(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAddContactDialogOpen = open)
    }

    fun addContact(usernameOrEmail: String) {
        viewModelScope.launch {
            val result = contactRepository.addContact(usernameOrEmail)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isAddContactDialogOpen = false)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(errorMessage = err.message)
            }
        }
    }

    fun startChatWithContact(contactUser: User, onChatReady: (String) -> Unit) {
        viewModelScope.launch {
            val chat = chatRepository.getOrCreateChatForUser(contactUser)
            onChatReady(chat.id)
        }
    }

    fun togglePinChat(chatId: String) {
        viewModelScope.launch {
            chatRepository.togglePinChat(chatId)
        }
    }

    fun toggleMuteChat(chatId: String) {
        viewModelScope.launch {
            chatRepository.toggleMuteChat(chatId)
        }
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            chatRepository.deleteChat(chatId)
        }
    }

    fun startMockCall(otherUser: User, isVideo: Boolean) {
        val mockCall = Call(
            id = "call_${System.currentTimeMillis()}",
            caller = currentUser.value ?: SampleData.currentUser,
            receiver = otherUser,
            callType = if (isVideo) CallType.VIDEO else CallType.AUDIO,
            status = CallStatus.OUTGOING,
            durationSeconds = 0,
            startedAt = System.currentTimeMillis()
        )
        _uiState.value = _uiState.value.copy(
            activeCallModal = mockCall,
            isCallActive = true
        )
        viewModelScope.launch {
            callRepository.recordCall(mockCall)
        }
    }

    fun endMockCall() {
        _uiState.value = _uiState.value.copy(
            activeCallModal = null,
            isCallActive = false
        )
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val userRepository: UserRepository,
        private val chatRepository: ChatRepository,
        private val contactRepository: ContactRepository,
        private val callRepository: CallRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                authRepository,
                userRepository,
                chatRepository,
                contactRepository,
                callRepository
            ) as T
        }
    }
}
