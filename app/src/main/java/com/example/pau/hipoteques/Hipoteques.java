package com.example.pau.hipoteques;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Hipoteques extends AppCompatActivity {
    EditText Preu;
    EditText Estalvis;
    EditText Plas;
    EditText Euribor;
    EditText Diferencial;
    TextView mes;
    TextView total;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hipoteques);
        Preu = (EditText) findViewById(R.id.Preu);
        Estalvis = (EditText) findViewById(R.id.Estalvis);
        Plas =(EditText) findViewById(R.id.Plas);
        Euribor =(EditText) findViewById(R.id.Euribor);
        Diferencial=(EditText) findViewById(R.id.Diferencial);
        mes = (TextView) findViewById(R.id.mes);
        total=(TextView) findViewById(R.id.total);
        btn = (Button) findViewById(R.id.btn);

        Preu.setText("120000");
        Estalvis.setText("2000");
        Plas.setText("30");
        Euribor.setText("0.163");
        Diferencial.setText("2.5");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hipoteques, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Calcular(View view) {
        Calc calc = new Calc().invoke();
        double quotaMensual = calc.getQuotaMensual();
        double todasLasQuotas = calc.getTodasLasQuotas();

        Mostrar(quotaMensual, todasLasQuotas);
    }

    private void Mostrar(double quotaMensual, double todasLasQuotas) {
        DecimalFormat dc = new DecimalFormat("#0.00");
        mes.setText("MES:"+dc.format(quotaMensual));
        total.setText("TOTAL:"+dc.format(todasLasQuotas));
    }

    private class CalcConceptes {
        private int mesosApagar;
        private double divisor;
        private double dividendo;

        public int getMesosApagar() {
            return mesosApagar;
        }

        public double getDivisor() {
            return divisor;
        }

        public double getDividendo() {
            return dividendo;
        }

        public CalcConceptes invoke() {
            float preuHipoteca = Float.parseFloat(Preu.getText().toString());
            float estalvis =Float.parseFloat(Estalvis.getText().toString());
            float euribor =Float.parseFloat(Euribor.getText().toString());
            float diferencial=Float.parseFloat(Diferencial.getText().toString());
            int plaç= Integer.parseInt(Plas.getText().toString());

            float preuApagar =preuHipoteca-estalvis;
            float interesMensual=(euribor+diferencial)/12;
            mesosApagar = plaç * 12;

            divisor = preuApagar * interesMensual;
            dividendo = 100 * (1 - Math.pow(1 + interesMensual / 100, -mesosApagar));
            return this;
        }
    }

    private class CalcularQuotas {
        private CalcConceptes calcConceptes;
        private double quotaMensual;
        private double todasLasQuotas;

        public CalcularQuotas(CalcConceptes calcConceptes) {
            this.calcConceptes = calcConceptes;
        }

        public double getQuotaMensual() {
            return quotaMensual;
        }

        public double getTodasLasQuotas() {
            return todasLasQuotas;
        }

        public CalcularQuotas invoke() {
            double divisor = calcConceptes.getDivisor();
            double dividendo = calcConceptes.getDividendo();
            int mesosApagar = calcConceptes.getMesosApagar();

            quotaMensual = divisor / dividendo;
            todasLasQuotas = quotaMensual * mesosApagar;
            return this;
        }
    }

    private class Calc {
        private double quotaMensual;
        private double todasLasQuotas;

        public double getQuotaMensual() {
            return quotaMensual;
        }

        public double getTodasLasQuotas() {
            return todasLasQuotas;
        }

        public Calc invoke() {
            CalcConceptes calcConceptes = new CalcConceptes().invoke();
            CalcularQuotas calcularQuotas = new CalcularQuotas(calcConceptes).invoke();
            quotaMensual = calcularQuotas.getQuotaMensual();
            todasLasQuotas = calcularQuotas.getTodasLasQuotas();
            return this;
        }
    }
}
