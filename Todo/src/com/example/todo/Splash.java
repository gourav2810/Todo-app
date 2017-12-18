package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	Thread t1=new Thread(){
		
		public void run()
		{
			try
			{
				
				sleep(2000);
			}
			catch(Exception e)
			{}
			finally
			{
				
				Intent i=new Intent(Splash.this,MainActivity.class);
				startActivity(i);
				finish();
			}
			
		}
		
		
	};
	t1.start();
}
}
