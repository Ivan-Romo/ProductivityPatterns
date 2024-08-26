package com.example.productivitypatterns.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.productivitypatterns.domain.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionViewModel : ViewModel() {

    private var _sessionList = MutableStateFlow<List<Session>>(emptyList())
    var sessionList = _sessionList.asStateFlow()

    private val db = Firebase.firestore

    init {
        getCarList()
    }

    private fun getCarList() {

        db.collection("cars")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (value != null) {
                    _sessionList.value = value.toObjects()
                }
            }
    }

    fun createCar() {
        val car = hashMapOf(
            "id" to 4,
            "brand" to "OLA"
        )

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.d("createCar", "User not authenticated")
            return
        }

        val db = FirebaseFirestore.getInstance()
        val userCarDocRef = db.collection("cars").document(userId)

        // Aquí usamos set con merge para asegurarnos de que el documento se crea si no existe
        userCarDocRef.set(hashMapOf("cars" to FieldValue.arrayUnion(car)), SetOptions.merge())
            .addOnSuccessListener {
                Log.d("createCar", "Car successfully added!")
            }
            .addOnFailureListener { e ->
                Log.w("createCar", "Error adding car", e)
            }

    }

    fun updateCar() {
        val car = hashMapOf(
            "id" to 4,
            "brand" to "Mazda"
        )
        db.collection("cars")
            .document("uQ9C9PzGXxw1ivXm0zZ4")
            .set(car)
            .addOnSuccessListener {
                Log.d("document", "UPDATED")
            }
    }
}
