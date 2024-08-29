package com.example.productivitypatterns.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.productivitypatterns.domain.Session
import com.example.productivitypatterns.domain.hashMapToSession
import com.example.productivitypatterns.domain.toHashMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class SessionViewModel : ViewModel() {

    private var _sessionList = MutableStateFlow<List<Session>>(emptyList())
    var sessionList = _sessionList.asStateFlow()

    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    private val db = Firebase.firestore

    init {
        getSessionList()
    }

    private fun getSessionList() {
        if (userId != null) {
            val userSessionsDocRef = db.collection("sessions").document(userId)

            userSessionsDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val sessions = document.get("sessions") as List<HashMap<String, Any>>


                        // Convertimos la lista de HashMaps a una lista de objetos Session
                        val sessionList = sessions.map { sessionMap ->
                            hashMapToSession(sessionMap)
                        }

                        // Actualizamos el estado de _sessionList
                        _sessionList.value = sessionList

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

        userSessionsDocRef.set(hashMapOf("sessions" to FieldValue.arrayUnion(session.toHashMap())), SetOptions.merge())

    }
}
