package al.uax.fragments;

import al.uax.myactivitylist.R;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WebListFragment extends ListFragment{
	boolean singleColumn;
	ArrayAdapter<CharSequence> mAdapter;
	
	public void onActivityCrated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		View webFragment = getActivity().findViewById(R.id.web_fragment);
		singleColumn = webFragment == null;
		
		mAdapter = ArrayAdapter.createFromResource(getActivity(), 
				R.array.webs, android.R.layout.simple_list_item_1);
		
		setListAdapter(mAdapter);
	}
	
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		
		CharSequence url = mAdapter.getItem(position);
		
		if(singleColumn){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url.toString()));
			startActivity(intent);
		}
		else{
			FragmentManager fm = getFragmentManager();
			FragmentTransaction transition = fm.beginTransaction();
			
			WebFragment web = new WebFragment();
			Bundle args = new Bundle();
			args.putCharSequence(WebFragment.EXTRA_URL, url);
			web.setArguments(args);
			
			transition.replace(R.id.web_fragment, web);
			transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transition.commit();
		}
	}
}