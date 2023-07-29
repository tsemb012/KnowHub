package com.example.droidsoftthird.composable.group.content.bottomdialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.droidsoftthird.composable.shared.SharedConfirmButton
import com.example.droidsoftthird.model.domain_model.ApiGroup

@Composable
fun ConfirmFilterButton(
    text : String,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onConfirm: (ApiGroup.FilterCondition) -> Unit
) {
    SharedConfirmButton(text) { onConfirm(temporalCondition.value) }
}


