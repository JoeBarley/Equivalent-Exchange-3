package com.pahimar.ee3;

import com.pahimar.ee3.configuration.ConfigurationHandler;
import com.pahimar.ee3.handler.CraftingHandler;
import com.pahimar.ee3.handler.FuelHandler;
import com.pahimar.ee3.handler.GuiHandler;
import com.pahimar.ee3.init.ModBlocks;
import com.pahimar.ee3.init.ModItems;
import com.pahimar.ee3.item.EquivalencyStack;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.proxy.IProxy;
import com.pahimar.ee3.reference.EventHandlers;
import com.pahimar.ee3.reference.Reference;
import com.pahimar.ee3.util.LogHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, certificateFingerprint = Reference.FINGERPRINT, version = "0.2")
public class EquivalentExchange3
{
    @Instance
    public static EquivalentExchange3 instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event)
    {

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Initialize the configuration
        ConfigurationHandler.init(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Reference.MOD_ID.toLowerCase() + File.separator);

        // Initialize mod items
        ModItems.init();

        // Initialize mod blocks
        ModBlocks.init();

        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Register the GUI Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        // Initialize mod tile entities
        proxy.registerTileEntities();

        // Initialize custom rendering and pre-load textures (Client only)
        proxy.initRenderingAndTextures();

        // Register the Items Event Handler
        FMLCommonHandler.instance().bus().register(EventHandlers.itemEventHandler);
        MinecraftForge.EVENT_BUS.register(EventHandlers.itemEventHandler);

        CraftingHandler.init();

        // Register our fuels
        GameRegistry.registerFuelHandler(new FuelHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        EquivalencyStack equivalencyStack = new EquivalencyStack(Items.beef);

        for (Object object : equivalencyStack.getEquivalentStacks())
        {
            LogHelper.info(object);
        }
    }

    @EventHandler
    public void handleIMCMessages(IMCEvent event)
    {

    }
}
