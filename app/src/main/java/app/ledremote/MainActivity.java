package app.ledremote;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ConsumerIrManager ir;
    private Vibrator vibrator;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ir = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        statusText = findViewById(R.id.status_text);

        View root = findViewById(R.id.root);
        int pad = root.getPaddingLeft();
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(pad + insets.getSystemWindowInsetLeft(), pad + insets.getSystemWindowInsetTop(),
                    pad + insets.getSystemWindowInsetRight(), pad + insets.getSystemWindowInsetBottom());
            return insets;
        });

        buildRemote(findViewById(R.id.remote_panel));
        if (!hasIr()) {
            statusText.setText("⚠ No IR blaster on this device — needs a phone with built-in IR");
            statusText.setTextColor(0xFFFFB74D);
        }
    }

    private int dp(float v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private void buildRemote(LinearLayout panel) {
        for (Keys.Key[] row : Keys.ROWS) {
            LinearLayout tr = new LinearLayout(this);
            tr.setOrientation(LinearLayout.HORIZONTAL);
            panel.addView(tr, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
            for (Keys.Key k : row) {
                TextView b = new TextView(this);
                b.setText(k.label);
                b.setGravity(Gravity.CENTER);
                b.setTextColor(k.textColor);
                b.setTextSize(k.label.length() > 6 ? 14 : 17);
                b.setTypeface(Typeface.DEFAULT_BOLD);
                b.setSoundEffectsEnabled(false);
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(k.color);
                gd.setCornerRadius(dp(40));
                gd.setStroke(dp(1), 0x33000000);
                b.setBackground(new RippleDrawable(ColorStateList.valueOf(0x55FFFFFF), gd, null));
                b.setOnClickListener(v -> send(k));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                lp.setMargins(dp(7), dp(6), dp(7), dp(6));
                tr.addView(b, lp);
            }
        }
    }

    private boolean hasIr() {
        return ir != null && ir.hasIrEmitter();
    }

    private void send(Keys.Key k) {
        if (!hasIr()) {
            Toast.makeText(this, "This phone has no IR blaster", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            ir.transmit(Nec.CARRIER_HZ, k.pattern);
            vibrate();
        } catch (RuntimeException e) {
            statusText.setText("IR send failed: " + e.getMessage());
            statusText.setTextColor(0xFFFFB74D);
        }
    }

    private void vibrate() {
        if (vibrator == null || !vibrator.hasVibrator()) return;
        try {
            vibrator.vibrate(VibrationEffect.createOneShot(12, VibrationEffect.DEFAULT_AMPLITUDE));
        } catch (Throwable ignored) {
        }
    }

}
