package com.productivity.productivitypatterns.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.productivity.productivitypatterns.domain.Session
import com.productivity.productivitypatterns.util.formatToDayMonth
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class StatsViewModel(private var _data: MutableList<Session>) : ViewModel() {//Yo creo que pasar solo la lista es mejor
    var sessionData = mutableStateOf(_data).value

    init {
        if (sessionData.isEmpty()) {
            sessionData = mutableListOf<Session>(
                Session(
                    duration = 0,
                    responses = mapOf(Pair("prod", "0")),
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

    fun getAverageProductivityInTheLast7Days(type: String): Int {
        var total = 0.0
        var count = 0
        sessionsLast7Days.forEach { session ->
            if (session.type == type) {
                total += session.responses["prod"]?.toInt() ?: 0
                count++
            }
        }
        return (total / count * 10).toInt()
    }

    fun getProductivityOfEachSessionInTheLast7Days(type: String): Pair<List<String>, List<Int>> {
        var list: MutableList<Int> = mutableListOf()
        var categoriesList: MutableList<String> = mutableListOf()
        sessionsLast7Days.forEach { session ->
            if (session.type == type) {
                list.add(session.responses["prod"]!!.toInt() * 10)
                categoriesList.add(session.datetime.formatToDayMonth())
            }
        }
        return Pair<List<String>, List<Int>>(categoriesList, list)
    }

    fun getProductivityInTheLastSession(type: String): Int {
        var lastSession = sessionData.findLast { sesssion -> sesssion.type == type }
        if (lastSession == null) {
            return 0
        }else {
            return lastSession.responses["prod"]!!.toInt() * 10
        }
    }


    fun getYesNoStats(idQuestion: String, type: String): Pair<Int, Int> {
        var yesProductivity = 0;
        var yesCount = 0
        var noProductivity = 0;
        var noCount = 0

        sessionsLast7Days.forEach { session ->
            if (session.type == type) {
                val productivity = session.responses["prod"]!!.toInt() * 10
                if (session.responses[idQuestion] == "no") {
                    noProductivity += productivity
                    noCount++
                } else {
                    yesProductivity += productivity
                    yesCount++
                }
            }
        }

        val averageWhenYes = if (yesCount > 0) yesProductivity / yesCount else 0
        val averageWhenNo = if (noCount > 0) noProductivity / noCount else 0
        return Pair(averageWhenYes, averageWhenNo)
    }

    fun getRatingStats(idQuestion: String, type: String): Pair<List<String>, Pair<List<Int>, List<Int>>> {
        val productivityList: MutableList<Int> = mutableListOf()
        val ratingList: MutableList<Int> = mutableListOf()
        val categoriesList: MutableList<String> = mutableListOf()
        sessionsLast7Days.forEach { session ->
            if (session.type == type) {
                productivityList.add(session.responses["prod"]!!.toInt() * 10)
                categoriesList.add(session.datetime.formatToDayMonth())

                if (session.responses.containsKey(idQuestion)) {
                    ratingList.add(session.responses[idQuestion]!!.toInt() * 10)
                }
            }
        }

        val list: Pair<List<Int>, List<Int>> = Pair(productivityList, ratingList)
        return Pair<List<String>, Pair<List<Int>, List<Int>>>(categoriesList, list)
    }

    fun getMultipleStats(idQuestion: String, type: String): Map<String, Int> {
        var map: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

        sessionsLast7Days.forEach { session ->
            if (session.type == type) {
                if (session.responses.containsKey(idQuestion)) {
                    val key = session.responses[idQuestion]!!
                    val value = session.responses["prod"]!!.toInt() * 10
                    if (map.containsKey(key)) {
                        val (currentSum, count) = map[key]!!
                        map[key] = Pair(currentSum + value, count + 1)
                    } else {

                        map[key] = Pair(value, 1)
                    }
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


}