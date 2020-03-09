package org.projectcardboard;

import org.junit.Test;
import org.projectcardboard.client.controller.SoundEffectPlayer;
import org.projectcardboard.client.controller.SoundEffectPlayer.SoundName;

public class SoundEffectPlayerTest {

    @Test
    public void staticMapLoaded() {
        /**
         * Because this is static, private, void the only way to 
         * "test" this is to just do it. If resources weren't loaded, this will
         * except and the tests will fail.
         */
        SoundEffectPlayer.getInstance().playSound(SoundName.error);
    }
}