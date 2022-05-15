package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import oshi.SystemInfo

class SysFetch : Command(arrayOf("sysfetch", "sysinfo", "fetch")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        val sysinfo = SystemInfo()
        val proc = sysinfo.hardware.processor
        val gpu = sysinfo.hardware.graphicsCards.first()
        val ram = sysinfo.hardware.memory
        val up = sysinfo.operatingSystem.systemUptime
        event.channel.sendMessage(MessageBuilder()
            .appendLine("Fetching info...").build()).thenAccept { message ->
            this.launch {
                message.edit(MessageBuilder()
                    .appendLine("```")
                    .appendLine("OS: ${sysinfo.operatingSystem.family}")
                    .appendLine("UPTIME: ${up / (60 * 60 * 24)}d ${(up % 86400) / (60 * 60)}h ${(up / 60) % 60}m ${up % 60}s")
                    .appendLine("CPU: ${proc.processorIdentifier.name} (${proc.physicalProcessorCount}C ${proc.logicalProcessorCount}T)")
                    .appendLine("RAM: ${ram.total / (1073741824)}GB")
                    .appendLine("GPU: ${gpu.name}")
                    .appendLine("HOST: ${sysinfo.hardware.computerSystem.baseboard.model}")
                    .appendLine("OS VER: ${sysinfo.operatingSystem.versionInfo}")
                    .appendLine("```")
                    .build()
                ).thenAccept {
                    this.launch { onComplete(it, client, client.config.auto_delete.bot.content_generation) }
                }
            }
        }
    }
}