package com.productivity.productivitypatterns.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.productivity.productivitypatterns.domain.Personalization
import com.productivity.productivitypatterns.domain.Session
import com.productivity.productivitypatterns.domain.hashMapToSession
import com.productivity.productivitypatterns.domain.toHashMap
import com.productivity.productivitypatterns.util.listQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalSessionViewModel(private val context: Context): ViewModel() {

    private var _sessionList = MutableStateFlow<MutableList<Session>>(mutableListOf())
    var sessionList = _sessionList.asStateFlow()

    init {
        getSessionList()
    }

    fun getLastSessionType(): String{
        if(_sessionList.value.isEmpty()){
            return ""
        }
        else {
            return _sessionList.value.last().type
        }
    }

    private fun getSessionList() {
        val file = File(context.filesDir, "user_sessions.json")
        if (file.exists()) {
            val json = file.readText()
            _sessionList.value = Json.decodeFromString<MutableList<Session>>(json)
        } else {
            val json = Json.encodeToString(_sessionList.value)
            val file = File(context.filesDir, "user_sessions.json")
            file.writeText(json)
        }
    }

    fun createSession(session: Session) {
        _sessionList.value.add(session)
        saveUserSessions()
        loadUserSessions()
    }

    fun saveUserSessions() {
        val json = Json.encodeToString(_sessionList.value)
        val file = File(context.filesDir, "user_sessions.json")
        file.writeText(json)
    }

    fun loadUserSessions() {
        val file = File(context.filesDir, "user_sessions.json")
        if (file.exists()) {
            val json = file.readText()

            _sessionList.value = Json.decodeFromString<MutableList<Session>>(json)
        }
    }
}