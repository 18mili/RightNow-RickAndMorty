package cl.duoc.rightnow

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// DataStore
val Context.userPrefs by preferencesDataStore(name = "users_pref")

// claves
private val USERS_RAW = stringPreferencesKey("users_raw")     // acá guardamos TODOS los usuarios
private val CURRENT_USER = stringPreferencesKey("current_user") // email del que inició sesión

/**
 * Vamos a guardar los usuarios en este formato:
 *
 *  email|password|name;email2|password2|name2;...
 *
 *  - Los usuarios se separan con ';'
 *  - Los campos dentro del usuario se separan con '|'
 */
class UserLocalDataStore(private val context: Context) {

    // obtener el string crudo
    private suspend fun getRawUsers(): String {
        return context.userPrefs.data.map { prefs ->
            prefs[USERS_RAW] ?: ""
        }.first()
    }

    // convertir el string en lista de User
    private suspend fun getUsersList(): List<User> {
        val raw = getRawUsers()
        if (raw.isBlank()) return emptyList()

        return raw.split(";")
            .filter { it.isNotBlank() }
            .map { userStr ->
                val parts = userStr.split("|")
                // parts[0] = email, parts[1] = password, parts[2] = name (si existe)
                val email = parts.getOrNull(0) ?: ""
                val password = parts.getOrNull(1) ?: ""
                val name = parts.getOrNull(2) ?: ""
                User(name = name, email = email, password = password)
            }
    }

    // guardar lista de usuarios en el string
    private suspend fun saveUsersList(users: List<User>) {
        val raw = users.joinToString(separator = ";") { user ->
            "${user.email}|${user.password}|${user.name}"
        }
        context.userPrefs.edit { prefs ->
            prefs[USERS_RAW] = raw
        }
    }

    // registrar
    suspend fun register(user: User): Result<Unit> {
        val users = getUsersList()
        // validar que no exista el correo
        if (users.any { it.email.equals(user.email, ignoreCase = true) }) {
            return Result.failure(IllegalStateException("El email ya está registrado"))
        }
        // agregamos y guardamos
        saveUsersList(users + user)
        return Result.success(Unit)
    }

    // login: retorna true si coincide email+password
    suspend fun login(email: String, password: String): Boolean {
        val users = getUsersList()
        val ok = users.any { it.email.equals(email, ignoreCase = true) && it.password == password }
        if (ok) {
            // guardo quién entró
            context.userPrefs.edit { prefs ->
                prefs[CURRENT_USER] = email
            }
        }
        return ok
    }

    // obtener email del que está logueado
    suspend fun getCurrentUserEmail(): String? {
        return context.userPrefs.data.map { prefs ->
            prefs[CURRENT_USER]
        }.first()?.takeIf { it.isNotBlank() }
    }

    // cerrar sesión
    suspend fun logout() {
        context.userPrefs.edit { prefs ->
            prefs[CURRENT_USER] = ""
        }
    }
}
