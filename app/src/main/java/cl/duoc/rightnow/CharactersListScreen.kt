package cl.duoc.rightnow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
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
fun CharactersListScreen(
    navController: NavController,
    repository: RickAndMortyRepository = RickAndMortyRepository()
) {
    var characters by remember { mutableStateOf<List<RmCharacter>>(emptyList()) }
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cada vez que cambia la página, volvemos a cargar
    LaunchedEffect(currentPage) {
        isLoading = true
        errorMessage = null
        try {
            val response = repository.getCharacters(currentPage)
            characters = response.results
            totalPages = response.info.pages
        } catch (e: Exception) {
            errorMessage = "Error al cargar personajes: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Personajes Rick and Morty",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Si hubo error, lo mostramos arriba de la lista
            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)   // ocupa todo el espacio disponible
                    .fillMaxWidth()
            ) {
                items(characters) { character ->
                    CharacterListItem(
                        character = character,
                        onClick = {
                            navController.navigate("characterDetail/${character.id}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Controles de paginación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentPage > 1) currentPage-- },
                    enabled = currentPage > 1
                ) {
                    Text("Anterior")
                }

                Text(text = "Página $currentPage de $totalPages")

                Button(
                    onClick = { if (currentPage < totalPages) currentPage++ },
                    enabled = currentPage < totalPages
                ) {
                    Text("Siguiente")
                }
            }
        }
    }
}

@Composable
private fun CharacterListItem(
    character: RmCharacter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Estado: ${character.status}")
                Text(text = "Especie: ${character.species}")
            }
        }
    }
}
