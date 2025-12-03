package cl.duoc.rightnow

import cl.duoc.rightnow.data.rickmorty.RmCharacter

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val character: RmCharacter? = null
)
