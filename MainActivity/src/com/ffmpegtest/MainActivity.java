package com.ffmpegtest;

import java.io.File;
import java.util.ArrayList;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

	private ActionBar mActionBar;
	private ArrayList<String> item = null;
	private ArrayList<String> path = null;
	private String root;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		listView = (ListView)findViewById(R.id.list);
		listView.setOnItemClickListener(this);

		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);

		root = Environment.getExternalStorageDirectory().getPath();


		getDir(root);
	}

	private void getDir(String dirPath)
	{
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(dirPath);
		File[] files = f.listFiles();
		mActionBar.setTitle(dirPath);

		if(!dirPath.equals(root))
		{
			item.add("../");
			path.add(f.getParent()); 
		}

		for(int i=0; i < files.length; i++)
		{
			File file = files[i];

			if(!file.isHidden() && file.canRead()){
				path.add(file.getPath());
				if(file.isDirectory()){
					item.add(file.getName() + "/");
				}else{
					item.add(file.getName());
				}
			} 
		}

		ArrayAdapter<String> fileList =
				new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
		listView.setAdapter(fileList); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.refresh:
			break;
		case R.id.search:
			break;
		}

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id)
	{
		File file = new File(path.get(position));
		Log.e("file", ""+file.getAbsolutePath());

		if (file.isDirectory())
		{
			if(file.canRead()){
				getDir(path.get(position));
			} else{
				new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle("[" + file.getName() + "] folder can't be read!")
				.setPositiveButton("OK", null).show(); 
			} 
		}
		else
		{
			Intent i = new Intent(MainActivity.this, VideoActivity.class);
			Uri uri = Uri.fromFile(file);
			i.setDataAndType(uri, "video/*");
			startActivity(i);
		}
	}

	@Override
    public void onBackPressed() {
		if(item.get(0).equals("../"))
			getDir(path.get(0));
		else
			finish();
    }
}