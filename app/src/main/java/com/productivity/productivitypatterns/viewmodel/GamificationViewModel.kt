package com.productivity.productivitypatterns.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.productivity.productivitypatterns.domain.Gamification
import com.productivity.productivitypatterns.domain.Session
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class GamificationViewModel(private val context: Context, val localSessionViewModel: LocalSessionViewModel):  ViewModel() {
    private var gamification: Gamification = Gamification();

    init {
        getPlayerInformation();
    }


    private fun getPlayerInformation() {
        val file = File(context.filesDir, "user_gamification.json")
        if (file.exists()) {
            val json = file.readText()
            gamification = Json.decodeFromString<Gamification>(json)
        } else {
            val json = Json.encodeToString(gamification)
            val file = File(context.filesDir, "user_gamification.json")
            file.writeText(json)
        }
    }

    fun getPoints(): Int{
        return gamification.points
    }
    fun addPoints(points: Int){
        gamification.points+=points
        saveUserGamification()
        loadUserGamification()
    }

    fun checkAchievements(): Unit{
        if(localSessionViewModel.sessionCount()>0){
            gamification.toggleChallengeStatus("Add one session")
        }


        saveUserGamification()
        loadUserGamification()
    }

    fun getAchievements():  Map<String, Pair<Boolean, String>>{
        return gamification.challenge
    }

    fun saveUserGamification() {
        val json = Json.encodeToString(gamification)
        val file = File(context.filesDir, "user_gamification.json")
        file.writeText(json)
    }

    fun loadUserGamification() {
        val file = File(context.filesDir, "user_gamification.json")
        if (file.exists()) {
            val json = file.readText()
            gamification= Json.decodeFromString<Gamification>(json)
        }
    }
}