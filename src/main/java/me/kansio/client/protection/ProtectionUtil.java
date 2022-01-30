package me.kansio.client.protection;

import me.kansio.client.Client;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import sun.misc.Unsafe;
import viamcp.utils.JLoggerToLog4j;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProtectionUtil {

    private static List<String> knownDebuggers = Arrays.asList(
            "wireshark",
            "fiddler",
            "ollydbg",
            "tcpview",
            "autoruns",
            "autorunsc",
            "filemon",
            "procmon",
            "regmon",
            "procexp",
            "idaq",
            "idaq64",
            "immunitydebugger",
            "dumpcap",
            "hookexplorer",
            "importrec",
            "petools",
            "lordpe",
            "sysinspector",
            "proc_analyzer",
            "sysAnalyzer",
            "sniff_hit",
            "windbg",
            "joeboxcontrol",
            "joeboxserver",
            "tv_w32",
            "vboxservice",
            "vboxtray",
            "xenservice",
            "vmtoolsd",
            "vmwaretray",
            "vmwareuser",
            "vgauthservice",
            "vmacthlp",
            "vmsrvc",
            "vmusrvc",
            "prl_cc",
            "prl_tools",
            "qemu-ga",
            "program manager",
            "vmdragdetectwndclass",
            "windump",
            "tshark",
            "networkminer",
            "capsa",
            "solarwinds",
            "glasswire",
            "http sniffer",
            "httpsniffer",
            "http debugger",
            "httpdebugger",
            "http debug",
            "httpdebug",
            "httpsniff",
            "httpnetworksniffer",
            "kismac",
            "http toolkit",
            "cain and able",
            "cainandable",
            "etherape"
    );

    public static boolean isDebugging() {
        List<String> launchArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String s : launchArgs) {
            if (s.startsWith("-Xbootclasspath") || s.startsWith("-Xdebug") || s.startsWith("-agentlib") || s.startsWith("-javaagent:") || s.startsWith("-Xrunjdwp:") || s.startsWith("-verbose")) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasDebuggerRunning() {
        //wont work on linux
        if (!SystemUtils.IS_OS_WINDOWS) {
            return false;
        }

        try {
            ProcessBuilder pBuilder = new ProcessBuilder();
            pBuilder.command("tasklist.exe");
            Process process = pBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                for (String knownDbg : knownDebuggers) {
                    if (line.toLowerCase().contains(knownDbg)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean doesChecksumMatch() {
        String sum = read("http://sleek.today/data/latest_sum");

        if (sum.equalsIgnoreCase("error")) {
            return false;
        }

        //check if it's running inside an ide
        String PROPERTY = System.getProperty("java.class.path");
        if (PROPERTY.contains("idea_rt.jar")) {
            return true;
        }

        //Get the check sum of the latest jar file

        try {
            CodeSource source = Client.class.getProtectionDomain().getCodeSource();
            File location = new File(source.getLocation().toURI().getPath());

            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            String cs = ProtectionUtil.checksum(md5Digest, location);

            Logger jLogger = new JLoggerToLog4j(LogManager.getLogger("checksum"));
            jLogger.log(Level.INFO, "checker:    cs = " + cs + "     sum = " + sum);

            if (cs.equalsIgnoreCase(sum)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static String read(String targetURL) {
        try {
            URLConnection connection = new URL(targetURL).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            URL url = new URL(targetURL);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }

            bufferedReader.close();
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }

    public static String checksum() {
        try {
            CodeSource source = Client.class.getProtectionDomain().getCodeSource();
            File file = new File(source.getLocation().toURI().getPath());

            MessageDigest digest = MessageDigest.getInstance("MD5");
            String cs = ProtectionUtil.checksum(digest, file);

            //Get file input stream for reading the file content
            FileInputStream fis = new FileInputStream(file);

            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }


            //close the stream; We don't need it now.
            fis.close();

            //Get the hash's bytes
            byte[] bytes = digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //return complete hash
            return sb.toString();
        } catch (Exception e) {

        }
        return "none found???";
    }

    public static String checksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        ;

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public static void crashJVM() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            unsafe.putAddress(0, 0);
        } catch (Exception e) {

        }
    }
}
