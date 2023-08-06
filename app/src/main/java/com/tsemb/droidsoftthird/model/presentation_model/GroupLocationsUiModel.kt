package com.tsemb.droidsoftthird.model.presentation_model

import androidx.compose.runtime.State
import androidx.paging.PagingData
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.GroupCountByArea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class GroupLocationsUiModel (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val groupCountByArea: List<GroupCountByArea> = listOf(),
    val groupsBySelectedArea: Flow<PagingData<ApiGroup>> = emptyFlow(),
) {
    companion object {
        operator fun invoke(
            current: GroupLocationsUiModel,
            groupCountByAreaLoadState: LoadState,
            groupsBySelectedAreaLoadState: Flow<PagingData<ApiGroup>>,
        ) = GroupLocationsUiModel(
            isLoading = groupCountByAreaLoadState is LoadState.Loading,
            error = groupCountByAreaLoadState.getErrorOrNull(),
            groupCountByArea = groupCountByAreaLoadState.getValueOrNull() ?: current.groupCountByArea,
            groupsBySelectedArea = groupsBySelectedAreaLoadState,
        )
    }
}

val State<GroupLocationsUiModel?>.groupCountByArea get() = value?.groupCountByArea
val State<GroupLocationsUiModel?>.groupsBySelectedArea get() = value?.groupsBySelectedArea
