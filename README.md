# ChatGamesNK

Plugin de minijuegos de chat para **Nukkit PetteriM1 Edition**, inspirado funcionalmente en [RareHyperIonYT/ChatGames](https://github.com/RareHyperIonYT/ChatGames) y adaptado a Nukkit.

## Características
- Cinco categorías iniciales con cinco desafíos por categoría (25 en total).
- Partidas automáticas y comandos para iniciar, detener, listar y recargar.
- Una sola ronda activa; gana el primer jugador que responde correctamente.
- Recompensas con EconomyAPI 2.x.
- Sonido al acertar configurable y desactivable.
- Mensajes centralizados en lang/es_ES.yml.
- Preguntas y variantes editables en archivos YAML independientes.
- Estadísticas persistentes en stats.yml.

## Requisitos
- Nukkit PetteriM1 Edition y Java 17.
- EconomyAPI 2.x para entregar recompensas.

## Compilar

El proyecto depende de la API de Nukkit PetteriM1 Edition. Primero instala esa API en el repositorio Maven local desde la rama `release`:

```bash
git clone --branch release https://github.com/BrunoJaegerK/NukkitPetteriM1Edition.git
cd NukkitPetteriM1Edition
mvn -DskipTests install
cd ..
```

Después, desde la carpeta raíz de ChatGamesNK:

```bash
mvn clean package
```

El artefacto esperado es `target/ChatGamesNK.jar`. El workflow de GitHub Actions realiza ambos pasos automáticamente. EconomyAPI se integra por reflexión en tiempo de ejecución, por lo que no hace falta descargar su JAR para compilar; sí debe estar instalado en el servidor para pagar recompensas.

## Instalar
1. Coloca ChatGamesNK.jar y EconomyAPI en plugins/.
2. Inicia el servidor para generar la configuración.
3. Edita config.yml, lang/es_ES.yml y los archivos de games/.

## Comandos
| Comando | Permiso | Función |
|---|---|---|
| /chatgames start <juego o random> | chatgames.admin | Inicia un juego |
| /chatgames stop | chatgames.admin | Detiene la ronda |
| /chatgames list | chatgames.admin | Lista juegos |
| /chatgames reload | chatgames.admin | Recarga archivos |
| /chatgames stats <jugador> | chatgames.stats | Consulta estadísticas del jugador conectado |

Alias: /cg.

## Añadir preguntas
Agrega una entrada a questions en el archivo YAML del minijuego:
- question: "&e¿Cuánto es 10 x 10?"
  answer: "100"

Para opción múltiple añade options y configura answer con la letra correcta (por ejemplo B).

## Almacenamiento
La primera versión usa YAML para las estadísticas. No necesita MongoDB, MySQL, H2 ni SQLite: los saldos los administra EconomyAPI y las estadísticas actuales son pequeñas. No se añade una base de datos externa sin necesidad; si luego se requiere clasificación global de alto volumen, se puede incorporar un adaptador configurable.

## Créditos
- Referencia funcional: https://github.com/RareHyperIonYT/ChatGames
- Economía: https://cloudburstmc.org/resources/economyapi.14/
- Autor: BrunoJaegerK.
