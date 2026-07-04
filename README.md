<div align="center">

# Miru

**Anime & manga en una sola app.** Dirige tu propia biblioteca, mira y lee desde tus fuentes favoritas.

Android 8.0+ · Kotlin · Jetpack Compose

</div>

---

## ¿Qué es Miru?

Miru es una aplicación Android para **ver anime y leer manga**, con una experiencia moderna:
biblioteca unificada, reproductor de video avanzado, lector configurable, sincronización con
trackers y un ecosistema de extensiones para las fuentes de contenido.

## Características

- **Anime + manga** en una sola biblioteca.
- **Reproductor** basado en mpv con soporte de subtítulos (ASS/SSA), gestos y control de velocidad.
- **Lector** de manga configurable: dirección de lectura, filtros de color, doble página.
- **Home** con recomendaciones y categorías automáticas por género (vía Jikan).
- **Trackers**: MyAnimeList, AniList, Kitsu, Shikimori, Bangumi.
- **Backup y restauración** local de la biblioteca.
- **Extensiones** para agregar fuentes de anime y manga.

## Instalación

Descarga el APK más reciente desde la landing de Miru o la sección de Releases.
Para instalar necesitas permitir "instalar apps de orígenes desconocidos" en Android.

> En dispositivos Xiaomi/HyperOS, activa además **Instalar vía USB** en Opciones de desarrollador.

## Compilar desde el código

Requisitos: **JDK 17+** (o el JBR de Android Studio) y el **Android SDK** (compileSdk 35,
build-tools 35.0.1, NDK 27.1.12297006).

```bash
# Debug APK (todas las arquitecturas + universal)
./gradlew :app:assembleDebug
```

Los APK quedan en `app/build/outputs/apk/debug/`.

Para un build de **release** firmado, copia `keystore.properties.example` a `keystore.properties`
y completa tus credenciales de firma.

## Créditos

Miru está construido sobre el trabajo de código abierto de
[Aniyomi](https://github.com/aniyomiorg/aniyomi) y [Mihon](https://github.com/mihonapp/mihon)
(a su vez basado en Tachiyomi). Gracias a esas comunidades.

## Licencia

Distribuido bajo la licencia **Apache 2.0**. Ver [LICENSE](LICENSE).

Miru es solo una interfaz: no aloja ni distribuye contenido. Las fuentes de contenido provienen
de extensiones de terceros y son responsabilidad de quien las utiliza.
