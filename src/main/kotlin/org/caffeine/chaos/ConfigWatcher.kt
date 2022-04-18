package org.caffeine.chaos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

suspend fun configWatcher(client: Client) {
    try {
        val watchService = withContext(Dispatchers.IO) {
            FileSystems.getDefault().newWatchService()
        }
        val path = Paths.get("").toAbsolutePath()
        val watchKey = withContext(Dispatchers.IO) {
            path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE
            )
        }
        while (true) {
            for (event in watchKey.pollEvents()) {
                val kind = event.kind()
                if (kind == StandardWatchEventKinds.ENTRY_DELETE && event.context().toString()
                        .contains("config.json")
                ) {
                    client.logout()
                    main()
                }
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY && event.context().toString()
                        .contains("config.json")
                ) {
                    client.logout()
                    main()
                }
            }
            val valid = watchKey.reset()
            if (!valid) {
                break
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
