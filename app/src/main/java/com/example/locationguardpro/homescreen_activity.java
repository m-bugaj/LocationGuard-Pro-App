
	 
	/*
	 *	This content is generated from the API File Info.
	 *	(Alt+Shift+Ctrl+I).
	 *
	 *	@desc 		
	 *	@file 		homescreen
	 *	@date 		Friday 01st of December 2023 11:33:56 AM
	 *	@title 		Page 1
	 *	@author 	
	 *	@keywords 	
	 *	@generator 	Export Kit v1.3.figma
	 *
	 */
	

package com.example.locationguardpro;

import android.app.Activity;
import android.os.Bundle;


import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

	public class homescreen_activity extends Activity {

	
	private View _bg__homescreen_ek2;
	private TextView locationguard_pro;
	private View _bg___group_3_ek1;
	private View rectangle_1;
	private TextView start_tracking;
	private View _bg___group_2_ek1;
	private View rectangle_3;
	private TextView reports;
	private View _bg__group_4_ek1;
	private View rectangle_2;
	private TextView stop_tracking;
	private ImageView _settingsicon_1;
	private ImageView helpiconwhite_1;
	private ImageView logowh_1;
	private View _bg__group_9_ek1;
	private ImageView rectangle;
	private View ellipse_1;
	private View _bg__frame_1_ek1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);

		
		_bg__homescreen_ek2 = (View) findViewById(R.id._bg__homescreen_ek2);
		locationguard_pro = (TextView) findViewById(R.id.locationguard_pro);
		_bg___group_3_ek1 = (View) findViewById(R.id._bg___group_3_ek1);
		rectangle_1 = (View) findViewById(R.id.rectangle_1);
		start_tracking = (TextView) findViewById(R.id.start_tracking);
		_bg___group_2_ek1 = (View) findViewById(R.id._bg___group_2_ek1);
		rectangle_3 = (View) findViewById(R.id.rectangle_3);
		reports = (TextView) findViewById(R.id.reports);
		_bg__group_4_ek1 = (View) findViewById(R.id._bg__group_4_ek1);
		rectangle_2 = (View) findViewById(R.id.rectangle_2);
		stop_tracking = (TextView) findViewById(R.id.stop_tracking);
		_settingsicon_1 = (ImageView) findViewById(R.id._settingsicon_1);
		helpiconwhite_1 = (ImageView) findViewById(R.id.helpiconwhite_1);
		logowh_1 = (ImageView) findViewById(R.id.logowh_1);
		_bg__group_9_ek1 = (View) findViewById(R.id._bg__group_9_ek1);
		rectangle = (ImageView) findViewById(R.id.rectangle);
		ellipse_1 = (View) findViewById(R.id.ellipse_1);
		_bg__frame_1_ek1 = (View) findViewById(R.id._bg__frame_1_ek1);
	
		
		_settingsicon_1.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				
				//Intent nextScreen = new Intent(getApplicationContext(), android_small___4_activity.class);
				//startActivity(nextScreen);
			
		
			}
		});
		
		
		//custom code goes here
	
	}
}
	
	