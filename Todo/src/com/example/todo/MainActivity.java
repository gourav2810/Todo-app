package com.example.todo;

import java.util.ArrayList;

import com.example.todo.db.DbHelper;
import com.example.todo.db.TaskContract;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	    private DbHelper mHelper;
	    private ListView mTaskListView;
	    private ArrayAdapter<String> mAdapter;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        mHelper = new DbHelper(this);
	        mTaskListView = (ListView) findViewById(R.id.listView1);

	      updateUI();
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.m_menu, menu);
	        return super.onCreateOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.action_add_task:
	                final EditText taskEditText = new EditText(this);
	                AlertDialog dialog = new AlertDialog.Builder(this)
	                        .setTitle("Add a new task")
	                        .setMessage("What do you want to do next?")
	                        .setView(taskEditText)
	                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int which) {
	                                String task = String.valueOf(taskEditText.getText());
	                                SQLiteDatabase db = mHelper.getWritableDatabase();
	                                ContentValues values = new ContentValues();
	                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
	                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
	                                        null,
	                                        values,
	                                        SQLiteDatabase.CONFLICT_REPLACE);
	                                db.close();
	                                updateUI();
	                                
	                            }
	                        })
	                        .setNegativeButton("Cancel", null)
	                        .create();
	                dialog.show();
	                return true;

	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	    
	    public void deleteTask(View view) {
	        View parent = (View) view.getParent();
	        TextView taskTextView = (TextView) parent.findViewById(R.id.title);
	        String task = String.valueOf(taskTextView.getText());
	        SQLiteDatabase db = mHelper.getWritableDatabase();
	        db.delete(TaskContract.TaskEntry.TABLE,
	                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
	                new String[]{task});
	        db.close();
	        updateUI();
	    }
	    private void updateUI() {
	        ArrayList<String> taskList = new ArrayList<String>();
	        SQLiteDatabase db = mHelper.getReadableDatabase();
	        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
	                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
	                null, null, null, null, null);
	        while (cursor.moveToNext()) {
	            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
	            taskList.add(cursor.getString(idx));
	        }
	        if (mAdapter == null) {
	            mAdapter = new ArrayAdapter<String>(this,R.layout.p_todo,R.id.title,taskList);
	            mTaskListView.setAdapter(mAdapter);
	        } else {
	            mAdapter.clear();
	            mAdapter.addAll(taskList);
	            mAdapter.notifyDataSetChanged();
	        }

	        cursor.close();
	        db.close();
	    }
	}

