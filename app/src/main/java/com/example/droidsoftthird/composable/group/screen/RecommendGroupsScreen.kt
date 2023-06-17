package com.example.droidsoftthird.composable.group.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.R
import com.example.droidsoftthird.RecommendGroupsViewModel
import com.example.droidsoftthird.composable.group.content.PagingGroupList
import com.example.droidsoftthird.model.domain_model.*
import com.example.droidsoftthird.model.domain_model.ApiGroup.FilterCondition.Companion.getPrefectureCode
import com.example.droidsoftthird.repository.csvloader.CityCsvLoader
import com.example.droidsoftthird.repository.csvloader.PrefectureCsvLoader

enum class FilterContentDestination { HOME, PREFECTURE, CITY }

@Composable
fun RecommendGroupsScreen(
    viewModel: RecommendGroupsViewModel,
    navigateToGroupDetail: (String) -> Unit,
    navigateToGroupAdd: () -> Unit,
) {

    val lazyPagingGroups = viewModel.groupsFlow.collectAsLazyPagingItems()
    val filterCondition = viewModel.groupFilterCondition
    val showDialog = remember { mutableStateOf(false) }
    val areaMap = viewModel.prefectureList to viewModel.cityList
    val onConfirm: (ApiGroup.FilterCondition?) -> Unit = {
            condition -> viewModel.updateFilterCondition(condition ?: ApiGroup.FilterCondition())
    }

    DisplayGroupListWithFab(showDialog, lazyPagingGroups, navigateToGroupDetail, navigateToGroupAdd)
    AnimatedFilterConditionDialog(showDialog, areaMap, filterCondition, onConfirm)
}

@Composable
fun DisplayGroupListWithFab(
    showDialog: MutableState<Boolean>,
    lazyPagingGroups: LazyPagingItems<ApiGroup>,
    navigateToGroupDetail: (String) -> Unit,
    navigateToGroupAdd: () -> Unit
) {
    Box {
        PagingGroupList(lazyPagingGroups, navigateToGroupDetail)
        FloatingActionButtons(showDialog, navigateToGroupAdd)
    }
}

@Composable
private fun BoxScope.FloatingActionButtons(
    showDialog: MutableState<Boolean>,
    navigateToGroupAdd: () -> Unit
) {
    Column (modifier = Modifier
        .padding(bottom = 64.dp, end = 16.dp)
        .align(Alignment.BottomEnd)) {
        FloatingActionButton(
            onClick = { showDialog.value = true },
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Icon(Icons.Filled.FilterList, contentDescription = "Filter")
        }
        FloatingActionButton(
            onClick = { navigateToGroupAdd() }
        ) {
           Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun AnimatedFilterConditionDialog(
    showDialog: MutableState<Boolean>,
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    filterCondition: State<ApiGroup.FilterCondition?>,
    onConfirm: (ApiGroup.FilterCondition?) -> Unit
) {
    if (showDialog.value) {
        val animatedVisibility = remember { mutableStateOf(showDialog.value) }
        val enterTransition = slideInVertically(initialOffsetY = { it })
        val exitTransition = slideOutVertically(targetOffsetY = { it })

        AnimatedVisibility(
            visible = animatedVisibility.value,
            enter = enterTransition + slideIn(animationSpec = tween(300), initialOffset = { IntOffset.Zero }),
            exit = exitTransition + slideOut(animationSpec = tween(300), targetOffset = { IntOffset.Zero }),
        ) {

            FilterConditionDialog(
                title = stringResource(id = R.string.group_condition),
                areaMap,
                filterCondition = filterCondition.value ?: ApiGroup.FilterCondition(),
                onCancel = { showDialog.value = false }
            ) { temporalCondition ->
                onConfirm(temporalCondition)
                showDialog.value = false
            }

        }
    }
}

@Composable
fun FilterConditionDialog(
    title: String,
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    filterCondition: ApiGroup.FilterCondition,
    onCancel: () -> Unit,
    onConfirm: (ApiGroup.FilterCondition) -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 50.dp),
            shape = MaterialTheme.shapes.medium
        ) { FilterConditionContent(title, areaMap, filterCondition, onCancel, onConfirm) }
    }
}

@Composable
private fun FilterConditionContent(
    title: String,
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    filterCondition: ApiGroup.FilterCondition,
    onCancel: () -> Unit,
    onConfirm: (ApiGroup.FilterCondition) -> Unit,
) {
    val destination = remember { mutableStateOf(FilterContentDestination.HOME) }
    val temporalCondition = remember { mutableStateOf(filterCondition) }
    when (destination.value) {
        FilterContentDestination.HOME -> filterHomeContent(title, destination, areaMap, temporalCondition, onCancel, onConfirm)
        FilterContentDestination.PREFECTURE -> displayPrefectureListContent(destination, areaMap.first, temporalCondition)
        FilterContentDestination.CITY -> displayCityListContent(destination, areaMap.second, temporalCondition)
    }
}


@Composable
fun displayPrefectureListContent(
    destination: MutableState<FilterContentDestination>,
    prefectureList: List<PrefectureCsvLoader.PrefectureLocalItem>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>
) {
    listDialog(
        "活動地域を選択してください",
        prefectureList,
        { destination.value = FilterContentDestination.HOME },
        {
            temporalCondition.value = temporalCondition.value.copy(areaCode = null, areaCategory = null)
            destination.value = FilterContentDestination.HOME
        }
    ) {
        temporalCondition.value = temporalCondition.value.copy(areaCode = it.code, areaCategory = AreaCategory.PREFECTURE)
        destination.value = FilterContentDestination.CITY
    }
}

@Composable
fun displayCityListContent(
    destination: MutableState<FilterContentDestination>,
    cityList: List<CityCsvLoader.CityLocalItem>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>
) {
    listDialog2("活動地域を選択してください", cityList.filter { temporalCondition.value.areaCode == it.prefectureCode }, { destination.value = FilterContentDestination.PREFECTURE }, { destination.value = FilterContentDestination.HOME }) {
        temporalCondition.value = temporalCondition.value.copy(areaCode = it.cityCode, areaCategory = AreaCategory.CITY)
        destination.value = FilterContentDestination.HOME
    }
}

    @Composable
fun listDialog2(
    label: String,
    items: List<CityCsvLoader.CityLocalItem>,
    onBack: () -> Unit,
    onNonSelected: () -> Unit,
    onSelected: (CityCsvLoader.CityLocalItem) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h6,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(onClick = onBack) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }

        Divider()

        LazyColumn(content = {
            item {
                //指定しない
                Text(
                    text = "指定しない",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onNonSelected()
                        })
                        .padding(16.dp),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
            items(items.size) { index ->
                val item = items[index]
                Text(
                    text = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onSelected(item)
                        })
                        .padding(16.dp),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
        })
    }
}

@Composable
fun listDialog(
    label: String,
    items: List<PrefectureCsvLoader.PrefectureLocalItem>,
    onBack: () -> Unit,
    onNonSelected: () -> Unit,
    onSelected: (PrefectureCsvLoader.PrefectureLocalItem) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.h6,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(onClick = onBack) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }

        Divider()

        LazyColumn(content = {
            item {
                Text(
                    text = "指定しない",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onNonSelected()
                        })
                        .padding(16.dp),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
            items(items.size) { index ->
                val item = items[index]
                Text(
                    text = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onSelected(item)
                        })
                        .padding(16.dp),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
        })
    }
}

@Composable
private fun filterHomeContent(
    title: String,
    destination: MutableState<FilterContentDestination>,
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onCancel: () -> Unit,
    onConfirm: (ApiGroup.FilterCondition) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        headerRow(title, onCancel, temporalCondition)
        Spacer(modifier = Modifier.height(24.dp))

        activityAreaSection(areaMap, temporalCondition, destination)
        Spacer(modifier = Modifier.height(8.dp))

        MultiSelectSection(temporalCondition)
        SingleSelectSection(temporalCondition)

        Spacer(modifier = Modifier.height(48.dp))
        decisionButton(temporalCondition, onConfirm)
    }
}

@Composable
private fun SingleSelectSection(temporalCondition: MutableState<ApiGroup.FilterCondition>) {

    SingleSelectChipFlow(
        stringResource(R.string.learning_frequency),
        Icons.Default.DateRange,
        FrequencyBasis.toArrayForDisplay(),
        temporalCondition.value.frequencyBasis,
        { stringResource(id = it?.displayNameId!!) },
        { temporalCondition.value = temporalCondition.value.copy(frequencyBasis = it) }
    )

}

@Composable
private fun MultiSelectSection(temporalCondition: MutableState<ApiGroup.FilterCondition>) {
    MultiSelectChipFlow(
        title = stringResource(id = R.string.group_type),
        icon = Icons.Default.Group,
        items = GroupType.toArrayForDisplay(),
        stringProvider = { stringResource(id = it.displayNameId) },
        selectedItems = temporalCondition.value.groupTypes,
        onSelected = { temporalCondition.value = temporalCondition.value.copy(groupTypes = it) }

    )
    MultiSelectChipFlow(
        title = stringResource(R.string.facility_environment),
        icon = Icons.Default.LocationCity,
        items = FacilityEnvironment.toArrayForDisplay(),
        stringProvider = { stringResource(id = it.displayNameId) },
        selectedItems = temporalCondition.value.facilityEnvironments,
        onSelected = { temporalCondition.value = temporalCondition.value.copy(facilityEnvironments = it) }
    )
}

@Composable
private fun headerRow(
    title: String,
    onCancel: () -> Unit,
    temporalCondition: MutableState<ApiGroup.FilterCondition>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCancel) {
            Icon(Icons.Filled.Close, contentDescription = "キャンセル")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.clear_filter),
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable(onClick = {
                    temporalCondition.value = ApiGroup.FilterCondition()
                })
        )
    }
}

@Composable
private fun activityAreaSection(
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    destination: MutableState<FilterContentDestination>
) {
    LabelItem(
        title = stringResource(id = R.string.activity_area),
        icon = Icons.Default.LocationOn,
    )
    val activityAreaLabel = when (temporalCondition.value.areaCategory) {
        AreaCategory.PREFECTURE ->
            areaMap.first.firstOrNull { it.code == temporalCondition.value.areaCode }?.name
        AreaCategory.CITY -> {
            val prefectureCode = temporalCondition.value.areaCode?.let { getPrefectureCode(it) }
            areaMap.first.firstOrNull { it.code == prefectureCode }?.name + " , " +
                areaMap.second.firstOrNull { it.cityCode == temporalCondition.value.areaCode }?.name
        }
        else -> null
    }

    TransparentButton(
        text = activityAreaLabel ?: stringResource(id = R.string.select_area),
        onClick = { destination.value = FilterContentDestination.PREFECTURE }
    )
}

@Composable
private fun TransparentButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.button,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = "ArrowRight",
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

    @Composable
private fun decisionButton(
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onConfirm: (ApiGroup.FilterCondition) -> Unit
) {
    Button(onClick = { onConfirm(temporalCondition.value) }, modifier = Modifier.fillMaxWidth()) {
        Text("決定")
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun <T> MultiSelectChipFlow(
    title: String,
    icon: ImageVector,
    items: List<T>,
    selectedItems: Set<T>,
    stringProvider: @Composable (T) -> String,
    onSelected: (Set<T>) -> Unit
) {
    val rememberSelectedItems = remember(selectedItems) { mutableStateOf(selectedItems) }
    LabelItem(
        title = title,
        icon = icon
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        items.forEach { item ->
            val isSelected = rememberSelectedItems.value.contains(item)
            ChipItem(
                label = stringProvider(item),
                isSelected = isSelected,
                onClick = {
                    rememberSelectedItems.value = rememberSelectedItems.value.toMutableSet().apply {
                        if (isSelected) {
                            remove(item)
                        } else {
                            add(item)
                        }
                    }
                    onSelected(rememberSelectedItems.value)
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun <T> SingleSelectChipFlow(
    title: String,
    icon: ImageVector,
    items: List<T>,
    selectedItem: T,
    stringProvider: @Composable (T) -> String,
    onSelected: (T) -> Unit
) {
    val rememberSelectedItem = remember(selectedItem) { mutableStateOf(selectedItem) }
    LabelItem(
        title = title,
        icon = icon
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
    ) {
        items.forEach { item ->
            val isSelected = rememberSelectedItem.value == item
            ChipItem(
                label = stringProvider(item),
                isSelected = isSelected,
                onClick = {
                    rememberSelectedItem.value = item
                    onSelected(rememberSelectedItem.value)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }

    Chip(
        modifier = Modifier.padding(start = 4.dp, end = 4.dp),
        colors = ChipDefaults.chipColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onSurface
        ),
        onClick = onClick
    ) {
        Text(label)
    }
}

@Composable
fun LabelItem(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold)
        }
    }
}