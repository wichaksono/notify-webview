
/**
 * pasang di onCreate()
 **/
WebView webviewku = findViewById(R.id.webviewku);
webviewku.setWebViewClient(new WebViewClient());
webviewku.setWebChromeClient(new WebChromeClient());
webviewku.getSettings().setJavaScriptEnabled(true);
webviewku.addJavascriptInterface(new WebViewInit(this), "Android");
webviewku.loadUrl("http://157.230.37.179:4002/ww.php");
webviewku.setVisibility(View.GONE);
