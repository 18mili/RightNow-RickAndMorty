package cl.duoc.rightnow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cl.duoc.rightnow.data.rickmorty.RickAndMortyRepository
import cl.duoc.rightnow.data.rickmorty.RmCharacter

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    navController: NavController,
    repository: RickAndMortyRepository = RickAndMortyRepository()
) {
    var character by remember { mutableStateOf<RmCharacter?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(characterId) {
        isLoading = true
        errorMessage = null
        try {
            character = repository.getCharacter(characterId)
        } catch (e: Exception) {
            errorMessage = "Error al cargar personaje: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para volver a la lista
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                character?.let { ch ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = ch.image,
                            contentDescription = ch.name,
                            modifier = Modifier
                                .size(220.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = ch.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Estado: ${ch.status}")
                        Text(text = "Especie: ${ch.species}")
                    }
                }
            }
        }
    }
}
