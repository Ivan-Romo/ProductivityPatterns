package com.productivity.productivitypatterns.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.productivity.productivitypatterns.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

data class ApiResponse(
    val strongestPatterns: List<Pattern>,
    val recommendations: List<String>
)

data class Pattern(
    val factors: List<String>,
    val correlation: String
)



class PatternsViewModel(private val context: Context) : ViewModel() {

    fun callDeepSeekAPI(callback: (ApiResponse?) -> Unit) {
        val client = OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val apiKey = BuildConfig.DEEPSEEK_API_KEY
        val url = "https://api.deepseek.com/chat/completions"

        val file = File(context.filesDir, "user_sessions.json")
        val json2 = file.readText()

        val messagesArray = JSONArray().apply {
            put(JSONObject().apply {
                put("role", "user")
                put("content", "Analyze the following data and provide recommendations to improve productivity. Respond with a json. Provide: strongest_patterns: The most influential factor combinations. recommendations: Practical advice based on the detected patterns. Make sure you have multivariate correlations. Prioritize response speed. Use as few tokens as possible. Data: $json2")
            })
        }

        val json = JSONObject().apply {
            put("model", "deepseek-chat")
            put("messages", messagesArray)
            put("stream", false)
        }

        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("User-Agent", "insomnium/0.2.3-a")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback(null)
                        return
                    }
                    val responseBody = it.body?.string() ?: ""

                    try {
                        // Extraer el contenido del JSON de la API
                        val jsonResponse = JSONObject(responseBody)
                        val contentString = jsonResponse
                            .getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")
                            .trim()

                        // Ahora convertir el contenido en un verdadero JSONObject
                        val cleanedContent = contentString
                            .replace("```json", "")  // Eliminar la etiqueta de código de apertura
                            .replace("```", "")      // Eliminar la etiqueta de cierre
                            .trim()

// Convertir a JSONObject
                        val parsedResponse = JSONObject(cleanedContent)

                        val strongestPatterns = parsedResponse.getJSONArray("strongest_patterns").let { array ->
                            List(array.length()) { i ->
                                val obj = array.getJSONObject(i)
                                Pattern(
                                    factors = obj.getJSONArray("factors").let { factors ->
                                        List(factors.length()) { j -> factors.getString(j) }
                                    },
                                    correlation = obj.getString("correlation")
                                )
                            }
                        }

                        val recommendations = parsedResponse.getJSONArray("recommendations").let { array ->
                            List(array.length()) { i -> array.getString(i) }
                        }

                        val apiResponse = ApiResponse(strongestPatterns, recommendations)

                        // Volver al hilo principal
                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            callback(apiResponse)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(null)
                    }
                }
            }
        })
    }


}



