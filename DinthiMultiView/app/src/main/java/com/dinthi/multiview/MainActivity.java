package com.dinthi.multiview;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SafeBrowsingResponse;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final List<WebView> webViews = new ArrayList<>();
    private final String[] labels = {"YouTube", "Instagram", "Facebook", "Search / website"};
    private final String[] urls = {
        "https://m.youtube.com/",
        "https://www.instagram.com/",
        "https://m.facebook.com/",
        "https://www.google.com/"
    };
    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(13, 16, 25));
        getWindow().setNavigationBarColor(Color.rgb(13, 16, 25));
        buildUi();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(13, 16, 25));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(14), dp(10), dp(10), dp(10));
        header.setBackgroundColor(Color.rgb(21, 26, 39));

        TextView title = new TextView(this);
        title.setText("Dinthi MultiView");
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.addView(title, new LinearLayout.LayoutParams(0, dp(42), 1));

        Button reload = new Button(this);
        reload.setText("Reload");
        reload.setAllCaps(false);
        reload.setOnClickListener(v -> {
            for (WebView w : webViews) w.reload();
        });
        header.addView(reload);
        root.addView(header);

        TextView note = new TextView(this);
        note.setText("4 panels • Choose a service in each panel • Sign-in and playback depend on each platform");
        note.setTextColor(Color.rgb(190, 198, 215));
        note.setTextSize(11);
        note.setPadding(dp(12), dp(7), dp(12), dp(7));
        root.addView(note);

        // A 2x2 grid adapts to portrait and landscape. Each panel owns its own WebView.
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(row1, new LinearLayout.LayoutParams(-1, 0, 1));
        root.addView(row2, new LinearLayout.LayoutParams(-1, 0, 1));

        for (int i = 0; i < 4; i++) {
            LinearLayout panel = new LinearLayout(this);
            panel.setOrientation(LinearLayout.VERTICAL);
            panel.setBackgroundColor(Color.rgb(29, 35, 50));
            LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(0, -1, 1);
            pp.setMargins(dp(3), dp(3), dp(3), dp(3));

            LinearLayout bar = new LinearLayout(this);
            bar.setOrientation(LinearLayout.HORIZONTAL);
            bar.setGravity(Gravity.CENTER_VERTICAL);
            bar.setPadding(dp(4), 0, dp(2), 0);

            Spinner picker = new Spinner(this);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, labels);
            picker.setAdapter(adapter);
            picker.setSelection(i < 3 ? i : 3);
            bar.addView(picker, new LinearLayout.LayoutParams(0, dp(42), 1));

            Button open = new Button(this);
            open.setText("↗");
            open.setContentDescription("Open this panel in browser");
            open.setMinWidth(dp(38));
            open.setPadding(0, 0, 0, 0);
            bar.addView(open, new LinearLayout.LayoutParams(dp(42), dp(42)));
            panel.addView(bar);

            WebView web = new WebView(this);
            web.setBackgroundColor(Color.WHITE);
            configureWebView(web);
            webViews.add(web);
            panel.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));

            final int index = i;
            picker.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                boolean first = true;
                @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    if (first) { first = false; }
                    String url = urls[position];
                    web.loadUrl(url);
                }
                @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });
            open.setOnClickListener(v -> {
                String current = web.getUrl();
                if (current == null || !current.startsWith("https://")) current = urls[picker.getSelectedItemPosition()];
                try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(current))); }
                catch (Exception e) { Toast.makeText(this, "No browser available", Toast.LENGTH_SHORT).show(); }
            });

            (i < 2 ? row1 : row2).addView(panel, pp);
        }

        Button tips = new Button(this);
        tips.setText("Usage & limitations");
        tips.setAllCaps(false);
        tips.setOnClickListener(v -> new AlertDialog.Builder(this)
            .setTitle("Dinthi MultiView")
            .setMessage("This app displays four independent web pages. It is not an official YouTube, Instagram, or Facebook app. Some platforms may block sign-in, video playback, or features in embedded WebViews. If a page does not work, use the ↗ button to open it in your browser or official app. Four simultaneous pages can use substantial RAM, battery and data. This app does not bypass platform restrictions.")
            .setPositiveButton("OK", null).show());
        root.addView(tips, new LinearLayout.LayoutParams(-1, dp(46)));

        setContentView(root);
    }

    private void configureWebView(WebView web) {
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true); // Required by modern social sites.
        s.setDomStorageEnabled(true);
        s.setSupportZoom(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setMediaPlaybackRequiresUserGesture(true); // Keep playback user-initiated.
        s.setJavaScriptCanOpenWindowsAutomatically(false);
        s.setSupportMultipleWindows(false);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String scheme = uri.getScheme() == null ? "" : uri.getScheme();
                if (!scheme.equals("https") && !scheme.equals("http")) return true;
                String host = uri.getHost() == null ? "" : uri.getHost().toLowerCase();
                if (isAllowedHost(host)) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); }
                catch (Exception ignored) {}
                return true;
            }
            @Override public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
                if (request.isForMainFrame()) {
                    Toast.makeText(MainActivity.this, "Page could not load. Check internet or open in browser.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isAllowedHost(String host) {
        return host.equals("youtube.com") || host.endsWith(".youtube.com")
            || host.equals("youtu.be")
            || host.equals("instagram.com") || host.endsWith(".instagram.com")
            || host.equals("facebook.com") || host.endsWith(".facebook.com")
            || host.equals("fb.com") || host.endsWith(".fb.com")
            || host.equals("google.com") || host.endsWith(".google.com")
            || host.equals("gstatic.com") || host.endsWith(".gstatic.com")
            || host.equals("googleusercontent.com") || host.endsWith(".googleusercontent.com");
    }

    @Override public void onBackPressed() {
        for (int i = webViews.size() - 1; i >= 0; i--) {
            WebView web = webViews.get(i);
            if (web.canGoBack()) { web.goBack(); return; }
        }
        super.onBackPressed();
    }

    @Override protected void onDestroy() {
        for (WebView web : webViews) {
            if (web.getParent() instanceof ViewGroup) ((ViewGroup) web.getParent()).removeView(web);
            web.stopLoading();
            web.loadUrl("about:blank");
            web.clearHistory();
            web.removeAllViews();
            web.destroy();
        }
        webViews.clear();
        super.onDestroy();
    }
}
