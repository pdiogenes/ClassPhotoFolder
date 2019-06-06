package com.example.classphotofolder;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class horario extends AppCompatActivity  {

    Controller_Horario horario;
    Cursor cursor;
    String[] nomeCampos;
    int[] idViews;
    Spinner horarios,dias;
    ImageButton imgBtnInserir, imgBtnExcluir;
    private static final int horaFim = 1;
    private static final int horaInicio = 2;
    TimePickerDialog timePickerDialog;
    EditText editNomeDisc, editHoraIni, editHoraFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);


        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnExcluir = (ImageButton) findViewById(R.id.imgBtnExcluir);
        horarios = (Spinner) findViewById(R.id.spnHorarios);

        editNomeDisc = (EditText) findViewById(R.id.txtNomeDisciplina);
        editHoraIni = (EditText) findViewById(R.id.txtHoraInicio);
        editHoraFim = (EditText) findViewById(R.id.txtHoraFim);
        dias = (Spinner) findViewById(R.id.spnDiaSemana);

        editNomeDisc.setHint("Digite o nome da disciplina");
        editHoraIni.setHint("Insira a hora de inicio");
        editHoraFim.setHint("Insira a hora de fim");

                // (2) create a simple static list of strings
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Segunda");
        spinnerArray.add("Terça");
        spinnerArray.add("Quarta");
        spinnerArray.add("Quinta");
        spinnerArray.add("Sexta");
        spinnerArray.add("Sábado");
        spinnerArray.add("Domingo");
        // (3) create an adapter from the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            android.R.layout.simple_spinner_item,
            spinnerArray
        );

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // (4) set the adapter on the spinner
        dias.setAdapter(adapter);

        editHoraIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwTimeDialog(horaInicio);
            }
        });

        editHoraFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwTimeDialog(horaFim);
            }
        });


        horario = new Controller_Horario(this);
        cursor = horario.preencheSpinner();
        nomeCampos = new String[]{DBHelper.COLUNA_NOME_DISCIPLINA, DBHelper.COLUNA_DIA_SEMANA, DBHelper.COLUNA_HORA_INICIO, DBHelper.COLUNA_HORA_FIM};
        idViews = new int[]{R.id.txtNomeDisciplina, R.id.txtDiaSemana, R.id.txtHoraInicio, R.id.txtHoraFim};
        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this, R.layout.spinner_horarios, cursor, nomeCampos, idViews, 0);
        horarios.setAdapter(adaptador);

        horario.preencheSpinner();


        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnInserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //inserirHorario();
            }
        });

        imgBtnExcluir = (ImageButton) findViewById(R.id.imgBtnExcluir);
        imgBtnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //excluirHorario();
            }
        });
    }

    void throwTimeDialog(final int i){
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if(i == horaInicio){
                    editHoraIni.setText(hourOfDay + ":" + minutes);
                }
                else if(i == horaFim){
                    editHoraFim.setText(hourOfDay + ":" + minutes);
                }
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }

    void inserirHorario(){
        /*
        String resultado;
        if (!editNomeDisc.getText().toString().isEmpty() && !editHoraIni.getText().toString().isEmpty()
            && !editHoraFim.getText().toString().isEmpty()) {
            String nomeDisc = editNomeDisc.getText().toString();
            String diaSemana = this.dias.getSelectedItem().toString();
            //TODO horaInicio
            //TODO horaFim
            //TODO spnDiaSemana
            resultado = this.horario.insereHorario(nomeDisc, horaInicio, horaFim , diaSemana);
            Toast.makeText(this, nomeDisc + " no dia " + diaSemana + " inserida com sucesso", Toast.LENGTH_SHORT).show();
            editNomeDisc.setText(""); editHoraIni.setText(""); editHoraFim.setText("");
            //preencheAdaptador();
        } else Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

        */
    }

    void excluirHorario(Controller_Horario horario , Spinner horarios){
        cursor = (Cursor) horarios.getSelectedItem();
        horario.apagaRegistro(cursor.getInt(0));
        //preencheAdaptador();
    }

}
