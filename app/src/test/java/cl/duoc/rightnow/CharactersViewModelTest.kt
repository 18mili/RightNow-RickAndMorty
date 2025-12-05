package cl.duoc.rightnow

import cl.duoc.rightnow.data.rickmorty.RickAndMortyRepository
import cl.duoc.rightnow.data.rickmorty.RmCharacter
import cl.duoc.rightnow.data.rickmorty.RmCharactersResponse
import cl.duoc.rightnow.data.rickmorty.RmInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var fakeRepo: RickAndMortyRepository
    private lateinit var viewModel: CharactersViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)

        fakeRepo = mockk()

        val fakeResponse = RmCharactersResponse(
            info = RmInfo(
                count = 1,
                pages = 1,
                next = null,
                prev = null
            ),
            results = listOf(
                RmCharacter(
                    id = 1,
                    name = "Rick",
                    status = "Alive",
                    species = "Human",
                    image = "https://test.com/rick.png"
                )
            )
        )

        coEvery { fakeRepo.getCharacters(1) } returns fakeResponse

        // IMPORTANTE: tu ViewModel debe aceptar el repo por constructor
        viewModel = CharactersViewModel(fakeRepo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al crear el ViewModel se carga la primera pagina`() = runTest {
        // Dejar que se ejecuten las coroutines del init {}
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(1, state.characters.size)
        assertEquals("Rick", state.characters.first().name)
    }
}
