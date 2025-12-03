package cl.duoc.rightnow

import cl.duoc.rightnow.data.rickmorty.RmCharacter

data class CharactersUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val characters: List<RmCharacter> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 1
)
