package cl.duoc.rightnow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.rightnow.data.rickmorty.RickAndMortyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val repo: RickAndMortyRepository = RickAndMortyRepository()
) : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(CharactersUiState())

    // Estado expuesto a la vista (inmutable)
    val uiState: StateFlow<CharactersUiState> = _uiState

    init {
        // Carga la primera página al crear el ViewModel
        loadPage(1)
    }

    fun loadPage(page: Int) {
        if (page <= 0) return

        viewModelScope.launch {
            // Loading ON
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = repo.getCharacters(page)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        characters = response.results,
                        currentPage = page,
                        totalPages = response.info.pages,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun goToNextPage() {
        val state = _uiState.value
        if (state.currentPage < state.totalPages) {
            loadPage(state.currentPage + 1)
        }
    }

    fun goToPrevPage() {
        val state = _uiState.value
        if (state.currentPage > 1) {
            loadPage(state.currentPage - 1)
        }
    }
}
