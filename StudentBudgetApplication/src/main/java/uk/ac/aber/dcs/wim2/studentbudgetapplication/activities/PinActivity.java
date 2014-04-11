package uk.ac.aber.dcs.wim2.studentbudgetapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import uk.ac.aber.dcs.wim2.studentbudgetapplication.R;

public class PinActivity extends Activity implements View.OnClickListener {

    private ImageView one;
    private ImageView two;
    private ImageView three;
    private ImageView four;
    private ImageView five;
    private ImageView six;
    private ImageView seven;
    private ImageView eight;
    private ImageView nine;
    private ImageView zero;
    private ImageView clear;
    private EditText pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        registerViews();
    }

    private void registerViews() {
        one = (ImageView) findViewById(R.id.oneImage);
        two = (ImageView) findViewById(R.id.twoImage);
        three = (ImageView) findViewById(R.id.threeImage);
        four = (ImageView) findViewById(R.id.fourImage);
        five = (ImageView) findViewById(R.id.fiveImage);
        six = (ImageView) findViewById(R.id.sixImage);
        seven = (ImageView) findViewById(R.id.sevenImage);
        eight = (ImageView) findViewById(R.id.eightImage);
        nine = (ImageView) findViewById(R.id.nineImage);
        zero = (ImageView) findViewById(R.id.zeroImage);
        clear = (ImageView) findViewById(R.id.clearImage);
        pin = (EditText) findViewById(R.id.pin);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        clear.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pin, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oneImage:
                pin.append("1");
                break;
            case R.id.twoImage:
                pin.append("2");
                break;
            case R.id.threeImage:
                pin.append("3");
                break;
            case R.id.fourImage:
                pin.append("4");
                break;
            case R.id.fiveImage:
                pin.append("5");
                break;
            case R.id.sixImage:
                pin.append("6");
                break;
            case R.id.sevenImage:
                pin.append("7");
                break;
            case R.id.eightImage:
                pin.append("8");
                break;
            case R.id.nineImage:
                pin.append("9");
                break;
            case R.id.zeroImage:
                pin.append("0");
                break;
            case R.id.clearImage:
                pin.setText("");
                break;

        }
        if(pin.getText().length() == 4){
            Intent intent = new Intent(this, EnterActivity.class);//checking here
            startActivity(intent);
        }
    }
}
