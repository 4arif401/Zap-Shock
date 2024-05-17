package dev.lab.week3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import dev.lab.week3.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCalculate, btnClear;
    EditText etInput, etInput2;
    TextView textView, textTCharge, textFCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btClear);
        etInput2 = findViewById(R.id.etInputRb);
        etInput = findViewById(R.id.etInputKwh);
        textTCharge = findViewById(R.id.textTCharge);
        textFCost = findViewById(R.id.textFCost);
        btnCalculate.setOnClickListener(this);
        btnClear.setOnClickListener(this);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        showWelcomeDialog();

    }

    private void showWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //message layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView);

        //title
        TextView titleView = dialogView.findViewById(R.id.dialogTitle);
        titleView.setText("Welcome!");
        titleView.setTextColor(Color.BLACK); // Set title text color to black

        //message
        TextView messageView = dialogView.findViewById(R.id.dialogMessage);
        messageView.setText("Please input the number of electricity unit used in kWh and rebate percentage between 0 - 5.");
        messageView.setTextColor(Color.BLACK); // Set message text color to black
        messageView.setGravity(Gravity.CENTER); // Center-align the message text

        //OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog when OK is clicked
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //button color
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(Color.BLACK);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            //Toast.makeText(this, "about clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        else if(selected == R.id.menuHome) {
            Toast.makeText(this, "You are already at Home Page", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (view == btnCalculate) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            String kwh = etInput.getText().toString();
            String rb = etInput2.getText().toString();
            int numkwh;
            double rebate;
            double charge = 0;
            double cost = 0;

            try {
                numkwh = Integer.parseInt(kwh);
                rebate = Integer.parseInt(rb);

                // Check if rebate is within the valid range (0 to 5)
                if (rebate < 0 || rebate > 5) {
                    Toast.makeText(this, "Rebate must be between 0 and 5", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early if input is invalid
                }

                //First 200 kWh
                if (numkwh <= 200) {
                    charge += numkwh * 0.218;
                    cost = charge - (charge * (rebate / 100));
                }
                //Including next 100 kWh
                else if (numkwh > 200 && numkwh <= 300) {
                    charge += 200 * 0.218;
                    numkwh = numkwh - 200;
                    charge += numkwh * 0.334;
                    cost = charge - (charge * (rebate / 100));
                }
                //Including next 300 kWh
                else if (numkwh > 300 && numkwh <= 600) {
                    charge += 200 * 0.218;
                    numkwh = numkwh - 200;
                    charge += 100 * 0.334;
                    numkwh = numkwh - 100;
                    charge += numkwh * 0.516;
                    cost = charge - (charge * (rebate / 100));
                }
                //Including another next 300 kWh and onwards
                else if (numkwh > 600) {
                    charge += 200 * 0.218;
                    numkwh = numkwh - 200;
                    charge += 100 * 0.334;
                    numkwh = numkwh - 100;
                    charge += 300 * 0.516;
                    numkwh = numkwh - 300;
                    charge += numkwh * 0.546;
                    cost = charge - (charge * (rebate / 100));
                }
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Please enter number in the input field", Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                Toast.makeText(this, "Please enter number in the input field", Toast.LENGTH_SHORT).show();

            }

            textTCharge.setText("RM" + String.format("%.2f", charge));
            textFCost.setText("RM" + String.format("%.2f", cost));

        } else if (view == btnClear) {
            // Clear text fields etInput and etInput2
            etInput.setText("");
            etInput2.setText("");
        }

        /*if (view == btnClear){
            etInput.setText("");
            etNumber2.setText("");
        } */
    }
}
