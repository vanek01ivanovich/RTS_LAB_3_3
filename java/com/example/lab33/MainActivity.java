package com.example.lab33;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random = new Random();
    EditText a;
    EditText b;
    EditText c;
    EditText d;
    EditText y;
    TextView result;
    TextView time;
    TextView iterations;
    int valueA;
    int valueB;
    int valueC;
    int valueD;
    double valueY;

    static int[][] genders = new int[4][4];
    static int[] deltas = new int[4];
    static double[] probabilities;
    static int randomRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        y = findViewById(R.id.y);
        result = findViewById(R.id.result);
        time = findViewById(R.id.time);
        iterations = findViewById(R.id.iterations);
    }

    public void onStartClick(View view){
        result.setText(null);
        time.setText(null);
        iterations.setText(null);
        valueA = Integer.parseInt(a.getText().toString());
        valueB = Integer.parseInt(b.getText().toString());
        valueC = Integer.parseInt(c.getText().toString());
        valueD = Integer.parseInt(d.getText().toString());
        valueY = Double.parseDouble(y.getText().toString());
        startLabSolve(valueA,valueB,valueC,valueD,valueY);
    }

    public void startLabSolve(int a,int b,int c,int d,double y){
        findRandomGen(y);
        int count = 0;
        int[] parametrs = {a,b,c,d};

        double startTime = System.nanoTime();
        while (findDelta(parametrs,y)){
            count++;
            makeRoulette();
            makeCrossAndMutation();
            if (count >1000) {
                System.out.println("unsuccessfully ");
                break;
            }
        }
        double endTime = System.nanoTime();
        double times = endTime - startTime;
        times /= 1000000;

        time.setText(Double.toString(times));
        iterations.setText(Integer.toString(count));

    }

    public void findRandomGen(double y){
        randomRange = (int)Math.round(y/2);
        for (int i = 0; i < genders.length; i++) {
            for (int j = 0; j < genders[i].length; j++) {
                genders[i][j] = random.nextInt((6 - 1) + 1) + 1;
            }
        }
    }

    public boolean findDelta(int[] parametrs,double y){
        for (int i = 0; i < genders.length; i++) {
            int temp = 0;
            for (int j = 0; j < genders[i].length; j++) {
                temp += genders[i][j]*parametrs[j];
            }
            deltas[i] = temp;
        }
        for (int i = 0; i < deltas.length; i++) {
            deltas[i] = Math.abs(deltas[i]-(int)y);
            if (deltas[i] == 0){
                result.setText(Arrays.toString(genders[i]));
                return false;
            }
        }

        return true;
    }

    public void makeRoulette(){
        double sumValueDeltas = 0;
        probabilities = new double[deltas.length];
        for (int i = 0; i < deltas.length; i++) {
            probabilities[i] = (1/(double)deltas[i]);
            sumValueDeltas +=  (1/(double)deltas[i]);
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /=sumValueDeltas;
        }
    }

    public void makeCrossAndMutation(){
        int[] firstIter = new int[2];
        for (int i = 0; i < 2; i++) {
            int first = 0;
            int second = 0;
            while (true){
                first = random.nextInt((4));
                second = random.nextInt((4));
                if ((first != second)){
                    if (i == 0) {
                        firstIter[0] = first;
                        firstIter[1] = second;
                        break;
                    }
                    if (i == 1){
                        if (!((first == firstIter[0] || first == firstIter[1]) &&
                                (second == firstIter[0] || second == firstIter[1]))){
                            break;
                        }
                    }
                }
            }
            for (int j = 0; j < genders[first].length/2; j++) {
                int temp = genders[first][j];
                genders[first][j] = genders[second][j];
                genders[second][j] = temp;
            }
        }

        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] >= 0.5){
                genders[i][i]++;
                break;
            }
        }
    }

}
