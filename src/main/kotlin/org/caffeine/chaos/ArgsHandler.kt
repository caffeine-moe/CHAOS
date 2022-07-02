package org.caffeine.chaos

import org.caffeine.chaos.api.utils.log
import java.io.File
import kotlin.system.exitProcess

suspend fun handleArgs(args : Array<String>) {
    if (args.isNotEmpty()) {
        when (args.first()) {
            "-h", "--help" -> {
                log("CHAOS v$versionString")
                log("  -h, --help: Show this help message and exit")
                log("  -v, --version: Show version information and exit")
                log("  -c, --config: Load a config file")
                log("  -u, --update: Check for updates")
                exitProcess(69)
            }
            "-v", "--version" -> {
                log("CHAOS v$versionString")
                exitProcess(69)
            }
            "-c", "--config" -> {
                if (args.size > 1 && File(args[1]).exists()) {
                    log("Loading config file ${args[1]}")
                    configFile = File(args[1])
                } else {
                    log("No config file specified.")
                    exitProcess(69)
                }
            }
            "-u", "--update" -> {
                checkUpdates()
                exitProcess(69)
            }
            else -> {
                log("Unknown argument: ${args.first()}")
                exitProcess(69)
            }
        }
    }
}