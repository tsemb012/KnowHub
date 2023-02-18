package com.example.droidsoftthird.model.domain_model

import java.time.LocalDate
import java.time.LocalTime

data class SelectedItemStack(
        val selectedDate:LocalDate? = null,
        val selectedPeriod:Pair<LocalTime, LocalTime>? = null,
        val selectedPlace:EditedPlace? = null,
        val selectedGroup:ApiGroup? = null,
)