package com.productivity.productivitypatterns.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.productivity.productivitypatterns.domain.Session
import com.productivity.productivitypatterns.domain.hashMapToSession
import com.productivity.productivitypatterns.domain.toHashMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.HashMap

class SessionViewModel() : ViewModel() {

    private var _sessionList = MutableStateFlow<MutableList<Session>>(mutableListOf())
    var sessionList = _sessionList.asStateFlow()

    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    private val db = Firebase.firestore

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
        if (userId != null) {
            val userSessionsDocRef = db.collection("sessions").document(userId)

            userSessionsDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val sessions = document.get("sessions") as List<HashMap<String, Any>>

                        val sessionList = sessions.map { sessionMap ->
                            hashMapToSession(sessionMap)
                        }
                        _sessionList.value = sessionList.toMutableList()

                    } else {
                        Log.d("getSessionList", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("getSessionList", "Error getting document: ", exception)
                }
        }
    }

    fun createSession(session: Session) {
        if (userId == null) {
            Log.d("createCar", "User not authenticated")
            return
        }

        val db = FirebaseFirestore.getInstance()
        val userSessionsDocRef = db.collection("sessions").document(userId)

        _sessionList.value.add(session)

        userSessionsDocRef.set(hashMapOf("sessions" to FieldValue.arrayUnion(session.toHashMap())), SetOptions.merge())
    }
}
