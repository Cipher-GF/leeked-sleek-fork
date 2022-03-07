package today.sleek.base.config

import today.sleek.client.gui.notification.Notification
import java.nio.file.Paths
import today.sleek.client.gui.notification.NotificationManager
import java.io.File
import java.lang.Exception
import java.nio.file.Files

class Config {

    var name: String
    var author: String? = null
    var lastUpdated: String? = null
    lateinit var file: File
    var isOnline = false

    constructor(name: String, file: File) {
        this.name = name
        this.file = file
    }

    constructor(name: String, author: String?, lastUpdated: String?, online: Boolean, file: File?) {
        this.author = author
        this.lastUpdated = lastUpdated
        this.name = name
        if (file != null) {
            this.file = file
        }
        isOnline = online
    }

    fun rename(newName: String?) {
        try {
            val original = Paths.get(file.canonicalPath)
            val to = Paths.get(file.path)
            Files.move(original, to)
        } catch (e: Exception) {
            NotificationManager.getNotificationManager()
                .show(Notification(Notification.NotificationType.ERROR, "Error!", "Couldn\'t rename config!", 1))
            e.printStackTrace()
        }
    }
}