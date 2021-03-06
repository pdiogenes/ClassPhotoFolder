package com.example.classphotofolder;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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

    //teste
    long ini = 0;
    long fi = 0;

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


        preencheAdaptador();
        horario.preencheSpinner();


        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnInserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inserirHorario();
            }
        });

        imgBtnExcluir = (ImageButton) findViewById(R.id.imgBtnExcluir);
        imgBtnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                excluirHorario(horario, horarios);
            }
        });



        horarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) horarios.getSelectedItem();
                editNomeDisc.setText(cursor.getString
                        (cursor.getColumnIndexOrThrow(DBHelper.COLUNA_NOME_DISCIPLINA)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void throwTimeDialog(final int i){
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String minutos = "", horas = "";

                if(minutes < 10){
                    minutos = "0"+minutes;
                } else minutos = ""+minutes;

                if(hourOfDay < 10){
                    horas = "0"+hourOfDay;
                }
                else horas = ""+hourOfDay;

                if(i == horaInicio){
                    editHoraIni.setText(horas + ":" + minutos);
                    String aux = horas+minutos;
                    ini = Long.parseLong(aux);
                }
                else if(i == horaFim){
                    editHoraFim.setText(horas + ":" + minutos);
                    String aux = horas+minutos;
                    fi = Long.parseLong(aux);
                }
            }
        }, 0, 0, true);
        timePickerDialog.show();
    }

    void inserirHorario(){

        String resultado;
        if (!editNomeDisc.getText().toString().isEmpty() && !editHoraIni.getText().toString().isEmpty()
            && !editHoraFim.getText().toString().isEmpty()) {
            String nomeDisc = editNomeDisc.getText().toString();
            String diaSemana = this.dias.getSelectedItem().toString();
            String horaInicio = this.editHoraIni.getText().toString();
            String horaFim = this.editHoraFim.getText().toString();
            resultado = this.horario.insereHorario(nomeDisc, ini, fi, horaInicio, horaFim, diaSemana);
            Toast.makeText(this, nomeDisc + " no dia " + diaSemana + " inserida com sucesso", Toast.LENGTH_SHORT).show();
            editNomeDisc.setText(""); editHoraIni.setText(""); editHoraFim.setText("");
            preencheAdaptador();
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                    File.separator + "ClassPhotoFolder/" + nomeDisc);
            editHoraFim.setText("");
            editHoraIni.setText("");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
                Toast.makeText(this, "Pasta criada.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Pasta da matéria " + nomeDisc +" já existe.", Toast.LENGTH_SHORT).show();
            }
            if(!success){
              Toast.makeText(this, "Erro ao criar a pasta.", Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();


    }

    void excluirHorario(Controller_Horario horario , Spinner horarios){
        cursor = (Cursor) horarios.getSelectedItem();
        horario.apagaRegistro(cursor.getInt(0));
        preencheAdaptador();
        editHoraFim.setText("");
        editHoraIni.setText("");
    }

    public void preencheAdaptador() {
        this.horario = new Controller_Horario(this);
        this.cursor = horario.preencheSpinner();
        this.nomeCampos = new String[]{DBHelper.COLUNA_NOME_DISCIPLINA, DBHelper.COLUNA_DIA_SEMANA, DBHelper.COLUNA_HORA_INICIO_STRING, DBHelper.COLUNA_HORA_FIM_STRING};
        this.idViews = new int[]{R.id.txtNomeDisciplina, R.id.txtDiaSemana, R.id.txtHoraInicio, R.id.txtHoraFim};
        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this, R.layout.spinner_horarios, this.cursor, this.nomeCampos, this.idViews, 0);
        horarios.setAdapter(adaptador);
    }


}
