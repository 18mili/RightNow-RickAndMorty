package cl.duoc.rightnow.data.rickmorty

class RickAndMortyRepository(
    private val api: RickAndMortyApi = RickAndMortyService.api
) {
    suspend fun getCharacters(page: Int) = api.getCharacters(page)

    suspend fun getCharacter(id: Int) = api.getCharacter(id)
}
