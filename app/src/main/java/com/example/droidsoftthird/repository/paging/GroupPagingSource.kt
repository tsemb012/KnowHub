package com.example.droidsoftthird.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.model.domain_model.ApiGroup

//GroupPagingSourceでテストを行う必要性が感じられないため、レポジトリー層で都度インスタンス化するように
class GroupPagingSource(
        private val api: MainApi,
        private val areaCode: Int?,
        private val areaCategory: String?
): PagingSource<Int, ApiGroup>() {


    override fun getRefreshKey(state: PagingState<Int, ApiGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiGroup> {
        return try {
            val nextPage = params.key ?: 1
            val groups = api.fetchGroups(nextPage, areaCode, areaCategory).map { it.toEntity() }

            LoadResult.Page(
                data = groups,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (groups.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
