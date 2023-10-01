package org.caffeine.chaos.processes

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.config
import org.caffeine.octane.client.Client
import org.caffeine.octane.utils.log
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

// checks if anything happens to the config file, if something does, then reload
suspend fun configWatcher(client : Client) = coroutineScope {
    launch {
        val watchService = FileSystems.getDefault().newWatchService()
        val path = Paths.get("").toAbsolutePath()
        val watchKey = path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
        var valid = watchKey.reset()
        while (valid) {
            for (event in watchKey.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && event.context().toString()
                        .contains("config.json")
                ) {
                    log("Change in config detected, reloading.", "CONFIG:")
                    loadConfig()
                    client.configuration.token = config.token
                    client.restart()
                }
            }
            valid = watchKey.reset()
        }
    }
}