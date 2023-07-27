package kore.botssdk.delegate

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface EmployeeAssistDelegateEntryPoint {
    fun getKoreBotDelegate(): EmployeeAssistDelegate?
}