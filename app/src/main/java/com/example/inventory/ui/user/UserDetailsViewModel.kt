/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.UsersRepository
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the [UsersRepository]'s data source.
 */
class UserDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle[UserDetailsDestination.itemIdArg])

    fun deleteUser(onDeleteSuccess: () -> Unit) {
        viewModelScope.launch {
            usersRepository.deleteUserById(userId)
            onDeleteSuccess()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for UserDetailsScreen
 */
data class UserDetailsUiState(
    val outOfStock: Boolean = true,
    val userDetails: UserDetails = UserDetails()
)
