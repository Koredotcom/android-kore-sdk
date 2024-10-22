package com.kore.speech

import com.kore.botclient.BuildConfig

/**
 * Android Speech library logger.
 * You can provide your own logger delegate implementation, to be able to log in a different way.
 * By default the log level is set to DEBUG when the build type is debug, and OFF in release.
 * The default logger implementation logs in Android's LogCat.
 * @author gotev (Aleksandar Gotev)
 */
class Logger internal constructor() {
    enum class LogLevel {
        DEBUG,
        INFO,
        ERROR,
        OFF
    }

    interface LoggerDelegate {
        fun error(tag: String, message: String)
        fun error(tag: String, message: String, exception: Throwable)
        fun debug(tag: String, message: String)
        fun info(tag: String, message: String)
    }

    private var mLogLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.OFF
    private var mDelegate: LoggerDelegate = com.kore.speech.DefaultLoggerDelegate()

    private object SingletonHolder {
        val instance = Logger()
    }

    companion object {
        fun resetLoggerDelegate() {
            synchronized(Logger::class.java) {
                SingletonHolder.instance.mDelegate = com.kore.speech.DefaultLoggerDelegate()
            }
        }

        fun setLoggerDelegate(delegate: LoggerDelegate?) {
            requireNotNull(delegate) { "delegate MUST not be null!" }
            synchronized(Logger::class.java) { SingletonHolder.instance.mDelegate = delegate }
        }

        fun setLogLevel(level: LogLevel) {
            synchronized(Logger::class.java) { SingletonHolder.instance.mLogLevel = level }
        }

        fun error(tag: String, message: String) {
            if (SingletonHolder.instance.mLogLevel <= LogLevel.ERROR) {
                SingletonHolder.instance.mDelegate.error(tag, message)
            }
        }

        fun error(tag: String, message: String, exception: Throwable) {
            if (SingletonHolder.instance.mLogLevel <= LogLevel.ERROR) {
                SingletonHolder.instance.mDelegate.error(tag, message, exception)
            }
        }

        fun info(tag: String, message: String) {
            if (SingletonHolder.instance.mLogLevel <= LogLevel.INFO) {
                SingletonHolder.instance.mDelegate.info(tag, message)
            }
        }

        fun debug(tag: String, message: String) {
            if (SingletonHolder.instance.mLogLevel <= LogLevel.DEBUG) {
                SingletonHolder.instance.mDelegate.debug(tag, message)
            }
        }
    }
}
