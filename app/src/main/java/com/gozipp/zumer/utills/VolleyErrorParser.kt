package com.gozipp.zumer.utills

import com.android.volley.*

object VolleyErrorParser {
    fun getVolleyErrorMessage(error: VolleyError): String {

        if (error is TimeoutError) {
            return "Timeout error occur"
        }
        if (error is NoConnectionError) {
            return "Oops! it's a bad internet day"
        }
        if (error is NetworkError) {
            return "No network connection available"
        }
        if (error is ServerError) {
            return "Server has encounter a problem"
        }
        return "Something went wrong"
    }
}