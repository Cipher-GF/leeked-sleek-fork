package me.kansio.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.notification.Notification;
import me.kansio.client.notification.NotificationManager;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleData(
        name = "Staff Detector",
        category = ModuleCategory.EXPLOIT,
        description = "Checks if there's a staff member in your game"
)
public class StaffDetect extends Module {

    @Getter
    private ArrayList<String> staffInMatch = new ArrayList<>();

    private List<String> verusKnownStaff = Arrays.asList(
            "Jinaaan", "_JustMix", "Eissaa", "iEvlect", "mohmad_q8", "1Brhom", "policewomen", "AssassinTime", "AlmondMilky", "PerfectRod_", "Ahmmd", "xImTaiG_", "xIBerryPlayz", "comsterr", "1Sweet", "1Hussain", "Ev2n", "1M7mdz", "iMehdi_", "FernandoEscobar", "1F5aMH___3oo", "EyesO_Diamond", "SenpaiAhmed", "xMz7", "1Daykel", "Bl2ck", "Aboz3bl", "JustGetOutDude", "Nwwf", "ixBander", "BinRashood", "WalriderTime", "ZANAD", "EthqnInMC", "Laeria", "1xM7moD_", "MK_F16", "502x", "1HeyImHasson_", "Ayrinea", "resci", "AngelRana", "Rvgeraz", "1Syr", "rr2ot", "M7mmd", "0ayt", "Bastic", "leeleeeleeeleee", "iikimo", "i7_e", "Not_Creegam", "1LaB", "iS3od_", "3Mmr", "puffingstardust", "YouwantMeThis", "Severity_", "baderr", "iSlo0oMx2", "xA7md_7rb", "1M7mmD", "Frqosh", "1Ahmd", "1Cloud_", "1mAhMeeD", "ebararh", "KaaReeeM", "M4rwaan", "MightyM7MD", "RADVN", "420syr1a", "Stay1High", "_N3", "Foxy__Boy", "DetectiveDnxx", "Rayleiigh", "FaRidok", "GzazhMan", "UmmIvy", "S3mm", "Dizibre", "Mvhammed", "Tibbz_BGamer", "FANOR_SY", "qMoha2nd", "N29r", "1RealFadi", "1Tz3bo", "1_Nr", "N15_", "MeAndOnlyMee", "xx1k", "1Neres", "1Mshari", "38eD", "bodi66699", "IamCsO", "m7mdxjw", "xL2d", "FastRank", "arbawii", "1ELY_", "TheDaddyRank", "Abdulaziz187", "HeWantMeee", "Mt2b", "b3ed", "LVQ_KWT", "420m2tt", "mzh", "PotatoSoublaki", "Mlazm_", "3AmOdi_", "MayBe1ForAGer", "w7r", "S3rvox", "MrProfessor_T", "DetectiveMuhamed", "Mieeteer", "1M0ha", "lt1x", "Bl2q", "Crlexs", "502z", "ImortalM3x", "Lwzi", "martadella", "dazaiscatgirl", "MShkLJe", "BlackOurs", "KingHOYT", "_Skofy", "0StefanSalvatore", "DetectiveFoxTY", "1ASSASSINATION_", "iTz_AbODe2", "m6m6_", "ilybb0", "zFlokenzVEVO", "vxom", "NaRqC", "Blood_Artz", "Muhameed", "1Terix", "Ba6ee5man", "Enorm1ty", "SheWantMeee", "Aiiden", "AbduIlah", "yQuack", "AdamViP_", "0hHaze", "nickdimos", "Tostiebramkaas", "0Taha", "1A7MD_PvP", "Raceth", "AYm13579", "M7_m", "MastersLouis__", "iikuya", "1A7mad1", "xiiNinja", "AbuA7md506", "Ix20", "yosife_7Y", "xIiM7mDx_", "VTomas", "V_Death_V", "judeh_gamer", "yff3", "1Selver", "1Ab0oDx", "Dqrkfall", "oq_ba", "mokgii", "1Levaai", "a4lf", "hlla", "rkqx", "xBeshoo", "TheBlackTime", "1Lweez", "ABBGEEN", "BaSiL_123", "3bdalh", "Y2serr", "JustKreem", "0hMustafa", "MightySaeed", "1ayt", "FireLigtning", "1MasterOogway", "Le3b0ody", "_Cignus_", "_Surfers_", "1Sinqx", "SKZ96", "ByNEK", "Ihaveatrashaim", "SpecialAmeer", "39be", "ImM7MAD", "StrengthSimp", "OnlyMoHqMeD__", "cKoro", "wl3d", "xiiRadi", "Sp0tzy_", "idpc", "rjassassin", "90fa", "Ahmed_1b", "a7madx7craft", "pauseflow", "EmirhanBoss", "AG_99", "Enormities", "nejem", "Lunching", "aXav", "a7mm", "7bx_", "MezzBek", "UmmDrRep", "2gfs", "NotMissYou_", "Its_Qassem", "Faisaal", "xZomik", "Violeet", "oBIXT", "ItzFHD", "Sarout", "0YH_", "Vrqq", "Aparamillos"
    );

    private int amount;
    boolean done = false;

    @Subscribe
    public void onRender(RenderOverlayEvent event) {
        if (staffInMatch.size() != 0 && done == true) {
            //mc.getNetHandler().handleTitle(new S45PacketTitle(S45PacketTitle.Type.TITLE, new ChatComponentText(ChatUtil.translateColorCodes("&c&lThere is a staff member in your lobby")), 100, 1000, 100));
            NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.WARNING,"WARNING", "§c§l" + amount + " Staff Members", 10));
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.ticksExisted < 5) {
            amount = 0;
            done = false;
            staffInMatch.clear();
        }

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (staffInMatch.contains(player.getName())) {
                continue;
            }

            for (String staff : verusKnownStaff) {
                if (player.getName().contains(staff)) {
                    amount++;
                    staffInMatch.add(player.getName());
                    ChatUtil.logNoPrefix("§4§l[Staff Detect]: §c" + staff + " §fis in your game!");
                    done = true;
                }
            }
        }
    }
}
