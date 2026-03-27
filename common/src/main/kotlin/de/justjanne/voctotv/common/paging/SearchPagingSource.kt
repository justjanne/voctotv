/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import de.justjanne.voctotv.common.service.VoctowebSearchService
import de.justjanne.voctotv.voctoweb.model.LectureModel

class SearchPagingSource(
    private val service: VoctowebSearchService,
    private val query: String,
) : PagingSource<Int, LectureModel>() {
    override fun getRefreshKey(state: PagingState<Int, LectureModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LectureModel> {
        val page = params.key ?: 1
        val results = service.search(query, page)

        return if (results != null) {
            LoadResult.Page(
                data = results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (results.isEmpty()) null else page + 1,
            )
        } else {
            LoadResult.Error(Exception("Failed to fetch search results"))
        }
    }
}
