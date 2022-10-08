package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.log
import java.nio.file.*

//checks if anything happens to the config file, if something does, then reload
suspend fun configWatcher(client : Client) = coroutineScope{
    //create a watch service
    launch {
        val watchService = FileSystems.getDefault().newWatchService()
        //create a watch key for the root directory
        val path = Paths.get("").toAbsolutePath()
        val watchKey = path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_MODIFY
        )

        var valid = watchKey.reset()

        //loop forever
        while (valid) {
            //wait for an event
            for (event in watchKey.pollEvents()) {
                //if the event is a modification to the config file
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && event.context().toString()
                        .contains("config.json")
                ) {
                    //reload CHAOS
                    log("Change in config detected, reloading.", "CONFIG:")
                    client.logout()
                    loadConfig()
                    client.login(config.token)
                }
            }
            valid = watchKey.reset()
        }
    }
}