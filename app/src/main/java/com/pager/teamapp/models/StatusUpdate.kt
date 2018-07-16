package com.pager.teamapp.models

data class StatusUpdate(val event: String = EVENT_TYPE, val user: String, val state: String) {

    companion object {
        private const val EVENT_TYPE = "state_change"
    }

}