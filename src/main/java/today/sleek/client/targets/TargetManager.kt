package today.sleek.client.targets

import net.minecraft.entity.player.EntityPlayer
import java.util.ArrayList

class TargetManager {

    val target = ArrayList<String>()
    fun isTarget(ent: EntityPlayer): Boolean {
        return target.contains(ent.name)
    }

    fun isTarget(ent: String): Boolean {
        return target.contains(ent)
    }
}