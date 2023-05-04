package com.example.droidsoftthird.composable.group.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val temporalCondition = remember { mutableStateOf(filterCondition) }

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

                    // 都道府県リストのサンプルデータ
                    val prefectureItems = viewModel.prefectureList
                    val cityItems = viewModel.cityList

                    CascadeMenuExample(prefectureItems, cityItems)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        onConfirm(temporalCondition.value)

                                     }  , modifier = Modifier.fillMaxWidth()) {
                        Text("決定")
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
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

@Composable
fun CascadeMenuExample(samplePrefectures: List<PrefectureCsvLoader.PrefectureLocalItem>, sampleCities: List<CityCsvLoader.CityLocalItem>) {
    var selectedPrefecture: PrefectureCsvLoader.PrefectureLocalItem by remember { mutableStateOf(samplePrefectures[0]) }
    var selectedCity: CityCsvLoader.CityLocalItem by remember { mutableStateOf(sampleCities[0]) }

    Column {
        Text("都道府県を選択してください")

        DropDownMenuChip(
            selectedValue = selectedPrefecture,
            options = samplePrefectures,
            displayNameProvider = { it.name },
            onSelected = { selectedPrefecture = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropDownMenuChip(
            selectedValue = selectedCity,
            options = sampleCities.filter { it.prefectureCode == selectedPrefecture.code },
            displayNameProvider = { it.name },
            onSelected = { selectedCity = it }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropDownMenuChip(
    selectedValue: T,
    options : List<T>,
    displayNameProvider: @Composable (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var rememberSelectedValue by remember { mutableStateOf(selectedValue) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = displayNameProvider(rememberSelectedValue),
            onValueChange = { },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(item)
                        expanded = false
                        rememberSelectedValue = item
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(displayNameProvider(item))
                }
            }
        }
    }
}

/*@Preview
@Composable
fun PreviewFullScreenDialog() {
    FullScreenDialog(
        visible = true,
        title = "フルスクリーンダイアログ",
        filterCondition = ApiGroup.FilterCondition(
            groupTypes = setOf(GroupType.MOKUMOKU, GroupType.WORKSHOP),
            facilityEnvironments = setOf(FacilityEnvironment.LIBRARY, FacilityEnvironment.PARK),
            frequencyBasis = FrequencyBasis.WEEKLY,
        ),
        onCancel = { *//* キャンセルボタンが押されたときの処理 *//* },
        onConfirm = { *//* 決定ボタンが押されたときの処理 *//* }
    )
}*/
