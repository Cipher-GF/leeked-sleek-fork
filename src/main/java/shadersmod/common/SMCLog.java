package shadersmod.common;




public abstract class SMCLog
{
    
    private static final String PREFIX = "[Shaders] ";

    public static void severe(String message)
    {
        org.tinylog.Logger.error("[Shaders] " + message);
    }

    public static void warning(String message)
    {
        org.tinylog.Logger.warn("[Shaders] " + message);
    }

    public static void info(String message)
    {
        org.tinylog.Logger.info("[Shaders] " + message);
    }

    public static void fine(String message)
    {
        org.tinylog.Logger.debug("[Shaders] " + message);
    }

    public static void severe(String format, Object... args)
    {
        String s = String.format(format, args);
        org.tinylog.Logger.error("[Shaders] " + s);
    }

    public static void warning(String format, Object... args)
    {
        String s = String.format(format, args);
        org.tinylog.Logger.warn("[Shaders] " + s);
    }

    public static void info(String format, Object... args)
    {
        String s = String.format(format, args);
        org.tinylog.Logger.info("[Shaders] " + s);
    }

    public static void fine(String format, Object... args)
    {
        String s = String.format(format, args);
        org.tinylog.Logger.debug("[Shaders] " + s);
    }
}
