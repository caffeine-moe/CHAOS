package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.config
import oshi.SystemInfo

class SysFetch : Command(
    arrayOf("sysfetch", "sysinfo", "fetch"),
    CommandInfo("SysFetch", "sysfetch", "Sends your system information (specs).")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val sysInfo = SystemInfo()

        val proc = sysInfo.hardware.processor
        val gpu = sysInfo.hardware.graphicsCards.first()
        val ram = sysInfo.hardware.memory
        val up = sysInfo.operatingSystem.systemUptime
        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("Fetching info...").build()
        ).await().also { message ->
            message.edit(
                MessageBuilder()
                    .appendLine("```")
                    .appendLine("OS: ${sysInfo.operatingSystem.family}")
                    .appendLine("UPTIME: ${up / (60 * 60 * 24)}d ${(up % 86400) / (60 * 60)}h ${(up / 60) % 60}m ${up % 60}s")
                    .appendLine("CPU: ${proc.processorIdentifier.name} (${proc.physicalProcessorCount}C ${proc.logicalProcessorCount}T)")
                    .appendLine("RAM: ${(ram.total / 1073741824) + 1}GB")
                    .appendLine("GPU: ${gpu.name}")
                    .appendLine("HOST: ${sysInfo.hardware.computerSystem.baseboard.model}")
                    .appendLine("OS VER: ${sysInfo.operatingSystem.versionInfo}")
                    .appendLine("```")
                    .build()
            ).await().also {
                onComplete(it, config.auto_delete.bot.content_generation)
            }
        }
    }
}
