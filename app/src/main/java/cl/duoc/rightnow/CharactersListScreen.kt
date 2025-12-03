package cl.duoc.rightnow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cl.duoc.rightnow.data.rickmorty.RmCharacter

@Composable
fun CharactersListScreen(
    navController: NavController,
    viewModel: CharactersViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Personajes - Rick & Morty",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.errorMessage}")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(state.characters) { character ->
                        CharacterRow(
                            character = character,
                            onClick = {
                                navController.navigate("characterDetail/${character.id}")
                            }
                        )
                    }
                }
            }
        }

        // Controles de paginación usando el ViewModel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.goToPrevPage() },
                enabled = state.currentPage > 1
            ) {
                Text("Anterior")
            }

            Text("Página ${state.currentPage} / ${state.totalPages}")

            Button(
                onClick = { viewModel.goToNextPage() },
                enabled = state.currentPage < state.totalPages
            ) {
                Text("Siguiente")
            }
        }
    }
}

@Composable
private fun CharacterRow(
    character: RmCharacter,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(character.name, style = MaterialTheme.typography.titleMedium)
            Text(
                "${character.status} - ${character.species}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
