package app.ledremote;

/**
 * Action 3007108 RGB+CCT bulb remote. Extended NEC, address 0xEF00 (on-air bytes 00 EF).
 * Codes found by scanning; modes, brightness, CCT and the 60s timer are intentionally left out.
 */
final class Keys {

    private static final int ADDRESS = 0xEF00;

    static final class Key {
        final String label;
        final int color;
        final int textColor;
        final int[] pattern;

        Key(String label, int color, int textColor, int command) {
            this.label = label;
            this.color = color;
            this.textColor = textColor;
            this.pattern = Nec.frame(ADDRESS, command);
        }
    }

    private static final int DARK = 0xFF1B1F26;
    private static final int LIGHT = 0xFFFFFFFF;

    /** Layout rows, top to bottom, as on the physical remote. */
    static final Key[][] ROWS = {
            {
                    new Key("ON", 0xFF15171C, LIGHT, 0x00),
                    new Key("OFF", 0xFFE53935, LIGHT, 0x01),
            },
            {
                    new Key("Red", 0xFFD32F2F, LIGHT, 0x04),
                    new Key("Green", 0xFF2E9D4A, LIGHT, 0x05),
                    new Key("Blue", 0xFF2F3FB5, LIGHT, 0x06),
            },
            {
                    new Key("Sky blue", 0xFF2D7FE0, LIGHT, 0x08),
                    new Key("Mint", 0xFF8EDDB0, DARK, 0x09),
                    new Key("Violet", 0xFF6A35B8, LIGHT, 0x0A),
            },
            {
                    new Key("Rose", 0xFFE0335A, LIGHT, 0x0C),
                    new Key("Teal", 0xFF1BAFAA, LIGHT, 0x0D),
                    new Key("Orange", 0xFFF26A2E, LIGHT, 0x0E),
            },
            {
                    new Key("Warm\nwhite", 0xFFF5A623, DARK, 0x10),
                    new Key("Neutral\nwhite", 0xFFB5D334, DARK, 0x11),
                    new Key("Cool\nwhite", 0xFF35C4EC, DARK, 0x12),
            },
    };

    private Keys() {
    }
}
