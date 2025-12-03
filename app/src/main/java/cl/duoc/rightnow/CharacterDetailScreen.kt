package cl.duoc.rightnow

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    navController: NavController,
    characterId: Int,
    viewModel: CharacterDetailViewModel = viewModel()
) {
    // Pedimos al ViewModel que cargue el personaje cuando cambia el id
    LaunchedEffect(characterId) {
        viewModel.loadCharacter(characterId)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del personaje") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                }

                state.errorMessage != null -> {
                    Text("Error: ${state.errorMessage}")
                }

                state.character != null -> {
                    CharacterDetailContent(state.character!!)
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(character: RmCharacter) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier.size(200.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(character.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Estado: ${character.status}")
        Text("Especie: ${character.species}")
    }
}
