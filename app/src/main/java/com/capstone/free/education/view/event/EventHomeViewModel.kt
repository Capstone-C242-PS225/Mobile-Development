package com.capstone.free.education.view.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.free.education.data.event.retrofit.ApiConfig
import com.capstone.free.education.data.response.EventResponse
import com.capstone.free.education.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventHomeViewModel : ViewModel() {

    private val _events = MutableLiveData<EventResponse>()
    val events: LiveData<EventResponse> = _events

    private val _listUpcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val listUpcomingEvents: LiveData<List<ListEventsItem>> = _listUpcomingEvents

    private val _listFinishedEvents = MutableLiveData<List<ListEventsItem>>()
    val listFinishedEvents: LiveData<List<ListEventsItem>> = _listFinishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        val upcomingEventClient = ApiConfig.getApiService().getEvents(1)
        val finishedEventClient = ApiConfig.getApiService().getEvents(0)

        upcomingEventClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _events.value = responseBody
                        _listUpcomingEvents.value = responseBody.listEvents
                    } else {
                        _errorMessage.value = "No events found"
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed to load events: ${t.message}"
            }
        })

        finishedEventClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _events.value = responseBody
                        _listFinishedEvents.value = responseBody.listEvents
                    } else {
                        _errorMessage.value = "No finished events found"
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed to load finished events: ${t.message}"
            }
        })
    }
}