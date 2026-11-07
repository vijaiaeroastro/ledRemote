package app.ledremote;

/**
 * NEC infrared encoder for ConsumerIrManager: on/off durations in microseconds,
 * starting with "on", sent on a 38 kHz carrier.
 */
final class Nec {

    static final int CARRIER_HZ = 38000;

    private Nec() {
    }

    /** Extended NEC frame: address low byte, address high byte, command, ~command (each LSB first). */
    static int[] frame(int address16, int command) {
        int[] bytes = {address16 & 0xFF, (address16 >>> 8) & 0xFF, command & 0xFF, ~command & 0xFF};
        int[] p = new int[2 + 32 * 2 + 1];
        int i = 0;
        p[i++] = 9000;
        p[i++] = 4500;
        for (int b : bytes) {
            for (int bit = 0; bit < 8; bit++) {
                p[i++] = 560;
                p[i++] = ((b >>> bit) & 1) == 1 ? 1690 : 560;
            }
        }
        p[i] = 560;
        return p;
    }
}
