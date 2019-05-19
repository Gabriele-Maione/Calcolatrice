package com.example.calcolatrice;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView _screen;
    private String display = "";
    private String currentOperator = "";
    private String result = "";
    private boolean uguale=false;
    private int numOperatori=0;
    private int numNumeri=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _screen = (TextView)findViewById(R.id.TextViewRisultato);
        _screen.setText(display);
        //animazione
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        Button myButton = (Button) findViewById(R.id.btn1);
        myButton.setAnimation(myAnim);
    }
    private void updateScreen(){
        _screen.setText(display);
    }
    public void onClickNumber(View v){
        if(numNumeri>14){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Impossibile inserire piu' di 15 cifre");
            alertDialogBuilder.show();
            return;
        }
        uguale=false;
        _screen.setTextColor(Color.parseColor("#FFFFFF"));
        if(result != ""){
            clear();
            updateScreen();
        }
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        Button b = (Button) v;
        b.setAnimation(myAnim);
        b.startAnimation(myAnim);
        display += b.getText();
        if(display.length()>=12){
            _screen.setTextSize(30);
        }
        updateScreen();
        numNumeri++;
    }
    private boolean isOperator(char op){
        switch (op){
            case '+':
            case '−':
            case '×':
            case '÷':return true;
            default: return false;
        }
    }
    public void onClickOperator(View v){
        numNumeri=0;
        if(numOperatori>39){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Impossibile inserire piu' di 40 operatori");
            alertDialogBuilder.show();
            return;
        }
        _screen.setTextColor(Color.parseColor("#FFFFFF"));
        if(display == "") return;
        uguale=false;
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.milkshake);
        Button b = (Button) v;
        b.setAnimation(myAnim);
        b.startAnimation(myAnim);
        if(result != ""){
            String _display = result;
            clear();
            display = _display;
        }
        if(currentOperator != ""){
            Log.d("CalcX", ""+display.charAt(display.length()-1));
            if(isOperator(display.charAt(display.length()-1))){
                display = display.replace(display.charAt(display.length()-1), b.getText().charAt(0));
                updateScreen();
                return;
            }
            else{
                getResult();
                if(result.charAt(result.length()-1)=='0'){
                    result=result.substring(0, result.length()-1);
                    result=result.substring(0, result.length()-1);
                }
                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }
        display +=b.getText();
        currentOperator = b.getText().toString();
        if(display.length()>=12){
            _screen.setTextSize(30);
        }
        updateScreen();
        numOperatori++;
    }
    private void clear(){
        display = "";
        currentOperator = "";
        result = "";
        _screen.setTextSize(50);
        uguale=false;
        numNumeri=0;
        numOperatori=0;
    }
    public void onClickClear(View v){
        clear();
        updateScreen();
    }
    private double operate(String a, String b, String op){
        switch (op){
            case "+": return Double.valueOf(a) + Double.valueOf(b);
            case "−": return Double.valueOf(a) - Double.valueOf(b);
            case "×": return Double.valueOf(a) * Double.valueOf(b);
            case "÷": try{
                return Double.valueOf(a) / Double.valueOf(b);
            }catch (Exception e){
                Log.d("Calc", e.getMessage());
            }
            default: return -1;
        }
    }
    private boolean getResult(){
        if(currentOperator == "") return false;
        String[] operation = display.split(Pattern.quote(currentOperator));
        if(operation.length < 2) return false;
        result = String.valueOf(operate(operation[0], operation[1], currentOperator));
        return true;
    }
    public void onClickEqual(View v){
        uguale=true;
        numOperatori=0;
        numNumeri=0;
        if(display == "") return;
        if(!getResult()) return;
        if(result.charAt(result.length()-1)=='0'){
            result=result.substring(0, result.length()-1);
            result=result.substring(0, result.length()-1);
        }
        _screen.setTextColor(getResources().getColor(R.color.verde));
        if(result.length()>12){
            _screen.setTextSize(30);
        }
        _screen.setText(String.valueOf(result));
        display=result;
    }
    public void onClickDelete(View v){
        if(display.length()>0){
            if((display.charAt(display.length()-1)=='÷') || (isOperator(display.charAt(display.length()-1)))){
                currentOperator="";
            }
            if(uguale==true){
                result=result.substring(0, result.length()-1);
                display=display.substring(0, display.length()-1);
            }
            else{
                display=display.substring(0, display.length()-1);
            }
            updateScreen();
        }
    }
    public void onClickPiuMeno(View v){
        if(display==""){
            display="-";
            updateScreen();
            return;
        }
    }
}