package cl.duoc.rightnow.data.rickmorty

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    // GET /api/character?page=n
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): RmCharactersResponse

    // GET /api/character/{id}
    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): RmCharacter
}
