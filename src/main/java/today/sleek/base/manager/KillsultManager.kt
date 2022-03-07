package today.sleek.base.manager

import today.sleek.client.utils.Util
import today.sleek.client.utils.chat.ChatUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.util.*

/**
 * @author Kansio
 */
class KillsultManager : Util() {

    val killSults = ArrayList<String>()

    val defaultMessages = Arrays.asList(
        "You got sleeked L",
        "Sleek is just better...",
        "Verus got killed by Sleek",
        "You just got absolutely raped by Sleek :)",
        "Sleek too op I guess",
        "You got killed by %user% (uid: %uid%) using Sleek hake",
        "We do be doing slight amounts of trolling using Sleek",
        "me and da sleek bois destroying blocksmc",
        "sussy among us sleek hack???",
        "mad? rage at me on discord: %discord%",
        "got angry? rage at me on discord: %discord%",
        "rage at me on discord: %discord%",
        "mad? rage at me on discord: %discord%",
        "like da hack? .gg/GUauVwtFKj",
        "hack too good? get it here: .gg/GUauVwtFKj"
    )

    fun reload() {
        killSults.clear()
        readKillSults()
    }

    fun readKillSults() {
        val killsultsFile = File(mc.mcDataDir, File.separator + "Sleek" + File.separator + "killsults.txt")
        try {
            //if the file doesn't exist just create it
            if (!killsultsFile.exists()) {
                killsultsFile.createNewFile()
                val writer = BufferedWriter(FileWriter(killsultsFile))
                writer.write("// You can customize the killsults in this file")
                writer.newLine()
                writer.write("// Placeholders: ")
                writer.newLine()
                writer.write("// %discord% - Displays your discord")
                writer.newLine()
                writer.write("// %username% - Displays your client username")
                writer.newLine()
                writer.write("// %killed% - Displays the name of the person you killed")
                writer.newLine()
                writer.write("// %uid% - Displays your uid")
                writer.newLine()
                writer.write(" ")
                writer.newLine()
                for (ks in defaultMessages) {
                    writer.write(ks)
                    writer.newLine()
                }
                writer.close()
            }

            //read from the file
            val scanner = Scanner(killsultsFile)
            while (scanner.hasNextLine()) {
                val data = scanner.nextLine()

                //allow comments in the file
                if (data.startsWith("//")) continue
                if (data.startsWith(" ")) continue
                println("Found: $data")
                killSults.add(data)
            }
            scanner.close()
        } catch (e: Exception) {
            ChatUtil.log("There was an error whilst reading the killsults.")
            ChatUtil.log(e.message)
        }
    }
}