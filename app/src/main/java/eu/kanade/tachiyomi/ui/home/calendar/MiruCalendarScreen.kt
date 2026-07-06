package eu.kanade.tachiyomi.ui.home.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.entries.components.ItemCover
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.ui.browse.anime.source.globalsearch.GlobalAnimeSearchScreen
import eu.kanade.tachiyomi.ui.home.MiruMediaItem
import eu.kanade.tachiyomi.ui.home.intelligence.MiruIntelSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource
import java.util.Calendar

private val TEAL = Color(0xFF35D0D0)
private val DARK_TEAL_TEXT = Color(0xFF04201F)

class MiruCalendarScreenModel : ScreenModel {

    data class State(
        val selectedDay: Int,
        val itemsByDay: Map<Int, List<MiruMediaItem>> = emptyMap(),
        val loadingDay: Int? = null,
    )

    // Monday-first order, matching Jikan's schedule filter values.
    private val dayKeys = listOf(
        "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday",
    )

    private val _state = MutableStateFlow(State(selectedDay = todayIndex()))
    val state = _state.asStateFlow()

    init {
        selectDay(_state.value.selectedDay)
    }

    fun selectDay(index: Int) {
        _state.update { it.copy(selectedDay = index) }
        if (_state.value.itemsByDay.containsKey(index)) return

        screenModelScope.launch {
            _state.update { it.copy(loadingDay = index) }
            val items = MiruIntelSystem.fetchScheduleForDay(dayKeys[index])
            _state.update { current ->
                current.copy(
                    itemsByDay = current.itemsByDay + (index to items),
                    loadingDay = if (current.loadingDay == index) null else current.loadingDay,
                )
            }
        }
    }

    companion object {
        fun todayIndex(): Int {
            val dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            // Calendar.SUNDAY = 1, MONDAY = 2, ... SATURDAY = 7
            return if (dow == Calendar.SUNDAY) 6 else dow - Calendar.MONDAY
        }
    }
}

object MiruCalendarScreen : Screen() {

    private fun readResolve(): Any = MiruCalendarScreen

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { MiruCalendarScreenModel() }
        val state by screenModel.state.collectAsState()

        val dayLabels = listOf(
            stringResource(MR.strings.miru_calendar_mon),
            stringResource(MR.strings.miru_calendar_tue),
            stringResource(MR.strings.miru_calendar_wed),
            stringResource(MR.strings.miru_calendar_thu),
            stringResource(MR.strings.miru_calendar_fri),
            stringResource(MR.strings.miru_calendar_sat),
            stringResource(MR.strings.miru_calendar_sun),
        )
        val todayIndex = MiruCalendarScreenModel.todayIndex()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000)),
        ) {
            // Top bar: back + title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { navigator.pop() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = stringResource(MR.strings.miru_calendar_title),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Day selector tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                dayLabels.forEachIndexed { index, label ->
                    val selected = index == state.selectedDay
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selected) TEAL else Color.White.copy(alpha = 0.06f))
                            .clickable { screenModel.selectDay(index) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = label,
                            color = when {
                                selected -> DARK_TEAL_TEXT
                                index == todayIndex -> TEAL
                                else -> Color.White
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            val items = state.itemsByDay[state.selectedDay].orEmpty()
            val isLoading = state.loadingDay == state.selectedDay && items.isEmpty()

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = TEAL, strokeWidth = 3.dp)
                    }
                }
                items.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(MR.strings.miru_calendar_empty),
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        items(items, key = { it.id }) { item ->
                            CalendarItem(
                                item = item,
                                onClick = { navigator.push(GlobalAnimeSearchScreen(item.title)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarItem(
    item: MiruMediaItem,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.67f)
                .clip(RoundedCornerShape(6.dp)),
        ) {
            ItemCover.Book(
                data = item.thumbnailUrl,
                modifier = Modifier.fillMaxSize(),
            )
            if (!item.airingTime.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(TEAL)
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = item.airingTime,
                        color = DARK_TEAL_TEXT,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 15.sp,
        )
    }
}
