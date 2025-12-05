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

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState

    init {
        // Cargar la primera página al crear el ViewModel
        loadPage(page = 1, append = false)
    }

    /**
     * Carga una página de personajes.
     * @param page número de página a cargar
     * @param append si true, agrega al final de la lista actual (scroll infinito)
     */
    private fun loadPage(page: Int, append: Boolean) {
        val current = _uiState.value

        // Evitar llamadas innecesarias
        if (current.endReached) return
        if (page <= 0) return
        if (current.isLoading || current.isLoadingMore) return

        viewModelScope.launch {
            // Activar flags de carga
            _uiState.update {
                it.copy(
                    isLoading = if (!append) true else it.isLoading,
                    isLoadingMore = if (append) true else it.isLoadingMore,
                    errorMessage = null
                )
            }

            try {
                val response = repo.getCharacters(page)

                _uiState.update { prev ->
                    val newList =
                        if (append) prev.characters + response.results
                        else response.results

                    prev.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        characters = newList,
                        currentPage = page,
                        totalPages = response.info.pages,
                        endReached = response.info.next == null,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    /**
     * Llamado desde la UI cuando el usuario llega al final de la lista.
     */
    fun loadNextPage() {
        val state = _uiState.value

        if (state.endReached) return
        if (state.isLoading || state.isLoadingMore) return

        val nextPage = state.currentPage + 1

        if (nextPage <= state.totalPages) {
            loadPage(page = nextPage, append = true)
        } else {
            _uiState.update { it.copy(endReached = true) }
        }
    }
}
