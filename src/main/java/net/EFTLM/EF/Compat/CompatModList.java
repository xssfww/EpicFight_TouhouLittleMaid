package net.EFTLM.EF.Compat;

import net.minecraftforge.fml.ModList;
public class CompatModList {
    public static String EFN = "efn";
    public static String EFN_Enhance = "efn_enhance";
    public static boolean LoadedEFN() {
        return ModList.get().isLoaded(EFN);
    }
    public static boolean LoadedEFN_Enhance() {
        return ModList.get().isLoaded(EFN_Enhance);
    }
}
