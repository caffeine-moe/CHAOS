package org.caffeine.chaos.handlers

import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.configFile
import org.caffeine.chaos.processes.checkUpdates
import org.caffeine.chaos.versionString
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
                exitProcess(0)
            }

            "-v", "--version" -> {
                log(versionString)
                exitProcess(0)
            }

            "-c", "--config" -> {
                if (args.size > 1 && File(args[1]).exists()) {
                    log("Loading config file ${args[1]}")
                    configFile = File(args[1])
                } else {
                    log("No or Invalid config file path specified.")
                    exitProcess(1)
                }
            }

            "-u", "--update" -> {
                checkUpdates()
                exitProcess(0)
            }

            else -> {
                log("Unknown argument: ${args.first()}")
                exitProcess(1)
            }
        }
    }
}