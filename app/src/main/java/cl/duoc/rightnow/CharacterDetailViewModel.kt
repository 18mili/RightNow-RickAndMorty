package cl.duoc.rightnow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.rightnow.data.rickmorty.RickAndMortyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val repo: RickAndMortyRepository = RickAndMortyRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val character = repo.getCharacter(id)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        character = character,
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
}
