package com.bk.lrandom.realestate.fragments;
//package com.bk.lrandom.droidmarket.fragments;
//
//import java.io.File;
//import java.util.List;
//
//import com.bk.lrandom.droidmarket.R;
//import com.bk.lrandom.droidmarket.business.Ultils;
//import com.bk.lrandom.droidmarket.interfaces.CameraComunicator;
//import com.squareup.picasso.Picasso;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.hardware.Camera;
//import android.hardware.Camera.Parameters;
//import android.hardware.Camera.Size;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//
//public class CameraFragment extends ParentCameraFragment {
//	private SeekBar zoom = null;
//	String flashMode = null;
//	ImageButton btnZoomIn, btnZoomOut, btnFlash;
//	int maxZoom = 0;
//	int currentZoom = 0;
//	boolean flashOff = true;
//	CustomCameraHost host;
//	CustomCameraHost.Builder builder;
//	CameraComunicator listener;
//	String abPath = null;
//
//	public static final CameraFragment newInstance() {
//		CameraFragment fragment = new CameraFragment();
//		return fragment;
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//		listener = (CameraComunicator) getActivity();
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		host = new CustomCameraHost(getActivity());
//		builder = new CustomCameraHost.Builder(host);
//		builder.photoDirectory(Ultils.createDirOnSDCard(getResources()
//				.getString(R.string.folder_save_photo)));
//		setHost(builder.useFullBleedPreview(true).build());
//	}
//
//	class CustomCameraHost extends SimpleCameraHost {
//
//		public CustomCameraHost(Context _ctxt) {
//			super(_ctxt);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public void onCameraFail(FailureReason reason) {
//			// TODO Auto-generated method stub
//			super.onCameraFail(reason);
//		}
//
//		@Override
//		public Parameters adjustPreviewParameters(Parameters parameters) {
//			// TODO Auto-generated method stub
//			flashMode = CameraUtils.findBestFlashModeMatch(parameters,
//					Camera.Parameters.FLASH_MODE_RED_EYE,
//					Camera.Parameters.FLASH_MODE_AUTO,
//					Camera.Parameters.FLASH_MODE_ON,
//					Camera.Parameters.FLASH_MODE_TORCH);
//			if (doesZoomReallyWork() && parameters.getMaxZoom() > 0) {
//				Log.i(TAG, parameters.getMaxZoom() + "");
//				maxZoom = parameters.getMaxZoom();
//			}
//			PackageManager pm = getActivity().getPackageManager();
//			if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//				if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
//					parameters
//							.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//				}
//			}
//			return super.adjustPreviewParameters(parameters);
//		}
//
//		@Override
//		public void saveImage(PictureTransaction xact, final byte[] image) {
//			// TODO Auto-generated method stub
//			super.saveImage(xact, image);
//			abPath = host.getPhotoPath().getAbsolutePath();
//			// new Thread() {
//			// public void run() {
//			CameraFragment.this.getActivity().runOnUiThread(new Runnable() {
//				public void run() {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							getActivity());
//					LayoutInflater inflater = (LayoutInflater) getActivity()
//							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					View previewParent = inflater.inflate(
//							R.layout.preview_layout, null);
//					builder.setTitle(
//							getResources().getString(
//									R.string.confirm_take_photo))
//							.setView(previewParent)
//							.setNegativeButton(
//									getResources().getString(
//											R.string.cancel_label),
//									new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//										}
//									})
//							.setPositiveButton(
//									getResources().getString(R.string.ok_label),
//									new DialogInterface.OnClickListener() {
//
//										@Override
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//											listener.takePhotoPath(abPath);
//											Log.i(TAG, abPath);
//										}
//									});
//					ImageView imagePreview = (ImageView) previewParent
//							.findViewById(R.id.preview);
//					Bitmap bitmapOrg = BitmapFactory.decodeByteArray(image, 0,
//							image.length);
//					Matrix matrix = new Matrix();
//					matrix.setRotate(90);
//					Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapOrg,
//							200, 200, true);
//					resizedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0,
//							resizedBitmap.getWidth(),
//							resizedBitmap.getHeight(), matrix, true);
//					imagePreview.setImageBitmap(resizedBitmap);
//					Dialog dialog = builder.create();
//					dialog.setCanceledOnTouchOutside(false);
//
//					dialog.show();
//					Log.i(TAG, "run after showw");
//				}
//			});
//			// }
//			// }.start();
//		}
//	}
//
//	private static final String TAG = "CameraFragment";
//
//	@Override
//	public View onCreateView(final LayoutInflater inflater,
//			ViewGroup container, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		View cameraView = super.onCreateView(inflater, container,
//				savedInstanceState);
//
//		View view = inflater.inflate(R.layout.camera_fragment_layout,
//				container, false);
//		btnZoomIn = (ImageButton) view.findViewById(R.id.zoom_in);
//		btnZoomOut = (ImageButton) view.findViewById(R.id.zoom_out);
//		btnFlash = (ImageButton) view.findViewById(R.id.flash);
//		btnFlash.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (flashOff) {
//					if (flashMode != null) {
//						btnFlash.setImageResource(R.drawable.ic_flash_en);
//						flashOff = false;
//					}
//				} else {
//					btnFlash.setImageResource(R.drawable.ic_flash_dis);
//					flashOff = true;
//				}
//			}
//		});
//
//		btnZoomIn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (maxZoom != 0 && currentZoom < maxZoom) {
//					currentZoom++;
//					zoomTo(currentZoom).go();
//					Log.i(TAG, "zoomin");
//				}
//			}
//		});
//
//		btnZoomOut.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (maxZoom != 0 && currentZoom > 0 && currentZoom < maxZoom) {
//					currentZoom--;
//					zoomTo(currentZoom).go();
//					Log.i(TAG, "zoomout");
//				}
//			}
//		});
//
//		((ViewGroup) view.findViewById(R.id.camera)).addView(cameraView);
//		ImageButton takePhoto = (ImageButton) view
//				.findViewById(R.id.take_photo);
//		takePhoto.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				PictureTransaction xact = new PictureTransaction(getHost());
//
//				if (!flashOff) {
//					if (flashMode != null) {
//						setFlashMode(flashMode);
//					}
//				} else {
//					setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//				}
//				Uri uri = takePicture(xact);
//
//				Log.i("URI", host.getPhotoPath() + "");
//			}
//		});
//		return view;
//	}
//}
