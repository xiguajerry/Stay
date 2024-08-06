/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.loader;

import me.alpha432.stay.mixin.StayMixinLoader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name(value = "Stay")
public class FMLCoreMod implements IFMLLoadingPlugin {

    public FMLCoreMod() {
        try {
            Loader.load();
            if (ForgeEntry.shouldLoad) StayMixinLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 2021/11/9
            if (ForgeEntry.shouldLoad) StayMixinLoader.load();
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
