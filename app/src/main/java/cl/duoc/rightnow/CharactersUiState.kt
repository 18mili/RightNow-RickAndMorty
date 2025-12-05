package cl.duoc.rightnow

import cl.duoc.rightnow.data.rickmorty.RmCharacter

data class CharactersUiState(
    val isLoading: Boolean = false,        // carga inicial (primera página)
    val isLoadingMore: Boolean = false,    // cargando páginas siguientes
    val errorMessage: String? = null,
    val characters: List<RmCharacter> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val endReached: Boolean = false        // true cuando ya no hay más páginas
)
