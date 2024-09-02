package com.example.productivitypatterns.viewmodel

import androidx.lifecycle.ViewModel
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.domain.Session
import com.example.productivitypatterns.util.formatToDayMonth
import com.example.productivitypatterns.util.listQuestions
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class StatsViewModel(sessionViewModel: SessionViewModel) : ViewModel() {
    var sessionData = sessionViewModel.sessionList.value

    init {
        if (sessionData.isEmpty()) {
            sessionData = listOf<Session>(
                Session(
                    duration = 0,
                    responses = mapOf(Pair(UUID.randomUUID().toString(), "")),
                    type = ""
                )
            )
        }
    }

    val now = LocalDateTime.now()
    val sessionsLast7Days = sessionData
        .filter { session ->
            val daysBetween = ChronoUnit.DAYS.between(session.datetime.toLocalDate(), now.toLocalDate())
            daysBetween <= 7
        }
        .sortedBy { session -> session.datetime }
    val lastSession = sessionData.last()

    fun getAverageProductivityInTheLast7Days(): Int {
        var total = 0.0
        sessionsLast7Days.forEach { session ->
            total += session.responses["prod"]?.toInt() ?: 0
        }
        return (total / sessionsLast7Days.size * 10).toInt()
    }

    fun getProductivityOfEachSessionInTheLast7Days(): Pair<List<String>, List<Int>> {
        var list: MutableList<Int> = mutableListOf()
        var categoriesList: MutableList<String> = mutableListOf()
        sessionsLast7Days.forEach { session ->
            list.add(session.responses["prod"]!!.toInt() * 10)
            categoriesList.add(session.datetime.formatToDayMonth())
        }
        return Pair<List<String>, List<Int>>(categoriesList, list)
    }

    fun getProductivityInTheLastSession(): Int {
        return lastSession.responses["prod"]!!.toInt() * 10
    }

    fun getMusicProductivityInTheLastSession(): Map<String, Int> {
        var map: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

        sessionsLast7Days.forEach { session ->
            if (session.responses.containsKey("music")) {
                val key = session.responses["music"]!!
                val value = session.responses["prod"]!!.toInt() * 10

                if (map.containsKey(key)) {
                    val (currentSum, count) = map[key]!!
                    map[key] = Pair(currentSum + value, count + 1)
                } else {

                    map[key] = Pair(value, 1)
                }
            }
        }
        val averagedMap: MutableMap<String, Int> = mutableMapOf()
        map.forEach { (key, value) ->
            val (sum, count) = value
            averagedMap[key] = sum / count
        }
        return averagedMap
    }

    fun getStressAndSleepOfEachSessionInTheLast7Days(): Pair<List<String>, List<List<Int>>> {
        var productivityList: MutableList<Int> = mutableListOf()
        var sleepList: MutableList<Int> = mutableListOf()
        var stressList: MutableList<Int> = mutableListOf()

        var categoriesList: MutableList<String> = mutableListOf()
        sessionsLast7Days.forEach { session ->
            productivityList.add(session.responses["prod"]!!.toInt() * 10)
            categoriesList.add(session.datetime.formatToDayMonth())

            if(session.responses.size >= 6) {
                sleepList.add(session.responses["sleep"]!!.toInt() *10)
                stressList.add(session.responses["stress"]!!.toInt() *10)
            }
        }

        var list: List<List<Int>> = listOf(productivityList, sleepList, stressList)
        return Pair<List<String>, List<List<Int>>>(categoriesList, list)
    }


    fun getCaffeineAndTrainingOfEachSessionInTheLast7Days(): List<Int>{
        var productivityWhenTrained = 0
        var countWhenTrained = 0
        var productivityWhenNotTrained = 0
        var countWhenNotTrained = 0
        var productivityWhenCaffeine = 0
        var countWhenCaffeine = 0
        var productivityWhenNoCaffeine = 0
        var countWhenNoCaffeine = 0

        sessionsLast7Days.forEach { session ->
            if (session.responses.size >= 6) {
                val productivity = session.responses["prod"]!!.toInt() * 10

                if (session.responses["train"] == "no") {
                    productivityWhenNotTrained += productivity
                    countWhenNotTrained++
                } else {
                    productivityWhenTrained += productivity
                    countWhenTrained++
                }

                if (session.responses["cafe"] == "no") {
                    productivityWhenNoCaffeine += productivity
                    countWhenNoCaffeine++
                } else {
                    productivityWhenCaffeine += productivity
                    countWhenCaffeine++
                }
            }
        }

        val averageWhenTrained = if (countWhenTrained > 0) productivityWhenTrained / countWhenTrained else 0
        val averageWhenNotTrained = if (countWhenNotTrained > 0) productivityWhenNotTrained / countWhenNotTrained else 0
        val averageWhenCaffeine = if (countWhenCaffeine > 0) productivityWhenCaffeine / countWhenCaffeine else 0
        val averageWhenNoCaffeine = if (countWhenNoCaffeine > 0) productivityWhenNoCaffeine / countWhenNoCaffeine else 0

        return listOf(averageWhenTrained, averageWhenNotTrained, averageWhenCaffeine, averageWhenNoCaffeine)
    }

}