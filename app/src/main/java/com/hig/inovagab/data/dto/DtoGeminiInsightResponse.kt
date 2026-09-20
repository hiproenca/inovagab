package com.hig.inovagab.data.dto

import com.squareup.moshi.Json

data class DtoGeminiInsightResponse(@Json(name = "aiInsight") val insight: String)
