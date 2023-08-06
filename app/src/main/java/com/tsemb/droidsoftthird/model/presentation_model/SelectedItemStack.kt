package com.tsemb.droidsoftthird.model.presentation_model

import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.EditedPlace
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime

data class SelectedItemStack(
    val selectedDate:LocalDate? = null,
    val startTime: ZonedDateTime? = null,
    val duration: Duration? = null,
    val selectedPlace: EditedPlace? = null,
    val selectedGroup: ApiGroup? = null,
    val isOnline:Boolean? = null,
)