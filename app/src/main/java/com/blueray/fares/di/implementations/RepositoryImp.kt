package com.blueray.fares.di.implementations

import com.blueray.fares.api.ApiServices
import com.blueray.fares.api.Repository
import com.blueray.fares.helpers.HelperUtils.toStringRequestBody
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    apiClient : ApiServices
) : Repository {


    override suspend fun test() {
//        this function is for test di

        // another way to make request bodies
        val testVar = "some string".toStringRequestBody()
    }

}