package com.tutorial.hng9_stage3_task.utils

import com.tutorial.hng9_stage3_task.models.main.CountriesItem

interface RegisterClicks {
    fun onChildClicked(data:CountriesItem)
    fun onFilterClicked()
    fun onResetClicked()
}