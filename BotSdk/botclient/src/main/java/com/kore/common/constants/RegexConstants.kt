package com.kore.common.constants

class RegexConstants {
    companion object {
        const val REGEX_PHONE_NUMBER = "((\\+|)[0-9]{9,13})|((((\\+|)[0-9]{1,3})-|)((\\(|)[0-9]{3}(\\)|))-([0-9]{3})-([0-9]{4}))"
    }
}