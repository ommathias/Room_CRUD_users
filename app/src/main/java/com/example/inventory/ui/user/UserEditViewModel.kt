package com.example.inventory.ui.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a user from the [UsersRepository]'s data source.
 */
class UserEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle[UserEditDestination.itemIdArg])

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState

    init {
        getUser()
    }

    /**
     * Busca o usuário no banco e atualiza o estado da UI.
     */
    private fun getUser() {
        viewModelScope.launch {
            usersRepository.getUserStream(userId).collect { user ->
                user?.let {
                    _userUiState.value = it.toUserUiState(true)
                }
            }
        }
    }

    /**
     * Atualiza os dados do usuário na UI.
     */
    fun updateUiState(updatedUser: UserDetails) {
        _userUiState.value = _userUiState.value.copy(
            userDetails = updatedUser,
            isEntryValid = validateInput(updatedUser)
        )
    }

    /**
     * Atualiza o usuário no banco de dados.
     */
    fun updateUser(navigateBack: () -> Unit) {
        if (_userUiState.value.isEntryValid) {
            viewModelScope.launch {
                usersRepository.updateUser(_userUiState.value.userDetails.toUser())
                navigateBack()
            }
        }
    }

    /**
     * Exclui o usuário do banco de dados.
     */
    fun deleteUser() {
        viewModelScope.launch {
            usersRepository.deleteUser(_userUiState.value.userDetails.toUser())
        }
    }

    /**
     * Valida os campos antes de salvar.
     */
    private fun validateInput(userDetails: UserDetails): Boolean {
        val uiState = _userUiState.value.userDetails
        return uiState.name.isNotBlank() && uiState.email.isNotBlank() && uiState.age.isNotBlank()
    }
}