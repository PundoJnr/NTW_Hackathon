package com.bk.lrandom.realestate.fragments;
//package com.bk.lrandom.droidmarket.fragments;
//
//import java.io.File;
//
//import com.commonsware.cwac.camera.CameraHost;
//import com.commonsware.cwac.camera.CameraView;
//import com.commonsware.cwac.camera.PictureTransaction;
//import com.commonsware.cwac.camera.SimpleCameraHost;
//import com.commonsware.cwac.camera.ZoomTransaction;
//
//import android.net.Uri;
//import android.nfc.Tag;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class ParentCameraFragment extends Fragment {
//	private CameraView cameraView = null;
//	private SimpleCameraHost host = null;
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.app.Fragment#onCreateView(android.view. LayoutInflater,
//	 * android.view.ViewGroup, android.os.Bundle)
//	 */
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		cameraView = new CameraView(getActivity());
//		cameraView.setHost(getHost());
//
//		return (cameraView);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.app.Fragment#onResume()
//	 */
//	@Override
//	public void onResume() {
//		super.onResume();
//
//		cameraView.onResume();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see android.app.Fragment#onPause()
//	 */
//	@Override
//	public void onPause() {
//		cameraView.onPause();
//
//		super.onPause();
//	}
//
//	/**
//	 * Use this if you are overriding onCreateView() and are inflating a layout
//	 * containing your CameraView, to tell the fragment the CameraView, so the
//	 * fragment can help manage it. You do not need to call this if you are
//	 * allowing the fragment to create its own CameraView instance.
//	 * 
//	 * @param cameraView
//	 *            the CameraView from your inflated layout
//	 */
//	protected void setCameraView(CameraView cameraView) {
//		this.cameraView = cameraView;
//	}
//
//	/**
//	 * @return the CameraHost instance you want to use for this fragment, where
//	 *         the default is an instance of the stock SimpleCameraHost.
//	 */
//	public SimpleCameraHost getHost() {
//		if (host == null) {
//			host = new SimpleCameraHost(getActivity());
//		}
//
//		return (host);
//	}
//
//	/**
//	 * Call this (or override getHost()) to supply the CameraHost used for most
//	 * of the detailed interaction with the camera.
//	 * 
//	 * @param host
//	 *            a CameraHost instance, such as a subclass of SimpleCameraHost
//	 */
//	public void setHost(SimpleCameraHost host) {
//		this.host = host;
//	}
//
//	/**
//	 * Call this to take a picture and get access to a byte array of data as a
//	 * result (e.g., to save or stream).
//	 */
//	public Uri takePicture() {
//		takePicture(false, true);
//		return getPhotoPath();
//	}
//
//	/**
//	 * Call this to take a picture.
//	 * 
//	 * @param needBitmap
//	 *            true if you need to be passed a Bitmap result, false otherwise
//	 * @param needByteArray
//	 *            true if you need to be passed a byte array result, false
//	 *            otherwise
//	 */
//	public Uri takePicture(boolean needBitmap, boolean needByteArray) {
//		cameraView.takePicture(needBitmap, needByteArray);
//		return getPhotoPath();
//	}
//
//	/**
//	 * Call this to take a picture.
//	 * 
//	 * @param xact
//	 *            PictureTransaction with configuration data for the picture to
//	 *            be taken
//	 */
//	public Uri takePicture(PictureTransaction xact) {
//		cameraView.takePicture(xact);
//		return getPhotoPath();
//	}
//
//	/**
//	 * @return true if we are recording video right now, false otherwise
//	 */
//	public boolean isRecording() {
//		return (cameraView == null ? false : cameraView.isRecording());
//	}
//
//	/**
//	 * Call this to begin recording video.
//	 * 
//	 * @throws Exception
//	 *             all sorts of things could go wrong
//	 */
//	public void record() throws Exception {
//		cameraView.record();
//	}
//
//	/**
//	 * Call this to stop the recording triggered earlier by a call to record()
//	 * 
//	 * @throws Exception
//	 *             all sorts of things could go wrong
//	 */
//	public void stopRecording() throws Exception {
//		cameraView.stopRecording();
//	}
//
//	/**
//	 * @return the orientation of the screen, in degrees (0-360)
//	 */
//	public int getDisplayOrientation() {
//		return (cameraView.getDisplayOrientation());
//	}
//
//	/**
//	 * Call this to lock the camera to landscape mode (with a parameter of
//	 * true), regardless of what the actual screen orientation is.
//	 * 
//	 * @param enable
//	 *            true to lock the camera to landscape, false to allow normal
//	 *            rotation
//	 */
//	public void lockToLandscape(boolean enable) {
//		//cameraView.lockToLandscape(enable);
//	}
//
//	/**
//	 * Call this to begin an auto-focus operation (e.g., in response to the user
//	 * tapping something to focus the camera).
//	 */
//	public void autoFocus() {
//		cameraView.autoFocus();
//	}
//
//	/**
//	 * Call this to cancel an auto-focus operation that had been started via a
//	 * call to autoFocus().
//	 */
//	public void cancelAutoFocus() {
//		cameraView.cancelAutoFocus();
//	}
//
//	/**
//	 * @return true if auto-focus is an option on this device, false otherwise
//	 */
//	public boolean isAutoFocusAvailable() {
//		return (cameraView.isAutoFocusAvailable());
//	}
//
//	/**
//	 * If you are in single-shot mode and are done processing a previous
//	 * picture, call this to restart the camera preview.
//	 */
//	public void restartPreview() {
//		cameraView.restartPreview();
//	}
//
//	/**
//	 * @return the name of the current flash mode, as reported by
//	 *         Camera.Parameters
//	 */
//	public String getFlashMode() {
//		return (cameraView.getFlashMode());
//	}
//
//	/**
//	 * Call this to begin populating a ZoomTransaction, with the eventual goal
//	 * of changing the camera's zoom level.
//	 * 
//	 * @param level
//	 *            a value from 0 to getMaxZoom() (called on Camera.Parameters),
//	 *            to indicate how tight the zoom should be (0 indicates no zoom)
//	 * @return a ZoomTransaction to configure further and eventually call go()
//	 *         to actually do the zooming
//	 */
//	public ZoomTransaction zoomTo(int level) {
//		return (cameraView.zoomTo(level));
//	}
//
//	/**
//	 * Calls startFaceDetection() on the CameraView, which in turn calls
//	 * startFaceDetection() on the underlying camera.
//	 */
//	public void startFaceDetection() {
//		cameraView.startFaceDetection();
//	}
//
//	/**
//	 * Calls stopFaceDetection() on the CameraView, which in turn calls
//	 * startFaceDetection() on the underlying camera.
//	 */
//	public void stopFaceDetection() {
//		cameraView.stopFaceDetection();
//	}
//
//	public boolean doesZoomReallyWork() {
//		return (cameraView.doesZoomReallyWork());
//	}
//
//	public static final String TAG = "ParentCamera";
//
//	public void setFlashMode(String mode) {
//		cameraView.setFlashMode(mode);
//		Log.i(TAG, mode);
//	}
//
//	public Uri getPhotoPath() {
//		return Uri.fromFile(getHost().getPhotoPath());
//	}
//}
