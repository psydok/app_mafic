package ru.csu.ttpapp.mvp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.csu.ttpapp.common.DialogOnSaveTask;
import ru.csu.ttpapp.R;
import ru.csu.ttpapp.common.ListTasks;
import ru.csu.ttpapp.common.Task;
import ru.csu.ttpapp.common.TaskAdapter;

public class MainActivity extends AppCompatActivity implements DialogOnSaveTask.DialogListener {

    public static Context mContext;
    Activity mActivity;

    public static TasksPresenter presenter;
    private TaskAdapter taskAdapter;

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextLink;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        if (mContext == null) mContext = MainActivity.this;
        if (mActivity == null) mActivity = MainActivity.this;

        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DialogOnSaveTask();
                dialog.show(getSupportFragmentManager(), "DialogOnSaveTask show");
            }
        });

        taskAdapter = new TaskAdapter();

        RecyclerView listView = findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(taskAdapter);
        listView.setVisibility(View.VISIBLE);

        TaskModel taskModel = new TaskModel(mContext);

        presenter = new TasksPresenter(taskModel);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPreferences:
                Toast.makeText(this, "Setting App - todo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemAbout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle(R.string.about_app)
                        .setMessage(R.string.about_description)
                        .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(final DialogFragment dialog) {
        editTextTitle = dialog.getDialog().getWindow().findViewById(R.id.setName);
        editTextLink = dialog.getDialog().getWindow().findViewById(R.id.setLink);
        if (!editTextLink.getText().toString().isEmpty())
            presenter.add();
        else Toast.makeText(this, getString(R.string.link_empty_error), Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    public Task getTaskFromDialog() {
        Task newTask = new Task();
        newTask.setLink(editTextLink.getText().toString());
        newTask.setTitle(editTextTitle.getText().toString());
        return newTask;
    }

    public void showTasks(ListTasks listTasks) {
        taskAdapter.setData(listTasks);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    private AlertDialog dialog;

    public void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void hideProgressDialog() {
        dialog.dismiss();
    }

}
