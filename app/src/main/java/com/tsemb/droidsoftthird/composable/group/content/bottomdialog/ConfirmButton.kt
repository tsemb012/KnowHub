package com.tsemb.droidsoftthird.composable.group.content.bottomdialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.tsemb.droidsoftthird.composable.shared.SharedConfirmButton
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup

@Composable
fun ConfirmFilterButton(
    text : String,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onConfirm: (ApiGroup.FilterCondition) -> Unit
) {
    SharedConfirmButton(text) { onConfirm(temporalCondition.value) }
}


