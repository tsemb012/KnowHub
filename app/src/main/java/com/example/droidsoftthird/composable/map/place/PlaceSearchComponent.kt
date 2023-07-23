package com.example.droidsoftthird.composable.map.place

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.droidsoftthird.composable.ChipGroup
import com.example.droidsoftthird.composable.SearchBox
import com.example.droidsoftthird.model.domain_model.Category

@Composable
fun PlaceSearchComponent(
    searchByText: (query: String) -> Unit,
    searchByCategory: (category: Category) -> Unit
) {
    Column {
        SearchBox { query -> searchByText(query) }
        ChipGroup(Category.values()) { category -> searchByCategory(category) }

        //TOOD AutoCompleteの実装はSearchBoxとまとめて行う。
        /*modifier = Modifier
                    .height(56.dp)
                    .padding(top = 16.dp))*/
    }
}