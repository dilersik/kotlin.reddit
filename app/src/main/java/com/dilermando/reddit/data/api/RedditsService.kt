package com.dilermando.reddit.data.api

import com.dilermando.reddit.data.entity.Reddit
import com.dilermando.reddit.domain.utils.Constants
import com.dilermando.reddit.domain.utils.Constants.QUERY_LIMIT
import com.dilermando.reddit.domain.utils.Constants.QUERY_PAGINATE_AFTER
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditsService {

//    @GET(Constants.QUERY_REDDITS)
//    fun getPaginatedReddits(
//            @Query(QUERY_PAGINATE_AFTER) nextPage: String = "",
//            @Query(QUERY_LIMIT) limit: Int = Constants.DEFAULT_LIMIT): Call<Reddit>@GET(Constants.QUERY_REDDITS)

    @GET(Constants.QUERY_REDDITS)
    fun getPaginatedReddits(
            @Query(QUERY_PAGINATE_AFTER) nextPage: String = "",
            @Query(QUERY_LIMIT) limit: Int = Constants.DEFAULT_LIMIT): Observable<Reddit>

}
