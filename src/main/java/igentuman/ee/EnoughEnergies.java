package igentuman.ee;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = EnoughEnergies.MODID,
        name = EnoughEnergies.NAME,
        version = EnoughEnergies.VERSION
)
public class EnoughEnergies
{
    public static final String MODID = "enoughenergies";
    public static final String NAME = "United Energy";
    public static final String VERSION = "1.0.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
    }
}
