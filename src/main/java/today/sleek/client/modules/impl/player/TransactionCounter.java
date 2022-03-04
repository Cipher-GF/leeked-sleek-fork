package today.sleek.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import today.sleek.base.event.impl.PacketEvent;
import today.sleek.base.event.impl.UpdateEvent;
import today.sleek.base.modules.ModuleCategory;
import today.sleek.base.modules.ModuleData;
import today.sleek.client.modules.impl.Module;
import today.sleek.client.utils.chat.ChatUtil;
import today.sleek.base.value.value.BooleanValue;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

@ModuleData(
        name = "Transactions",
        category = ModuleCategory.PLAYER,
        description = "Checks if the server is using transactions to count ping"
)
public class TransactionCounter extends Module {

    private long currTime = System.currentTimeMillis();
    private long lastC0F = -1;
    private int transactions = 0;

    private BooleanValue debugC0F = new BooleanValue("Test C0F", this, true);

    private long startTime;

    @Override
    public void onEnable() {
        startTime = System.currentTimeMillis();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        long start = System.currentTimeMillis() - startTime;

        if (start >= 20000) {
            if (transactions >= 10) {
                ChatUtil.log("This server seems to use transaction packets to count your ping.");
                ChatUtil.log("Using a transaction delaying or cancelling disabler is recommended.");
                toggle();
            } else {
                ChatUtil.log("It looks like this server doesn't use transaction packets to count your ping.");
                toggle();
            }
            ChatUtil.log("You've sent §c" + transactions + " §ftransactions to the server.");
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (debugC0F.getValue() && event.getPacket() instanceof C0FPacketConfirmTransaction) {
            if (lastC0F == -1) {
                lastC0F = System.currentTimeMillis();
                ChatUtil.log("Received first ConfirmTransaction packet");
            } else {
                ChatUtil.log("Received ConfirmTransaction (" + (System.currentTimeMillis() - lastC0F) + "ms since last transaction)");
            }

            transactions++;
            lastC0F = System.currentTimeMillis();

            if (transactions > 10) {

            }
        }
    }

}
