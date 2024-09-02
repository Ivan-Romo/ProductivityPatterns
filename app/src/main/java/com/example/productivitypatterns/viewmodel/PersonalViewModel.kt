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

    fun deleteCustomQuestion(id: String) {
        var question = listQuestions.find { quest -> quest.id == id }
        if(question != null) {
            listQuestions.remove(question)
            info.customQuestions.remove(question)
            if(info.customQuestions.contains(question)) {
                info.customAnswers.remove(question.id)
            }
            info.enabledQuestions.remove(question.id)

            saveUserPersonalization()
            loadUserPersonalization()
        }
    }

    fun getListQuestionsAndEnabled(): List<Pair<Question, Boolean>> {
        var list = mutableListOf<Pair<Question, Boolean>>()
        listQuestions.forEach { question ->
            if(info.enabledQuestions.contains(question.id)) {
                if (!list.contains(Pair(question, info.enabledQuestions[question.id]))) {
                    list.add(Pair(question, info.enabledQuestions[question.id]!!))
                }
            }
        }
        return list
    }

    fun getListEnabledQuestions(): List<Question> {
        var list = mutableListOf<Question>()
        listQuestions.forEach { question ->
            if (info.enabledQuestions[question.id]!! && !list.contains(question)) {
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

    fun optionIsCustom(idQuestion: String, option: String): Boolean {
        if (!info.customAnswers.containsKey(idQuestion)) {
            return false
        } else if (info.customAnswers[idQuestion]!!.contains(option)) {
            return true
        } else {
            return false
        }
    }

    fun deleteCustomOption(idQuestion: String, option: String) {
        info.customAnswers[idQuestion]!!.remove(option)
        saveUserPersonalization()
        loadUserPersonalization()
    }

    fun addOptionToQuestion(idQuestion: String, newOption: String) {

        if (!info.customAnswers.containsKey(idQuestion)) {
            info.customAnswers[idQuestion] = mutableListOf(newOption)
        } else {
            info.customAnswers[idQuestion]!!.add(newOption)
        }

        saveUserPersonalization()
        loadUserPersonalization()
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

    fun resetData(){
        val json = Json.encodeToString(Personalization.default())
        val file = File(context.filesDir, "user_data.json")
        file.writeText(json)

        loadUserPersonalization()
    }

    fun addActivityType(type:String){
        if(!info.activityTypes.contains(type)) {
            info.activityTypes.add(type)
            saveUserPersonalization()
            loadUserPersonalization()
        }
    }

}