package al.uax.fragments;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MyFragment extends Activity{
	private WebListFragment mWebListFragment;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfragment);
		
		mWebListFragment = new WebListFragment();
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction initTransaction = fm.beginTransaction();
		initTransaction.add(R.id.web_list_fragment, mWebListFragment);
		initTransaction.commit();
		
		mWebListFragment = (WebListFragment) fm.findFragmentById(R.id.web_list_fragment);
	}
}