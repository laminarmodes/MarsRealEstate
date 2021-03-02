/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.lang.Exception

// API status enum
enum class MarsApiStatus { LOADING, ERROR, DONE}

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()
    private val _properties = MutableLiveData<List<MarsProperty>>()


    val status: LiveData<MarsApiStatus>
        get() = _status

    val properties: LiveData<List<MarsProperty>>
    get() = _properties

    // For navigating to the selectedProperty detail screen
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()

    val navigateToSelectedProperty: LiveData<MarsProperty>
    get() = _navigateToSelectedProperty

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {

        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                //var listResult = MarsApi.retrofitService.getProperties()

                _properties.value = MarsApi.retrofitService.getProperties()
                _status.value = MarsApiStatus.DONE
//                }
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }



        // Enqueue the Retrofit request
//        MarsApi.retrofitService.getProperties().enqueue(object : Callback<List<MarsProperty>>
//        {
//            // Override required Retrofit callbacks to assign an error message to the _response LiveData Value
//            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable)
//            {
//                _response.value = "Failure: " + t.message
//            }
//
//            // Override required Retrofit callbacks to assign the JSON response to the _response LiveData Value
//            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>)
//            {
//                _response.value = "Success: ${response.body()?.size} Mars properties retrieved"
//            }
//        })
    }

    fun displayPropertyDetails(marsProperty: MarsProperty)
    {
        _navigateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete()
    {
        _navigateToSelectedProperty.value = null
    }

}
