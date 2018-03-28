package com.example.aliegesoy.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    SharedPreferences prefs;
    ArrayList<ToDo> ToDos;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.ListView);
        prefs = getPreferences(Context.MODE_PRIVATE);
        button = findViewById(R.id.AddNew);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewToDo();
            }
        });
        Gson gson = new Gson();
        String json = prefs.getString("ToDos", "");
        ToDos = gson.fromJson(json, new TypeToken<List<ToDo>>() {
        }.getType());
        if (ToDos == null) {
            ToDos = new ArrayList<>();
        }
        UsersAdapter adapter = new UsersAdapter(getApplicationContext(), ToDos);
        listView.setAdapter(adapter);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public class UsersAdapter extends ArrayAdapter<ToDo> {
        public UsersAdapter(Context context, ArrayList<ToDo> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ToDo toDo = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
            }
            final TextView todoName = convertView.findViewById(R.id.ToDoName);
            final ImageButton imageButton = convertView.findViewById(R.id.doOrUndo);
            ImageButton delete = convertView.findViewById(R.id.DeleteToDo);
            todoName.setText(toDo.getName());
            if (toDo.getFlag() == 1) {
                imageButton.setImageResource(R.drawable.ic_replay_black_24dp);
                todoName.setPaintFlags(todoName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                todoName.setAlpha((float) 0.4);

            }
            todoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editToDoName(toDo.getName(), position);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToDos.remove(position);

                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToDo temp = ToDos.get(position);
                    if (temp.getFlag() == 1) {
                        temp.setFlag(0);
                        imageButton.setImageResource(R.drawable.ic_check_black_24dp);
                        todoName.setPaintFlags(todoName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        todoName.setAlpha((float) 1.0);

                    } else {
                        temp.setFlag(1);
                        imageButton.setImageResource(R.drawable.ic_replay_black_24dp);
                        todoName.setPaintFlags(todoName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        todoName.setAlpha((float) 0.4);

                    }
                }
            });
            return convertView;
        }
    }

    public void addNewToDo() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = dialogView.findViewById(R.id.edit);
        dialogBuilder.setTitle("New To Do");
        dialogBuilder.setMessage("Enter name here:");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ToDos.add(new ToDo(edt.getText().toString()));
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void editToDoName(String name, final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = dialogView.findViewById(R.id.edit);
        edt.setText(name);
        edt.setSelection(edt.getText().length());
        dialogBuilder.setTitle("Edit ToDo");
        dialogBuilder.setMessage("Enter new name here:");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ToDo temp = ToDos.get(position);
                temp.setName(edt.getText().toString());
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ToDos);
        prefsEditor.putString("ToDos", json);
        prefsEditor.commit();
    }


}