package com.studioninja.lockersgha.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import com.studioninja.lockersgha.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.studioninja.lockersgha.R;

public class Sdcard_Videos extends Activity implements OnItemClickListener, OnClickListener {
	MovefileTask movefiletask = null;
	private ProgressDialog pDialog;
	public static final int CUSTOM_PROGRESS_DIALOG = 0;
	private Activity context;
	private LayoutInflater inflater;
	private AQuery aq;
	private GridView lv;
	ProgressBar progress;
	ImageView Back, Next;
	ArrayList<String> Album_id;
	List<Image_Info> events;
	// ArrayList<String> Albumlist;
	// Dialog dialog;
	String DirName = "";
	String TAG = "Sdcard_photo";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setup();

	}

	private void setup() {
		context = this;
		aq = new AQuery(context);
		setContentView(R.layout.albumphoto);
		progress = (ProgressBar) findViewById(R.id.progress);
		lv = (GridView) findViewById(R.id.lview1);
		inflater = LayoutInflater.from(this);
		Back = (ImageView) findViewById(R.id.back);
		Next = (ImageView) findViewById(R.id.next);
		// Albumlist = new ArrayList<String>();

		DirName = this.getIntent().getExtras().getString("albumlist");
		// Albumlist = (ArrayList<String>)
		// getIntent().getSerializableExtra("albumlist");

		Log.e("DirName ------>>>>>", "--->>" + DirName);

		events = new ArrayList<Image_Info>();
		ListfileTask ltf = new ListfileTask();
		ltf.execute();

		lv.setOnItemClickListener(this);
		Next.setOnClickListener(this);
		Back.setOnClickListener(this);

		String sdCard = Environment.getExternalStorageDirectory().toString();
		File dir = new File(sdCard + "/" + ".Android_Libraries");
		File photo_dir = new File(sdCard + "/" + ".Android_Libraries/.Photo");
		File Video_dir = new File(sdCard + "/" + ".Android_Libraries/.Video");
		if (!dir.exists()) {
			dir.mkdir();
			Log.e(">>>", dir + "created");
		}
		if (!photo_dir.exists()) {
			photo_dir.mkdir();
			Log.e(">>>", photo_dir + "created");
		}

		if (!Video_dir.exists()) {
			Video_dir.mkdir();
			Log.e(">>>", Video_dir + "created");
		}

	}

	private class ListfileTask extends AsyncTask<Context, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(CUSTOM_PROGRESS_DIALOG);
		}

		protected String doInBackground(Context... params) {
			try {
				// List<String> al = Constants.getLocalVideoListings(Sdcard_Videos.this, DirName.substring(DirName.lastIndexOf("/") + 1));
				List<String> al = Constants.ReadSDCard_Videos(DirName);

				for (int i = 0; i < al.size(); i++) {
					events.add(new Image_Info(al.get(i), false));
				}
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			return "";
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismissDialog(CUSTOM_PROGRESS_DIALOG);
			Load_Albumphoto();
		}
	}

	private void Load_Albumphoto() {
		// TODO Auto-generated method stub

		try {

			// dialog.dismiss();
			lv.setAdapter(new ArrayAdapter<Image_Info>(context, 0, events) {

				@Override
				public View getView(int position, View view, ViewGroup parent) {

					view = inflater.inflate(R.layout.albumphoto_row, null);
					AQuery recycle = aq.recycle(view);
					Image_Info info = getItem(position);
					final ViewHolder holder = new ViewHolder();
					holder.pb = (ProgressBar) view.findViewById(R.id.progress1);

					holder.Coverimage = (ImageView) view.findViewById(R.id.coverpic);
					holder.checkbox = (CheckBox) view.findViewById(R.id.checkBox1);

					// File file = new File(info.Imagepath);
					// recycle.id(holder.Coverimage).progress(holder.pb).image(file, 300);
					Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(info.Imagepath, Thumbnails.MINI_KIND);
					holder.pb.setVisibility(View.INVISIBLE);
					recycle.id(holder.Coverimage).image(bmThumbnail);

					view.setTag(holder);
					holder.checkbox.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							CheckBox cb = (CheckBox) v;
							Image_Info _state = (Image_Info) cb.getTag();
							// Toast.makeText(mContext, "Checkbox: " +
							// cb.getText() + " -> " + cb.isChecked(),
							// Toast.LENGTH_LONG).show();
							_state.setImageselection(cb.isChecked());
						}
					});

					// }else {
					// holder = (ViewHolder) view.getTag();
					// }

					Image_Info state = events.get(position);

					holder.checkbox.setChecked(state.isImageselection());
					holder.checkbox.setTag(state);

					lv.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							// Searchbean info1 = (Searchbean)
							// parent.getItemAtPosition(position);

						}
					});

					return view;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {

	}

	public class Image_Info {
		String Imagepath;
		boolean Imageselection;

		public boolean isImageselection() {
			return Imageselection;
		}

		public void setImageselection(boolean imageselection) {
			Imageselection = imageselection;
		}

		public Image_Info(String imagepath, boolean imageselection) {
			this.Imagepath = imagepath;
			this.Imageselection = imageselection;
		}
	}

	static class ViewHolder {
		ImageView Coverimage;
		ProgressBar pb;
		CheckBox checkbox;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == Next) {
			Album_id = new ArrayList<String>();
			StringBuffer responseText = new StringBuffer();
			responseText.append("Selected Countries are...\n");
			ArrayList<Image_Info> stateList = (ArrayList<Image_Info>) events;

			for (int i = 0; i < stateList.size(); i++) {
				Image_Info state = stateList.get(i);

				if (state.isImageselection()) {
					// responseText.append("\n" + state.getArtist_name());
					Album_id.add(state.Imagepath);
				}
			}
			Log.e(">>>>>>", ">>>>" + Album_id);

			if (Album_id.size() > 0) {
				movefiletask = new MovefileTask(0);
				movefiletask.execute();
			}

		} else if (v == Back) {
			finish();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CUSTOM_PROGRESS_DIALOG:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Loading...Please wait");
			pDialog.setIndeterminate(true);
			pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.my_progress_indeterminate));
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}

	}

	private class MovefileTask extends AsyncTask<Context, Void, String> {
		String resultPath = null;
		int Index = 0;

		public MovefileTask(int i) {
			this.Index = i;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(CUSTOM_PROGRESS_DIALOG);
		}

		protected String doInBackground(Context... params) {
			try {
				MoveFile(Album_id.get(Index));
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
			return resultPath;
		}

		private void MoveFile(String path) {

			String newpath = "";
			String dirpath = "";

			String sdCard = Environment.getExternalStorageDirectory().toString();
			// the file to be moved or copied
			File sourceLocation = new File(path);
			// make sure your target location folder exists!
			// File targetLocation = new File(sdCard + "/dmnew/dm.jpg");

			String[] separated = path.split("/");

			for (int i = 0; i < separated.length - 1; i++) {

				if (i == 0) {

					// newpath = "." + newpath;

				} else {

					if (newpath.equals("")) {

						newpath = newpath + "." + separated[i];
						dirpath = dirpath + "/." + separated[i];

						File my_dir = new File(sdCard + "/" + ".Android_Libraries/.Video" + dirpath);
						if (!my_dir.exists()) {
							my_dir.mkdir();
							Log.e("------------------>>>", my_dir + "created");
						}

					} else {
						newpath = newpath + "/." + separated[i];
						dirpath = dirpath + "/." + separated[i];

						File my_dir = new File(sdCard + "/" + ".Android_Libraries/.Video" + dirpath);
						if (!my_dir.exists()) {
							my_dir.mkdir();
							Log.e("------------------>>>", my_dir + "created");
						}

					}

					// dirpath--------> /.storage/.sdcard0/.WhatsApp/.Media/.WhatsApp Images

				}

				Log.e(TAG, i + "newpath------> " + newpath);
				Log.e(TAG, i + "dirpath--------> " + dirpath);

			}

			File targetLocation = new File(sdCard + "/" + ".Android_Libraries/.Video" + dirpath + "/." + path.substring(path.lastIndexOf("/") + 1));

			// just to take note of the location sources
			Log.e(TAG, "sourceLocation: " + sourceLocation);
			Log.e(TAG, "targetLocation: " + targetLocation);

			try {

				// 1 = move the file, 2 = copy the file
				int actionChoice = 2;

				// moving the file to another directory
				if (actionChoice == 1) {

					if (sourceLocation.renameTo(targetLocation)) {
						Log.e(TAG, "Move file successful.");
					} else {
						Log.e(TAG, "Move file failed.");
					}

				}

				// we will copy the file
				else {

					// make sure the target file exists

					if (sourceLocation.exists()) {

						InputStream in = new FileInputStream(sourceLocation);
						OutputStream out = new FileOutputStream(targetLocation);

						// Copy the bits from instream to outstream
						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						in.close();
						out.close();

						Log.v(TAG, "Copy file successful.");
						// sourceLocation.delete();
						deleteFile(sourceLocation.getAbsolutePath());

						try {
							File nomediafile = new File(sdCard + "/" + ".Android_Libraries/.Video" + dirpath + "/.nomedia");
							if (!nomediafile.exists()) {
								boolean b = nomediafile.createNewFile();
								Log.e("nomediafile file created>>>", ">>>" + b);
							}

						} catch (Exception e1) {
							e1.printStackTrace();
						}

						// ScanMedia(path);
						// ScanMedia(sdCard + "/" + ".Android_Libraries/.Photo" + dirpath + "/." + path.substring(path.lastIndexOf("/") + 1));

					} else {
						Log.v(TAG, "Copy file failed. Source file missing.");
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void deleteFile(String path) {
			try {

				getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "='" + path + "'", null);
			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		private void ScanMedia(String path) {
			ContentValues values = new ContentValues();
			values.put("_data", path);
			ContentResolver cr = getContentResolver();
			cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Index++;
			if (Index < Album_id.size()) {

				movefiletask = new MovefileTask(Index);
				movefiletask.execute();

			} else {
				dismissDialog(CUSTOM_PROGRESS_DIALOG);
				// Sdcard_photo.this.finish();
				setup();
			}

		}
	}

}
