package com.tsemb.droidsoftthird.composable.group.content.bottomdialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.IconLabelItem
import com.tsemb.droidsoftthird.composable.group.screen.FilterContentDestination
import com.tsemb.droidsoftthird.composable.shared.chip_flow.MultiSelectChipFlow
import com.tsemb.droidsoftthird.composable.shared.chip_flow.SingleSelectChipFlow
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.AreaCategory
import com.tsemb.droidsoftthird.model.domain_model.FacilityEnvironment
import com.tsemb.droidsoftthird.model.domain_model.FrequencyBasis
import com.tsemb.droidsoftthird.model.domain_model.GroupType
import com.tsemb.droidsoftthird.model.domain_model.Style
import com.tsemb.droidsoftthird.repository.csvloader.CityCsvLoader
import com.tsemb.droidsoftthird.repository.csvloader.PrefectureCsvLoader

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
            enter = enterTransition + slideIn(
                animationSpec = tween(300),
                initialOffset = { IntOffset.Zero }),
            exit = exitTransition + slideOut(
                animationSpec = tween(300),
                targetOffset = { IntOffset.Zero }),
        ) {
            FilterConditionDialog(
                title = stringResource(id = R.string.group_condition_filter),
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
private fun FilterConditionDialog(
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
            shape = MaterialTheme.shapes.medium,
            color = colorResource(id = R.color.base_100)
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
        FilterContentDestination.HOME -> FilterHomeContent(title, destination, areaMap, temporalCondition, onCancel, onConfirm)
        FilterContentDestination.PREFECTURE -> DisplayPrefectureListContent(destination, areaMap.first, temporalCondition)
        FilterContentDestination.CITY -> DisplayCityListContent(destination, areaMap.second, temporalCondition)
    }
}

@Composable
fun DisplayPrefectureListContent(
    destination: MutableState<FilterContentDestination>,
    prefectureList: List<PrefectureCsvLoader.PrefectureLocalItem>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>
) {
    ListDialog(
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
fun DisplayCityListContent(
    destination: MutableState<FilterContentDestination>,
    cityList: List<CityCsvLoader.CityLocalItem>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>
) {
    ListDialog2("活動地域を選択してください", cityList.filter { temporalCondition.value.areaCode == it.prefectureCode }, { destination.value = FilterContentDestination.PREFECTURE }, { destination.value = FilterContentDestination.HOME }) {
        temporalCondition.value = temporalCondition.value.copy(areaCode = it.cityCode, areaCategory = AreaCategory.CITY)
        destination.value = FilterContentDestination.HOME
    }
}

@Composable
private fun ListDialog2(
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
                )
            }
        })
    }
}

@Composable
fun ListDialog(
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
                )
            }
        })
    }
}

@Composable
private fun FilterHomeContent(
    title: String,
    destination: MutableState<FilterContentDestination>,
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onCancel: () -> Unit,
    onConfirm: (ApiGroup.FilterCondition) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .align(Alignment.TopCenter),
        ) {
            headerRow(title, onCancel, temporalCondition)
            Spacer(modifier = Modifier.height(12.dp))

            ActivityAreaSection(areaMap, temporalCondition, destination)
            Spacer(modifier = Modifier.height(4.dp))

            MultiSelectSection(temporalCondition)
            SingleSelectSection(temporalCondition)

            Spacer(modifier = Modifier.height(48.dp))
        }
        Column(
            modifier = Modifier
                .background(color = colorResource(id = R.color.base_100))
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            Divider()
            ConfirmFilterButton(stringResource(id = R.string.group_condition_filter_confirm) ,temporalCondition, onConfirm)
        }
    }
}

@Composable
private fun SingleSelectSection(temporalCondition: MutableState<ApiGroup.FilterCondition>) {

    SingleSelectChipFlow(
        title = stringResource(R.string.event_frequency),
        icon = Icons.Default.DateRange,
        items = FrequencyBasis.toArrayForDisplay(),
        selectedItem = temporalCondition.value.frequencyBasis,
        stringProvider = { stringResource(id = it?.displayNameId!!) },
        onSelected = { temporalCondition.value = temporalCondition.value.copy(frequencyBasis = it) }
    )

    SingleSelectChipFlow(
        title = stringResource(id = R.string.group_type),
        icon = Icons.Default.LocalFireDepartment,
        items = GroupType.toArrayForDisplay(),
        selectedItem = temporalCondition.value.groupType,
        stringProvider = { stringResource(id = it?.displayNameId!!) },
        onSelected = { temporalCondition.value = temporalCondition.value.copy(groupType = it) }
    )

    SingleSelectChipFlow(
        title = stringResource(id = R.string.style),
        icon = Icons.Default.SentimentSatisfied,
        items = Style.toArrayForDisplay(),
        selectedItem = temporalCondition.value.style,
        stringProvider = { stringResource(id = it?.displayNameId!!) },
        onSelected = { temporalCondition.value = temporalCondition.value.copy(style = it) }
    )
}

@Composable
private fun MultiSelectSection(temporalCondition: MutableState<ApiGroup.FilterCondition>) {
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
private fun ActivityAreaSection(
    areaMap: Pair<List<PrefectureCsvLoader.PrefectureLocalItem>, List<CityCsvLoader.CityLocalItem>>,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    destination: MutableState<FilterContentDestination>
) {
    IconLabelItem(
        title = stringResource(id = R.string.activity_area),
        icon = Icons.Default.LocationOn,
    )
    val activityAreaLabel = when (temporalCondition.value.areaCategory) {
        AreaCategory.PREFECTURE ->
            areaMap.first.firstOrNull { it.code == temporalCondition.value.areaCode }?.name
        AreaCategory.CITY -> {
            val prefectureCode = temporalCondition.value.areaCode?.let {
                ApiGroup.FilterCondition.getPrefectureCode(
                    it
                )
            }
            areaMap.first.firstOrNull { it.code == prefectureCode }?.name + " , " +
                areaMap.second.firstOrNull { it.cityCode == temporalCondition.value.areaCode }?.name
        }
        else -> null
    }

    TransitionButton(
        text = activityAreaLabel ?: stringResource(id = R.string.select_area),
        onClick = { destination.value = FilterContentDestination.PREFECTURE }
    )
}
