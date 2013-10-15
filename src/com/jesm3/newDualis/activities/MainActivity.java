package com.jesm3.newDualis.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jesm3.newDualis.R;
import com.jesm3.newDualis.mail.MailActivity;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        startActivity(new Intent(this, MailActivity.class));
    }
}
