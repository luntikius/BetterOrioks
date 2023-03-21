package com.studentapp.betterorioks.data.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studentapp.betterorioks.data.NetworkOrioksRepository
import com.studentapp.betterorioks.data.subjects.SimpleSubjectsDatabase
import com.studentapp.betterorioks.data.subjects.SimpleSubjectsOfflineRepository
import com.studentapp.betterorioks.model.subjectsFromSite.SimpleSubject
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData
import com.studentapp.betterorioks.ui.components.makeStatusNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FindDifferencesWorker(
    context: Context,
    params: WorkerParameters,
): CoroutineWorker(appContext = context, params = params){

    private fun parseAcademicPerformance(subjectsData: SubjectsData):List<SimpleSubject>{
        val subjects = subjectsData.subjects
        val result = mutableListOf<SimpleSubject>()
        subjects.forEach {subject ->
            result.add(
                SimpleSubject(
                name = subject.name,
                systemId = subject.id,
                userScore = subject.grade.fullScore
            )
            )
            val controlForm = subject.formOfControl.name
            subject.getControlEvents().forEach{controlEvent ->
                val finalSubjectShort = if(controlEvent.shortName != "-" && controlEvent.shortName != " "){"(${controlEvent.shortName})"}else{""}
                result.add(
                    SimpleSubject(
                        name = "${controlEvent.type.name.ifBlank{controlForm}} $finalSubjectShort",
                        systemId = subject.id,
                        userScore = controlEvent.grade.score,
                        isSubject = false
                    )
                )
            }
        }
        return result
    }

    private suspend fun calculateSubjectsDifferences(simpleSubjects: List<SimpleSubject>, simpleSubjectsOfflineRepository: SimpleSubjectsOfflineRepository):Map<SimpleSubject,List<SimpleSubject>>{
        val savedSubjects = simpleSubjectsOfflineRepository.getSubjects().first()
        val subjects = simpleSubjects.filter { it.isSubject }
        val difference = mutableListOf<SimpleSubject>()
        if(subjects.size == savedSubjects.size){
            for(i in subjects.indices){
                if(subjects[i].userScore != savedSubjects[i].userScore){
                    difference.add(
                        SimpleSubject(
                            id = subjects[i].id,
                            name = subjects[i].name,
                            systemId = subjects[i].systemId,
                            userScore = "c ${savedSubjects[i].userScore} на ${subjects[i].userScore}",
                            isSubject = subjects[i].isSubject
                        )
                    )
                }
            }
        }
        val diffMap = mutableMapOf<SimpleSubject,List<SimpleSubject>>()
        difference.forEach{subject ->
            val savedControlEvents = simpleSubjectsOfflineRepository.getControlEvents(subject.systemId).first()
            val controlEvents = simpleSubjects.filter {it.systemId == subject.systemId && !it.isSubject}
            val tempDiff = mutableListOf<SimpleSubject>()
            if(controlEvents.size == savedControlEvents.size) {
                for (i in controlEvents.indices) {
                    println("${controlEvents[i].userScore} -> ${savedControlEvents[i].userScore}")
                    if (controlEvents[i].userScore != savedControlEvents[i].userScore) {
                        println("AA")
                        tempDiff.add(
                            SimpleSubject(
                                id = controlEvents[i].id,
                                name = controlEvents[i].name,
                                systemId = controlEvents[i].systemId,
                                userScore = "c ${savedControlEvents[i].userScore} на ${controlEvents[i].userScore}",
                                isSubject = controlEvents[i].isSubject
                            )
                        )
                    }
                }
            }
            diffMap[subject] = tempDiff
        }
        return diffMap
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {

                val cookies = inputData.getString("COOKIES")

                require(!cookies.isNullOrBlank()){
                    val errorMessage = "invalid cookies"
                    Log.e("FIND_DIFFERENCE_WORKER", "invalid cookies")
                    errorMessage
                }
                Log.d("FIND_DIFFERENCE_WORKER","run")
                val orioksRepository = NetworkOrioksRepository()
                val simpleSubjectsOfflineRepository = SimpleSubjectsOfflineRepository(
                    SimpleSubjectsDatabase.getDatabase(context = applicationContext).itemDao())
                val newData = orioksRepository.getSubjects(cookies = cookies)
                val simplifiedNewData = parseAcademicPerformance(newData)
                val differenceMap = calculateSubjectsDifferences(
                    simpleSubjects = simplifiedNewData,
                    simpleSubjectsOfflineRepository = simpleSubjectsOfflineRepository
                )
                differenceMap.forEach {
                    val head = it.key.name
                    it.value.forEach {simpleSubject ->
                        makeStatusNotification(
                            message = "Балл за ${simpleSubject.name} изменен: ${simpleSubject.userScore}",
                            head = head,
                            context = applicationContext)
                    }
                }
                simpleSubjectsOfflineRepository.insertItems(simplifiedNewData)
                Result.success()
            } catch (throwable: Throwable) {
                Log.e(
                    "FIND_DIFFERENCE_WORKER",
                    "error finding differences",
                    throwable
                )
                Result.failure()
            }
        }
    }

}