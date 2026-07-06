package eu.kanade.tachiyomi.ui.home.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.core.util.ifAnimeSourcesLoaded
import eu.kanade.core.util.ifMangaSourcesLoaded
import eu.kanade.presentation.components.SearchToolbar
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.ui.browse.anime.source.browse.BrowseAnimeSourceScreen
import eu.kanade.tachiyomi.ui.browse.anime.source.globalsearch.GlobalAnimeSearchScreenModel
import eu.kanade.tachiyomi.ui.browse.manga.source.browse.BrowseMangaSourceScreen
import eu.kanade.tachiyomi.ui.browse.manga.source.globalsearch.GlobalMangaSearchScreenModel
import eu.kanade.tachiyomi.ui.entries.anime.AnimeScreen
import eu.kanade.tachiyomi.ui.entries.manga.MangaScreen
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.screens.LoadingScreen
import eu.kanade.presentation.browse.anime.GlobalSearchContent as AnimeGlobalSearchContent
import eu.kanade.presentation.browse.manga.GlobalSearchContent as MangaGlobalSearchContent

private val TEAL = Color(0xFF35D0D0)
private val DARK_TEAL_TEXT = Color(0xFF04201F)

/**
 * Unified search: one query field that searches both anime and manga sources at once,
 * with an Anime | Manga toggle to switch which results are shown.
 */
class MiruUnifiedSearchScreen(
    private val initialQuery: String = "",
) : Screen() {

    @Composable
    override fun Content() {
        if (!ifAnimeSourcesLoaded() || !ifMangaSourcesLoaded()) {
            LoadingScreen()
            return
        }

        val navigator = LocalNavigator.currentOrThrow

        val animeModel = rememberScreenModel { GlobalAnimeSearchScreenModel(initialQuery = initialQuery) }
        val mangaModel = rememberScreenModel { GlobalMangaSearchScreenModel(initialQuery = initialQuery) }

        val animeState by animeModel.state.collectAsState()
        val mangaState by mangaModel.state.collectAsState()

        var query by remember { mutableStateOf(initialQuery) }
        // 0 = Anime, 1 = Manga
        var selectedTab by remember { mutableStateOf(0) }

        Scaffold(
            topBar = { scrollBehavior ->
                SearchToolbar(
                    searchQuery = query,
                    onChangeSearchQuery = { newQuery ->
                        if (newQuery == null) {
                            navigator.pop()
                        } else {
                            query = newQuery
                            animeModel.updateSearchQuery(newQuery)
                            mangaModel.updateSearchQuery(newQuery)
                        }
                    },
                    placeholderText = stringResource(MR.strings.miru_search_all_hint),
                    onSearch = {
                        animeModel.updateSearchQuery(it)
                        mangaModel.updateSearchQuery(it)
                        animeModel.search()
                        mangaModel.search()
                    },
                    navigateUp = navigator::pop,
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
            ) {
                MediaTypeToggle(
                    selectedTab = selectedTab,
                    onSelect = { selectedTab = it },
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    if (selectedTab == 0) {
                        AnimeGlobalSearchContent(
                            items = animeState.filteredItems,
                            contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                            getAnime = { animeModel.getAnime(it) },
                            onClickSource = { navigator.push(BrowseAnimeSourceScreen(it.id, query)) },
                            onClickItem = { navigator.push(AnimeScreen(it.id, true)) },
                            onLongClickItem = { navigator.push(AnimeScreen(it.id, true)) },
                        )
                    } else {
                        MangaGlobalSearchContent(
                            items = mangaState.filteredItems,
                            contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                            getManga = { mangaModel.getManga(it) },
                            onClickSource = { navigator.push(BrowseMangaSourceScreen(it.id, query)) },
                            onClickItem = { navigator.push(MangaScreen(it.id, true)) },
                            onLongClickItem = { navigator.push(MangaScreen(it.id, true)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaTypeToggle(
    selectedTab: Int,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TogglePill(
            label = "Anime",
            selected = selectedTab == 0,
            onClick = { onSelect(0) },
            modifier = Modifier.weight(1f),
        )
        TogglePill(
            label = "Manga",
            selected = selectedTab == 1,
            onClick = { onSelect(1) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TogglePill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) TEAL else Color.White.copy(alpha = 0.06f))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) DARK_TEAL_TEXT else Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
