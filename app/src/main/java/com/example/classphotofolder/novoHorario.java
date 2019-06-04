package com.example.classphotofolder;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

public class novoHorario extends AppCompatActivity {

    EditText txtNomeDisc, txtHoraIni, txtHoraFim, txtDiaSem;
    private static final int horaFim = 1;
    private static final int horaInicio = 2;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_horario);

        txtNomeDisc = (EditText) findViewById(R.id.txtNomeDisciplina);
        txtHoraIni = (EditText) findViewById(R.id.txtHoraInicio);
        txtHoraFim = (EditText) findViewById(R.id.txtHoraFim);
        txtDiaSem = (EditText) findViewById(R.id.txtDiaSemana);

        txtNomeDisc.setHint("Digite o nome da disciplina");
        txtHoraIni.setHint("Insira a hora de inicio");
        txtHoraFim.setHint("Insira a hora de fim");
        txtDiaSem.setHint("Digite o dia da semana dessa aula");

        txtHoraIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwTimeDialog(horaInicio);
            }
        });

        txtHoraFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwTimeDialog(horaFim);
            }
        });
    }

    void throwTimeDialog(final int i){
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if(i == horaInicio){
                    txtHoraIni.setText(hourOfDay + ":" + minutes);
                }
                else if(i == horaFim){
                    txtHoraFim.setText(hourOfDay + ":" + minutes);
                }
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }
}
