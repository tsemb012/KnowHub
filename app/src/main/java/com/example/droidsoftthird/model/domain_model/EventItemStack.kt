package com.example.droidsoftthird.model.domain_model

import java.time.LocalDate
import java.util.*

data class EventItemStack(
        val inputName : String? = null,
        val inputComment : String? = null,
        val selectedDate:LocalDate? = null,
        val selectedPeriod:Pair<Calendar, Calendar>? = null,
        val selectedPlace:EditedPlaceDetail? = null,
        val selectedGroup:ApiGroup? = null,
)