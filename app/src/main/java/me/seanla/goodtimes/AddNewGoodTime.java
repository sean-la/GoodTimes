package me.seanla.goodtimes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class AddNewGoodTime extends AppCompatActivity {

    private DbAdapter dbHelper;
    private static TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_good_time);
        dateView = (TextView) findViewById(R.id.dateView);
    }

    /* One day I will figure out how to make this class static and still work */
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Show the date on screen
            dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void submitGoodTime(View view) {

        EditText newGoodTime = (EditText) findViewById(R.id.newGoodTime);

        String goodTime = newGoodTime.getText().toString();
        String date = dateView.getText().toString();

        dbHelper = new DbAdapter(this);
        dbHelper.openForWriting();
        dbHelper.insertGoodTime(date, goodTime);
        dbHelper.close();

        finish();
    }
}
