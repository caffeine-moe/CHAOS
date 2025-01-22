package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen
import oshi.SystemInfo

class SysFetch : Command(
    arrayOf("sysfetch", "sysinfo", "fetch"),
    CommandInfo("SysFetch", "sysfetch", "Sends your system information (specs).")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val sysInfo = SystemInfo()
        val proc = sysInfo.hardware.processor
        val gpu = sysInfo.hardware.graphicsCards.first()
        val ram = sysInfo.hardware.memory
        val up = sysInfo.operatingSystem.systemUptime
        val speed = (proc.maxFreq / 1000000000).takeIf { it > 0 } ?: proc.maxFreq.takeIf { it == -1L } ?: "Unknown "
        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("Fetching info...")
        ).awaitThen { message ->
            message.edit(
                MessageBuilder()
                    .appendLine("```asciidoc")
                    .appendLine("•Platform:\n----------")
                    .appendLine("    •Family:: ${sysInfo.operatingSystem.family}")
                    .appendLine("    •Version:: ${sysInfo.operatingSystem.versionInfo}")
                    .appendLine("    •Manufacturer:: ${sysInfo.operatingSystem.manufacturer}")
                    .appendLine("    •Uptime:: ${up / (60 * 60 * 24)}d ${(up % 86400) / (60 * 60)}h ${(up / 60) % 60}m ${up % 60}s")
                    .appendLine("•CPU:\n-----")
                    .appendLine("    •Name:: ${proc.processorIdentifier.name}")
                    .appendLine("    •Family:: ${proc.processorIdentifier.microarchitecture}")
                    .appendLine("    •Vendor:: ${proc.processorIdentifier.vendor}")
                    .appendLine("    •Cores:: ${proc.physicalProcessorCount}")
                    .appendLine("    •Threads:: ${proc.logicalProcessorCount}")
                    .appendLine("    •Speed:: ${speed}GHz")
                    .appendLine("•RAM:\n-----")
                    .appendLine("    •Total Memory:: ${(ram.total / 1073741824) + 1}GB")
                    .appendLine("    •Free Memory:: ${(ram.available / 1073741824) + 1}GB")
                    .appendLine("    •Used Memory:: ${((ram.total - ram.available) / 1073741824) + 1}GB")
                    .appendLine("    •Virtual Memory:: ${((ram.virtualMemory.virtualMax) / 1073741824) + 1}GB")
                    .appendLine("•GPU:\n-----")
                    .appendLine("    •Vendor:: ${gpu.vendor}")
                    .appendLine("    •Model:: ${gpu.name}")
                    .appendLine("•Motherboard:\n-------------")
                    .appendLine("    •Model:: ${sysInfo.hardware.computerSystem.baseboard.model}")
                    .appendLine("    •Manufacturer:: ${sysInfo.hardware.computerSystem.baseboard.manufacturer}")
                    .appendLine("```")
            ).awaitThen {
                onComplete(it, true)
            }
        }
    }
}