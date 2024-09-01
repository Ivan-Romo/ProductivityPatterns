package com.example.productivitypatterns.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.productivitypatterns.domain.Personalization
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.util.listQuestions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class PersonalViewModel(private val context: Context) : ViewModel() {
    var info: Personalization = Personalization.default()

    init {
        val file = File(context.filesDir, "user_data.json")
        if (file.exists()) {
            val json = file.readText()

            info = Json.decodeFromString<Personalization>(json)
            info.customQuestions.forEach { question ->
                listQuestions.add(question)
            }
        } else {
            val json = Json.encodeToString(info)
            val file = File(context.filesDir, "user_data.json")
            file.writeText(json)
        }
    }

    fun getListQuestionsAndEnabled(): List<Pair<Question, Boolean>> {
        var list = mutableListOf<Pair<Question, Boolean>>()
        listQuestions.forEach { question ->
            if (!list.contains(Pair(question, info.enabledQuestions[question.id]!!))) {
                list.add(Pair(question, info.enabledQuestions[question.id]!!))
            }
        }
        return list
    }

    fun getListEnabledQuestions(): List<Question>{
        var list = mutableListOf<Question>()
        listQuestions.forEach { question ->
            if(info.enabledQuestions[question.id]!! && !list.contains(question)){
                list.add(question)
            }
        }
        return list
    }

    fun addCustomQuestion(question: Question) {
        if (!info.customQuestions.contains(question) && !listQuestions.contains(question)) {
            info.customQuestions.add(question)
            info.enabledQuestions[question.id] = true
            saveUserPersonalization()
            loadUserPersonalization()
        }
    }

    fun changeEnabled(id: String) {
        if (info.enabledQuestions.contains(id)) {
            info.enabledQuestions[id] = !info.enabledQuestions[id]!!

            saveUserPersonalization()
            loadUserPersonalization()
        }
    }

    fun saveUserPersonalization() {
        val json = Json.encodeToString(info)
        val file = File(context.filesDir, "user_data.json")
        file.writeText(json)
    }

    fun loadUserPersonalization() {
        val file = File(context.filesDir, "user_data.json")
        if (file.exists()) {
            val json = file.readText()

            info = Json.decodeFromString<Personalization>(json)
            info.customQuestions.forEach { question ->
                listQuestions.add(question)
            }
        }
    }

}