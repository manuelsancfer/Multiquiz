package edu.upc.eseiaat.pma.multiquiz;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Multiquiz extends AppCompatActivity {

    private int ids_answers[] = {  //tabla para pasar los id_ans
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    private int correct_answer;     //atribut tipo campo
    private int current_question;
    private int[] answer;           //solución de si vuelves hacía atrás siga la respuesta marcada
    private boolean[] answer_is_correct;
    private String[] all_questions;
    private TextView text_question;
    private RadioGroup group;
    private Button btn_next, btn_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiquiz);

        text_question = (TextView) findViewById(R.id.text_question); //seleccionar recuadro txt_question
        group = (RadioGroup) findViewById(R.id.answer_group); //obtener referencia al radiogroup
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_prev = (Button) findViewById(R.id.btn_prev);


        all_questions = getResources().getStringArray(R.array.all_questions); //coger array question
        answer_is_correct = new boolean[all_questions.length];  //crea objeto de tabla booleano con
        //la longitud de todas las preguntas
        answer = new int[all_questions.length]; //decir el tamaño que tienen todas las preguntas
        for(int i=0; i<answer.length; i++){ //poner array a -1 ya que si está a 0 se confunde
            answer[i]=-1;                   //el indice de las preguntas
        }


        current_question = 0;
        showQuestion();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Log.i("manu","btn clickado");

                checkAnswer();

                if(current_question < all_questions.length-1) {//para que al terminar el test no pete
                    //cuando sea la última no deberá incrementarse
                    current_question++;                        //para pasar la siguiente pregunta
                    showQuestion();
                } else{                                        // para contar las corretas
                    int correctas = 0, incorrectas = 0;
                    for (boolean b : answer_is_correct){    //cada elemento del array answer_is va
                        // metiendose en el booleano b, no hay que indicar cuando acaba.
                        if (b) correctas++;
                        else incorrectas++;
                    }
                    String resultado =
                            String.format("Correctas: %d -- Incorrectas: %d", correctas ,incorrectas);
                    Toast.makeText(Multiquiz.this, resultado, Toast.LENGTH_LONG).show();

                    finish();                               //finaliza la aplicación
                }


                for (int i=0; i<answer_is_correct.length; i++){ //ver si se guarda la tabla respuestas
                    Log.i("manu", String.format("Respuesta %d: %b", i, answer_is_correct[i]));
                }
            }
        });


        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if(current_question > 0){   //para que no funcione el volver atrás en la primera pregunta
                    current_question--;
                    showQuestion();
                }
            }
        });

    }

    private void checkAnswer() {
        int id = group.getCheckedRadioButtonId();   // me dice cual es el que esta activado
        int ans = -1;
        for(int i=0; i<ids_answers.length; i++){
            if(ids_answers[i]== id){    // cuando el array coincida con el activado
                ans=i;               //escibirá la respuesta en ans(el índice)
            }
        }

        answer_is_correct[current_question] = (ans == correct_answer); //para guardar si
        //respondio true o false
        answer[current_question] = ans;                //guarda cual he respondido
    }

    private void showQuestion() {       //creamos este método ya que se repite)
        String q = all_questions[current_question];   // q toma la pregunta actual guardada en all_quest
        String[] parts = q.split(";");                // partir las respuestas

        group.clearCheck();   //para que al pasar la pantalla se quite la respuesta anterior clicada

        text_question.setText(parts[0]);              //la pregunta la sacamos del string parts

        if(current_question == 0){          //desaparece en la primera pregunta el botón previous
            btn_prev.setVisibility(View.GONE);
        } else{
            btn_prev.setVisibility(View.VISIBLE);   //para que en las demás se vea
        }

        for(int i=0; i<ids_answers.length; i++){
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]); //boton(0),boton(1)...
            String ans = parts[i+1]; // parts(1) porque es la primera respuesta
            if(ans.charAt(0) == '*') //* lo tiene la respuesta correcta
            {
                correct_answer = i; //guardar el indice de la pregunta
                ans = ans.substring(1); //quita el primer caracter (*)
            }
            rb.setText(ans); //escribe una de las respuestas en el botón actual
            if(answer[current_question] == i){ //  si la pregunta actual coincide con el index del
                rb.setChecked(true);  //for, guarda la respuesta marcada
            }
        }

        if(current_question == all_questions.length-1){ //si pregunta actual=todas las preg-1
            // porque si un array tiene 10 la última pregunta será la 9
            btn_next.setText(R.string.finish);          //para quitar el btn next y poner el finish
        }
        else {
            btn_next.setText(R.string.next);          //para quitar el btn next y poner el finish
        }
    }

}
