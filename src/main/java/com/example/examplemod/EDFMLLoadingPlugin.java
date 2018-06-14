package com.example.examplemod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)

public class EDFMLLoadingPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
//This will return the name of the class "mod.culegooner.EDClassTransformer"
        return new String[]{EDClassTransformer.class.getName()};
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
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
