📘 RightNow App – Kotlin + Jetpack Compose + MVVM + Testing

Este repositorio contiene el proyecto RightNow, desarrollado para el curso de Desarrollo de Aplicaciones Móviles.
A lo largo de las actividades se implementaron múltiples funcionalidades prácticas, integrando Kotlin, Jetpack Compose, MVVM, Testing, Consumo de API, Navegación, y DataStore.

🚀 Índice

Características principales

Arquitectura utilizada

Funcionalidades implementadas por actividad

Screens principales

Testing implementado

Tecnologías utilizadas

Ejecución del proyecto

🌟 Características principales

✔ Aplicación desarrollada en Kotlin
✔ UI con Jetpack Compose
✔ Navegación con Navigation Compose
✔ Persistencia con DataStore Preferences
✔ Consumo de API real: Rick & Morty REST API
✔ Arquitectura MVVM
✔ Scroll infinito
✔ Pruebas unitarias, asincrónicas y de UI
✔ Login / Register funcional
✔ Manejo de sesión con DataStore
✔ Diseño adaptable y moderno con Material 3

🏗️ Arquitectura utilizada

El proyecto está organizado bajo el patrón MVVM (Model - View - ViewModel):

🧩 Model

Clases de datos: RmCharacter, RmInfo

Repositorio: RickAndMortyRepository

DataStore local: UserLocalDataStore

🎨 View (Compose UI)

LoginScreen

RegisterScreen

MainScreen

NewsScreen

CharactersListScreen

CharacterDetailScreen

⚙️ ViewModel

CharactersViewModel

Manejo de estados con UiState

Coroutines + Flow para datos reactivos

📚 Funcionalidades implementadas por actividad
🔹 1. Actividad: Login, Registro y DataStore

✔ Pantalla de Login
✔ Pantalla de Registro
✔ Validaciones:

email requerido

contraseña mínimo 6 caracteres

✔ Guardado de usuario con DataStore
✔ Logout
✔ Redirección automática si hay sesión activa
✔ Navegación con parámetros:
main/{username}

🔹 2. Actividad: Consumo de API Rick & Morty

✔ Cliente Retrofit (incluido en template del curso)
✔ Llamado a la API para obtener lista de personajes
✔ Pantalla de lista (CharactersListScreen)
✔ Pantalla de detalle (CharacterDetailScreen)
✔ Carga + Error + Loading states
✔ Mostrado de imágenes con AsyncImage (Coil)

🔹 3. Actividad: MVVM + UiState

✔ Implementación completa de MVVM
✔ CharactersViewModel
✔ CharactersUiState con:

isLoading

isLoadingMore

endReached

errorMessage

characters: List<RmCharacter>

✔ Lógica separada de la UI
✔ Las composables solo observan estado

🔹 4. Actividad: Scroll Infinito

✔ Eliminación de botones "Anterior" / "Siguiente"
✔ Carga automática al llegar al final del LazyColumn
✔ Prevención de llamadas duplicadas
✔ Indicador de carga al final de la lista
✔ Mensaje "No hay más personajes"
✔ Manejo de errores al cargar más páginas

🔹 5. Actividad: Implementación de Pruebas Unitarias y UI

Se implementó una batería de pruebas en:

🧪 Unit Tests (src/test/java/)

✔ ValidationTest
Pruebas para:

isValidEmail

isMinLength

✔ CoroutineBehaviorTest
Uso de:

runTest

advanceTimeBy

advanceUntilIdle

✔ CharactersViewModelTest
MockK para simular el repositorio:

carga de primera página

validación del estado inicial

📱 UI Tests (src/androidTest/java/)

✔ LoginScreenTest
Validación de UI:

ingreso de datos

validación de contraseña corta

mensajes de error visibles

✔ Test instrumentado funcionando en emulador
✔ Uso de:

createComposeRule()

onNodeWithText()

performTextInput()

performClick()

assertExists()

🖼️ Screens principales
🔐 LoginScreen
🧾 RegisterScreen
🏠 MainScreen
📰 NewsScreen
👤 ProfileScreen
👽 CharactersListScreen (Scroll Infinito)
📄 CharacterDetailScreen

🧪 Testing implementado
| Tipo de Test    | Ubicación              | Tecnologías             |
| --------------- | ---------------------- | ----------------------- |
| Unit Tests      | `src/test/java`        | JUnit 5, Kotest, MockK  |
| UI Tests        | `src/androidTest/java` | Compose UI Test         |
| Async Tests     | `src/test/java`        | kotlinx-coroutines-test |
| ViewModel Tests | `src/test/java`        | MockK + coroutines-test |

🛠️ Tecnologías Utilizadas

Kotlin

Jetpack Compose

Material 3

Navigation Compose

DataStore Preferences

Retrofit / API Rick & Morty

Coroutines + Flow

MVVM

MockK

Kotest

Compose UI Test

JUnit 5

▶️ Ejecución del Proyecto

Clonar el repositorio
git clone https://github.com/18mili/RightNow-RickAndMorty.git

Abrir en Android Studio

Ejecutar en emulador o dispositivo

Para ejecutar tests:

Unit Tests: botón ▶ en cada clase

UI Tests: usar un emulador en ejecución

🎉 Estado del Proyecto

🟢 Proyecto completo
🧪 Testing implementado

