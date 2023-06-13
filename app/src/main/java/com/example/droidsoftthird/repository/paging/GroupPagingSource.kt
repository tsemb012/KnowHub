package com.example.droidsoftthird.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException

//GroupPagingSourceでテストを行う必要性が感じられないため、レポジトリー層で都度インスタンス化するように
class GroupPagingSource(
        private val api: MainApi,
        private val userId: String?,
        private val groupFilterCondition: ApiGroup.FilterCondition
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
            val groups = api.fetchGroups(
                page = nextPage,
                userId = userId,
                areaCode = groupFilterCondition.areaCode,
                areaCategory = groupFilterCondition.areaCategory?.name?.lowercase(),
                groupTypes = groupFilterCondition.groupTypes.map { it.name.lowercase() },
                facilityEnvironments = groupFilterCondition.facilityEnvironments.map { it.name.lowercase() },
                frequency_bases = groupFilterCondition.frequencyBasis?.name?.lowercase(),
                allowMaxNumberGroupShow = groupFilterCondition.allowMaxNumberGroupShow,
            ).map { it.toEntity() }

            return LoadResult.Page(
                data = groups,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (groups.isEmpty()) null else nextPage + 1
            )
        } catch (e: HttpException) {
            // HTTPエラーが発生した場合、エラーメッセージをパースする
            LoadResult.Error(handleHttpException(e))
        } catch (e: Exception) {
            // その他のエラーはそのままスローする
            LoadResult.Error(e)
        }
    }
}

private fun handleHttpException(e: HttpException): Exception {
    val errorJsonString = e.response()?.errorBody()?.string()
    var errorMessage = "Unknown error occurred."

    if (errorJsonString != null) {
        errorMessage = try {
            // JSONをパースしてエラーメッセージを取得する
            val errorJson = JsonParser.parseString(errorJsonString).asJsonObject
            errorJson.get("error").asString
        } catch (jsonException: JsonSyntaxException) {
            // JSONのパースに失敗した場合、エラーメッセージをそのまま表示する
            errorJsonString
        }
    }
    // 適切なエラーメッセージを持つ新しい例外をスローする
    return Exception(errorMessage)
}
