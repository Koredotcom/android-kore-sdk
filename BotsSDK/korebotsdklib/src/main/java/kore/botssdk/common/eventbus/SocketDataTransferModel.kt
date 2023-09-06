package kore.botssdk.common.eventbus

import kore.botssdk.common.manager.BaseSocketConnectionManager
import kore.botssdk.models.BotRequest

/**
 * Created by Kishore on 01-Sep-23.
 */
class SocketDataTransferModel(
    var event_type: BaseSocketConnectionManager.EVENT_TYPE,
    var payLoad: String,
    var botRequest: BotRequest?,
    isFromSkillSwitch: Boolean
) {
    fun setFromUtterance(isFromSkillSwitch: Boolean) {
        this.isFromSkillSwitch = isFromSkillSwitch
    }

    var isFromSkillSwitch = false
        private set

    init {
        this.isFromSkillSwitch = isFromSkillSwitch
    }
}