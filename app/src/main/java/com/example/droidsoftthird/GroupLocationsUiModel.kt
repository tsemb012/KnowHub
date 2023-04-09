package com.example.droidsoftthird

import androidx.compose.runtime.State
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.GroupCountByArea
import com.example.droidsoftthird.model.presentation_model.LoadState

data class GroupLocationsUiModel (
        val isLoading: Boolean = false,
        val isBottomLoading: Boolean = false,
        val error: Throwable? = null,
        val groupCountByArea: List<GroupCountByArea> = listOf(),
        val groupsBySelectedArea: List<ApiGroup> = listOf(),
) {
    companion object {
        operator fun invoke(
            current: GroupLocationsUiModel,
            groupCountByAreaLoadState: LoadState,
            groupsBySelectedAreaLoadState: LoadState,
        ) = GroupLocationsUiModel(
            isLoading = groupCountByAreaLoadState is LoadState.Loading,
            isBottomLoading = groupsBySelectedAreaLoadState is LoadState.Loading,
            error = groupCountByAreaLoadState.getErrorOrNull() ?: groupsBySelectedAreaLoadState.getErrorOrNull(),
            groupCountByArea = groupCountByAreaLoadState.getValueOrNull() ?: current.groupCountByArea,
            groupsBySelectedArea = groupsBySelectedAreaLoadState.getValueOrNull() ?: current.groupsBySelectedArea,
        )
    }
}

val State<GroupLocationsUiModel?>.groupCountByArea get() = value?.groupCountByArea
val State<GroupLocationsUiModel?>.groupsBySelectedArea get() = value?.groupsBySelectedArea
