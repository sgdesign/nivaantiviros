package com.studioninja.lockersgha.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.app.Fragment;
import com.studioninja.lockersgha.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.studioninja.lockersgha.R;

import java.io.File;
import java.util.ArrayList;

public class VideoFragment extends Fragment implements OnClickListener {

	ArrayList<File> files;
	File folder;
	GridView listView;
	Activity context;
	AQuery aq;
	private LayoutInflater inflaternew;
	ArrayList<Option> items;
	String old_dir = "";
	LinearLayout Hidephotolist;
	ImageView img;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.main, container, false);
		getActivity().setTitle(R.string.fragment_title_videovault);
		Hidephotolist = (LinearLayout) root.findViewById(R.id.Hidephotolist);
		context = getActivity();
		aq = new AQuery(context);

		img = (ImageView) root.findViewById(R.id.img);
		img.setBackgroundResource(R.drawable.add_video_icon);

		inflaternew = LayoutInflater.from(getActivity());
		listView = (GridView) root.findViewById(R.id.lview1);

		Hidephotolist.setOnClickListener(this);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		old_dir="";
		items = new ArrayList<Option>();
		files = new ArrayList<File>();
		folder = new File(Environment.getExternalStorageDirectory().toString() + "/.Android_Libraries/.Video/");

		ListfileTask ltf = new ListfileTask();
		ltf.execute();
	}

	public void listf(File directory, ArrayList<File> files2) {

		// ArrayList<Option> fls = new ArrayList<Option>();
		// int count = 0;
		// File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				if (!file.getName().equalsIgnoreCase(".nomedia")) {
					files.add(file);

					String str = file.getAbsolutePath();

					String[] separated = str.split("/");

					String strdir = separated[separated.length - 2];

					Log.e("strdir", "--->" + strdir);
					// count = count + 1;

					// Log.e("count", "--->" + count);

					if (old_dir.equals("") || (!old_dir.equals(strdir))) {

						String ff = "";
						if (strdir.startsWith(".")) {
							ff = strdir.substring(1);
						} else {
							ff = strdir;
						}

						items.add(new Option(ff, "", file.getAbsolutePath()));

						old_dir = strdir;
					}

					Log.e("file", "---->" + file);
				}
			} else if (file.isDirectory()) {
				listf(file, files);
			}
		}

		// items = fls;

	}

	private class ListfileTask extends AsyncTask<Context, Void, String> {
		ProgressDialog dialog = new ProgressDialog(getActivity());

		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(CUSTOM_PROGRESS_DIALOG);
			dialog.setMessage("Loading....please wait");
			dialog.setCancelable(false);
			dialog.show();
		}

		protected String doInBackground(Context... params) {
			try {
				listf(folder, files);
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			return "";
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			SetAdapter();
		}
	}

	private void SetAdapter() {
		// TODO Auto-generated method stub

		if (items.size() > 0) {
			listView.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.INVISIBLE);
		}

		listView.setAdapter(new ArrayAdapter<Option>(context, 0, items) {
			@Override
			public View getView(int position, View view, ViewGroup parent) {

				view = inflaternew.inflate(R.layout.video_row, null);
				AQuery recycle = aq.recycle(view);
				Option info = getItem(position);
				final ViewHolder holder = new ViewHolder();
				holder.pb = (ProgressBar) view.findViewById(R.id.progress1ew);
				holder.Albumname = (TextView) view.findViewById(R.id.albumname);
				holder.Coverimage = (ImageView) view.findViewById(R.id.coverpic);
				// holder.checkbox = (CheckBox)
				// view.findViewById(R.id.checkBox1);
				holder.rowclick = (LinearLayout) view.findViewById(R.id.rowclick);

				holder.Albumname.setText(info.name);

				Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(info.path, Thumbnails.MINI_KIND);
				holder.pb.setVisibility(View.INVISIBLE);
				recycle.id(holder.Coverimage).image(bmThumbnail);
				// File file = new File(info.path);
				// recycle.id(holder.Coverimage).progress(holder.pb).image(file, 300);

				// Log.e("subpath", "---->" + info.path.substring(0,
				// info.path.indexOf(".extra")));

				// Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
				// BitmapFactory.decodeFile(info.path.substring(0,
				// info.path.indexOf(".extra"))), THUMBNAIL_SIZE,
				// THUMBNAIL_SIZE);
				// holder.Coverimage.setImageBitmap(ThumbImage);

				view.setTag(holder);

				Option state = items.get(position);
				// holder.checkbox.setChecked(state.isFolderSelection());
				// holder.checkbox.setTag(state);

				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						// Option o = (Option)
						// parent.getItemAtPosition(position);
						// if (o.getData().equalsIgnoreCase("folder") ||
						// o.getData().equalsIgnoreCase("parent directory")) {
						// folder = new File(o.getPath());
						// // fill(folder);
						// } else {
						// onFileClick(o);
						// }

						Option bb = (Option) parent.getItemAtPosition(position);
						Intent i = new Intent(getActivity(), Hide_Videos.class);
						i.putExtra("albumlist", bb.getPath().substring(0, bb.getPath().lastIndexOf("/")));
						startActivity(i);

					}
				});

				return view;
			}
		});
	}

	static class ViewHolder {
		TextView Albumname;
		ImageView Coverimage;
		ProgressBar pb;
		// CheckBox checkbox;
		LinearLayout rowclick;

	}

	@Override
	public void onClick(View v) {
		if (v == Hidephotolist) {

			Intent i2 = new Intent(getActivity(), Sdcard_VideosAlbum.class);
			startActivity(i2);

		}
	}
}
