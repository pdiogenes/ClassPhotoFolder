package com.example.classphotofolder;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class horario extends AppCompatActivity  {

    Controller_Horario horario;
    Cursor cursor;
    String[] nomeCampos;
    int[] idViews;
    Spinner horarios;
    ImageButton imgBtnInserir, imgBtnExcluir;
    private static final int horaFim = 1;
    private static final int horaInicio = 2;
    TimePickerDialog timePickerDialog;
    EditText txtNomeDisc, txtHoraIni, txtHoraFim, txtDiaSem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

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

        cursor = horario.preencheSpinner(); 
        nomeCampos = new String[]{DBHelper.COLUNA_NOME_DISCIPLINA, DBHelper.COLUNA_DIA_SEMANA, DBHelper.COLUNA_HORA_INICIO, DBHelper.COLUNA_HORA_FIM};
        idViews = new int[]{R.id.txtNomeDisciplina, R.id.txtDiaSemana, R.id.txtHoraInicio, R.id.txtHoraFim};
        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(), R.layout.spinner_horarios, cursor, nomeCampos, idViews, 0);
        horarios.setAdapter(adaptador);


        horario = new Controller_Horario(getBaseContext());
        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnExcluir = (ImageButton) findViewById(R.id.imgBtnExcluir);
        horarios = (Spinner) findViewById(R.id.spnHorarios);
        horario.preencheSpinner();


        ImageButton imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnInserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), novoHorario.class);
                startActivity(intent);
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
