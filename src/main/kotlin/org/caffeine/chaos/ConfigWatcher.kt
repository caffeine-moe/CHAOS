package org.caffeine.chaos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

//checks if anything happens to the config file, if something does, then reload
suspend fun configWatcher(client : Client) {
    //create a watch service
    val watchService = withContext(Dispatchers.IO) {
        FileSystems.getDefault().newWatchService()
    }
    //create a watch key for the root directory
    val path = Paths.get("").toAbsolutePath()
    val watchKey = withContext(Dispatchers.IO) {
        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
    }
    //loop forever
    while (true) {
        //wait for an event
        for (event in watchKey.pollEvents()) {
            //if the event is a modification to the config file
            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && event.context().toString()
                    .contains("config.json")
            ) {
                //reload the config
                client.logout()
                main()
            }
        }
        val valid = watchKey.reset()
        if (!valid) {
            break
        }
    }
}