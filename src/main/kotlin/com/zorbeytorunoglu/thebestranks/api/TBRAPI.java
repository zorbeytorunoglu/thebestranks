package com.zorbeytorunoglu.thebestranks.api;

import com.zorbeytorunoglu.thebestranks.TBR;
import com.zorbeytorunoglu.thebestranks.utils.RankUtils;

public class TBRAPI {

    private static TBR plugin;

    private TBRAPI() {}

    public static TBR getTBR() {
        return plugin;
    }

    public static void setPluginInstance(TBR plugin) {
        if (TBRAPI.plugin!=null) {
            throw new UnsupportedOperationException("You cannot initialize the plugin instance after it was initialized.");
        }

        TBRAPI.plugin = plugin;

    }

    public static RankUtils getRankManager() {
        return plugin.getUtils().getRankUtils();
    }

}
