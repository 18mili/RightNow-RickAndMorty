package cl.duoc.rightnow.data.rickmorty

// Info de paginación de la API
data class RmInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

// Personaje individual
data class RmCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String
)

// Respuesta de /character?page=n
data class RmCharactersResponse(
    val info: RmInfo,
    val results: List<RmCharacter>
)
