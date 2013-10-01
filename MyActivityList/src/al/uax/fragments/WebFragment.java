package al.uax.fragments;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

public class WebFragment extends WebViewFragment{
	public  static final String EXTRA_URL = "url";
	
	private CharSequence url;
	private SpecialWebViewClient mWebViewClient;
	
	@Override
	public void setArguments(Bundle args){
		this.url = args.getCharSequence(EXTRA_URL);
		super.setArguments(args);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d("TAG", "onCreate");
		super.onCreate(savedInstanceState);
		mWebViewClient = new SpecialWebViewClient(); 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		Log.d("TAG", "onActivityCreate");
		super.onActivityCreated(savedInstanceState);
		
		getWebView().setWebViewClient(mWebViewClient);
		getWebView().loadUrl(url.toString());
	}
	
	public class SpecialWebViewClient extends WebViewClient{
		
		public boolean shouldOverrideUrlLoading(WebView view, String url){
			WebSettings webSettings = view.getSettings();
			webSettings.setJavaScriptEnabled(true);
			view.loadUrl(url);
			return true;
		}
	}
}