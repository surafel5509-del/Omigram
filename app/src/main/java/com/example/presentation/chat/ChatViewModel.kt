package com.example.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.model.Chat
import com.example.core.model.Message
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.MessageRepository
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatUiState(
    val inputText: String = "",
    val replyingToMessage: Message? = null,
    val selectedMessageForMenu: Message? = null,
    val isAttachmentSheetOpen: Boolean = false,
    val isEmojiSheetOpen: Boolean = false,
    val isOptionsMenuOpen: Boolean = false
)

class ChatViewModel(
    private val chatId: String,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val chat: StateFlow<Chat?> = chatRepository.getChatById(chatId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val messages: StateFlow<List<Message>> = messageRepository.getMessages(chatId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Mark messages as read on entry
        viewModelScope.launch {
            messageRepository.markMessagesAsRead(chatId, SampleData.CURRENT_USER_ID)
        }
    }

    fun onInputTextChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun setReplyingTo(message: Message?) {
        _uiState.value = _uiState.value.copy(replyingToMessage = message)
    }

    fun selectMessageForMenu(message: Message?) {
        _uiState.value = _uiState.value.copy(selectedMessageForMenu = message)
    }

    fun toggleAttachmentSheet(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAttachmentSheetOpen = open)
    }

    fun toggleEmojiSheet(open: Boolean) {
        _uiState.value = _uiState.value.copy(isEmojiSheetOpen = open)
    }

    fun toggleOptionsMenu(open: Boolean) {
        _uiState.value = _uiState.value.copy(isOptionsMenuOpen = open)
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty()) return

        val replyTo = _uiState.value.replyingToMessage
        _uiState.value = _uiState.value.copy(
            inputText = "",
            replyingToMessage = null
        )

        viewModelScope.launch {
            messageRepository.sendMessage(
                chatId = chatId,
                content = text,
                replyTo = replyTo
            )
        }
    }

    fun sendVoiceNoteSimulation() {
        viewModelScope.launch {
            messageRepository.sendMessage(
                chatId = chatId,
                content = "🎤 Voice message (0:14)",
                replyTo = _uiState.value.replyingToMessage
            )
            _uiState.value = _uiState.value.copy(replyingToMessage = null)
        }
    }

    fun sendAttachmentSimulation(type: String, url: String) {
        _uiState.value = _uiState.value.copy(isAttachmentSheetOpen = false)
        viewModelScope.launch {
            messageRepository.sendMessage(
                chatId = chatId,
                content = if (type == "IMAGE") "Sent an image" else "Sent a document",
                attachmentUrl = url
            )
        }
    }

    fun toggleReaction(messageId: String, emoji: String) {
        viewModelScope.launch {
            messageRepository.toggleReaction(messageId, SampleData.CURRENT_USER_ID, emoji)
            _uiState.value = _uiState.value.copy(selectedMessageForMenu = null)
        }
    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            messageRepository.deleteMessage(messageId)
            _uiState.value = _uiState.value.copy(selectedMessageForMenu = null)
        }
    }

    fun toggleMuteChat() {
        viewModelScope.launch {
            chatRepository.toggleMuteChat(chatId)
            _uiState.value = _uiState.value.copy(isOptionsMenuOpen = false)
        }
    }

    fun togglePinChat() {
        viewModelScope.launch {
            chatRepository.togglePinChat(chatId)
            _uiState.value = _uiState.value.copy(isOptionsMenuOpen = false)
        }
    }

    class Factory(
        private val chatId: String,
        private val chatRepository: ChatRepository,
        private val messageRepository: MessageRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(chatId, chatRepository, messageRepository, userRepository) as T
        }
    }
}
