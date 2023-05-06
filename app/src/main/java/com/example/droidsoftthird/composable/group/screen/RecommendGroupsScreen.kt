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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.R
import com.example.droidsoftthird.RecommendGroupsViewModel
import com.example.droidsoftthird.composable.group.content.PagingGroupList
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.FacilityEnvironment
import com.example.droidsoftthird.model.domain_model.FrequencyBasis
import com.example.droidsoftthird.model.domain_model.GroupType
import com.example.droidsoftthird.repository.csvloader.CityCsvLoader
import com.example.droidsoftthird.repository.csvloader.PrefectureCsvLoader

@Composable
fun RecommendGroupsScreen(
    viewModel: RecommendGroupsViewModel,
    navigateToGroupDetail: (String) -> Unit,
    onConfirm: (ApiGroup.FilterCondition?) -> Unit
) {

    val error = viewModel.errorLiveData

    val lazyPagingGroups = viewModel.groupsFlow.collectAsLazyPagingItems()

    val filterCondition = viewModel.groupFilterCondition

    val showDialog = remember { mutableStateOf(false) }

    Box {
        PagingGroupList(lazyPagingGroups, navigateToGroupDetail)
        FloatingActionButton(
            onClick = { showDialog.value = true },
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(bottom = 64.dp)
                .align(Alignment.BottomEnd),
        ) {
            Icon(Icons.Filled.FilterList, contentDescription = "Filter")
        }
    }



    if (showDialog.value) {


        FullScreenDialog(
            viewModel,
            visible = showDialog.value,
            title = "フルスクリーンダイアログ",
            filterCondition = filterCondition.value,
            onCancel = { showDialog.value = false },
            onConfirm = {
                onConfirm(it)
                showDialog.value = false
            }
        )
    }
}


@Composable
fun FullScreenDialog(
    viewModel: RecommendGroupsViewModel,
    visible: Boolean,
    title: String,
    filterCondition: ApiGroup.FilterCondition,
    onCancel: () -> Unit,
    onConfirm: (ApiGroup.FilterCondition) -> Unit,
) {
    val animatedVisibility = remember { mutableStateOf(visible) }
    val enterTransition = slideInVertically(initialOffsetY = { it })
    val exitTransition = slideOutVertically(targetOffsetY = { it })

    AnimatedVisibility(
        visible = animatedVisibility.value,
        enter = enterTransition + slideIn(animationSpec = tween(300), initialOffset = { IntOffset.Zero}),
        exit = exitTransition + slideOut(animationSpec = tween(300), targetOffset = { IntOffset.Zero}),
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
            ) {

                val destination = remember { mutableStateOf("regular") }
                val temporalCondition = remember { mutableStateOf(filterCondition) }

                when (destination.value) {
                    "regular" -> regularDialog(temporalCondition, onCancel, title, destination, viewModel, onConfirm)
                    "prefecture" -> { listDialog("活動地域を選択してください", viewModel.prefectureList, { destination.value = "regular" }) {
                        temporalCondition.value = temporalCondition.value.copy(areaCode = it.code, areaCategory = "prefecture")
                        destination.value = "city" }
                    }
                    "city" -> {
                        listDialog2("活動地域を選択してください", viewModel.cityList.filter { temporalCondition.value.areaCode == it.prefectureCode }, { destination.value = "prefecture" }, { destination.value = "regular" }) {
                            temporalCondition.value = temporalCondition.value.copy(areaCode = it.cityCode, areaCategory = "city")
                            destination.value = "regular"
                        }
                    }
                }


            }
        }
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
                            onBack()
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
private fun regularDialog(
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onCancel: () -> Unit,
    title: String,
    destination: MutableState<String>,
    viewModel: RecommendGroupsViewModel,
    onConfirm: (ApiGroup.FilterCondition) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Cancel, contentDescription = "キャンセル")
            }
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.size(48.dp)) // IconButtonと同じサイズのスペーサー
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoItem(
            title = "活動地域",
            icon = Icons.Default.LocationOn,
        )
        val activityAreaLabel = if (temporalCondition.value.areaCategory == "prefecture") {
            viewModel.prefectureList.firstOrNull { it.code == temporalCondition.value.areaCode }?.name
        } else {
            viewModel.cityList.firstOrNull { it.cityCode == temporalCondition.value.areaCode }?.name
        }
        Text(text = activityAreaLabel ?: "選択してください", Modifier.clickable(onClick = {
            destination.value = "prefecture"
        }))
        Spacer(modifier = Modifier.height(8.dp))

        val groupTypeTitle = stringResource(R.string.group_type)
        val groupTypeIcon = Icons.Default.Group
        val groupTypeList = GroupType.toArrayForDisplay()
        val selectedType = temporalCondition.value.groupTypes

        ChipFlow(
            groupTypeTitle,
            groupTypeIcon,
            groupTypeList,
            selectedType,
            { stringResource(id = it.displayNameId) },
            { temporalCondition.value = temporalCondition.value.copy(groupTypes = it) }
        )

        val facilityEnvironmentTitle = stringResource(R.string.facility_environment)
        val facilityEnvironmentIcon = Icons.Default.Build
        val facilityEnvironmentList = FacilityEnvironment.toArrayForDisplay()
        val facilityEnvironmentSelectedType = temporalCondition.value.facilityEnvironments

        ChipFlow(
            facilityEnvironmentTitle,
            facilityEnvironmentIcon,
            facilityEnvironmentList,
            facilityEnvironmentSelectedType,
            { stringResource(id = it.displayNameId) },
            { temporalCondition.value = temporalCondition.value.copy(facilityEnvironments = it) }
        )

        val aTitle = stringResource(R.string.learning_frequency)
        val aIcon = Icons.Default.AcUnit
        val alist = FrequencyBasis.toArrayForDisplay()
        val atype = temporalCondition.value.frequencyBasis

        ChipFlowA(
            aTitle,
            aIcon,
            alist,
            atype,
            { stringResource(id = it?.displayNameId!!) },
            { temporalCondition.value = temporalCondition.value.copy(frequencyBasis = it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            onConfirm(temporalCondition.value)

        }, modifier = Modifier.fillMaxWidth()) {
            Text("決定")
        }


        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun <T> ChipFlow(
    title: String,
    icon: ImageVector,
    items: List<T>,
    selectedItems: Set<T>,
    stringProvider: @Composable (T) -> String,
    onSelected: (Set<T>) -> Unit
) {
    val rememberSelectedItems = remember { mutableStateOf(selectedItems) }
    ProfileInfoItem(
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
private fun <T> ChipFlowA(
    title: String,
    icon: ImageVector,
    items: List<T>,
    selectedItem: T,
    stringProvider: @Composable (T) -> String,
    onSelected: (T) -> Unit
) {
    val rememberSelectedItem = remember { mutableStateOf(selectedItem) }
    ProfileInfoItem(
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
fun ProfileInfoItem(title: String, icon: ImageVector) {
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
