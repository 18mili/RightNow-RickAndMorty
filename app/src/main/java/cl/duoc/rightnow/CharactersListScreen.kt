package cl.duoc.rightnow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
            // CARGA INICIAL (no hay personajes todavía)
            state.isLoading && state.characters.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // ERROR EN LA PRIMERA CARGA
            state.errorMessage != null && state.characters.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.errorMessage}")
                }
            }

            // LISTA CON DATOS
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(
                        items = state.characters,
                        key = { _, item -> item.id }
                    ) { index, character ->

                        CharacterRow(
                            character = character,
                            onClick = {
                                navController.navigate("characterDetail/${character.id}")
                            }
                        )
                        Divider()

                        // ⚡ Disparar carga de la siguiente página cuando llegamos al último ítem
                        val lastIndex = state.characters.lastIndex
                        if (index == lastIndex &&
                            !state.endReached &&
                            !state.isLoadingMore &&
                            !state.isLoading
                        ) {
                            LaunchedEffect(key1 = index) {
                                viewModel.loadNextPage()
                            }
                        }
                    }

                    // ÍTEM EXTRA CUANDO ESTÁ CARGANDO MÁS PÁGINAS
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    // MENSAJE FINAL CUANDO YA NO HAY MÁS PÁGINAS
                    if (state.endReached && !state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay más personajes")
                            }
                        }
                    }
                }

                // ERROR AL CARGAR MÁS (cuando ya había algunos personajes)
                if (state.errorMessage != null && state.characters.isNotEmpty()) {
                    Text(
                        text = "Error al cargar más: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
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
