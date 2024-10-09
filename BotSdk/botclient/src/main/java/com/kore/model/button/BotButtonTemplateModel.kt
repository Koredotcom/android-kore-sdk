package com.kore.model.button

import com.kore.model.ImageModel

class BotButtonTemplateModel
{
    var type : String? = ""
    var url : String? = ""
    var title : String? = ""
    var payload : String? = ""
    var name : String? = ""
    var btnType : String? = ""
    var label : String? = ""
    var is_actionable : Boolean? = false
    var botButtonPostBackModel : BotButtonPostBackModel? = null
    var customdate : HashMap<String, Any?>? = null
    var isSamePageNavigation : Boolean? = false
    var action : String? = ""
    var image : ImageModel? = null
}