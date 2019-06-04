package com.example.classphotofolder;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class horario extends AppCompatActivity  {

    EditText editDisciplina, editHorarioInicio, editHorarioFim;
    Controller_Horario horario;
    Cursor cursor;
    String[] nomeCampos;
    int[] idViews;
    Spinner horarios;
    ImageButton imgBtnInserir, imgBtnAlterar, imgBtnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        horario = new Controller_Horario(getBaseContext());
        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        btnAlterar = (ImageButton) findViewById(R.id.btnAlterar);
        btnExcluir = (ImageButton) findViewById(R.id.btnApagar);
        editProduto = (EditText) findViewById(R.id.editProduto);
        editQuantidade = (EditText) findViewById(R.id.editQuantidade);
        editValor = (EditText) findViewById(R.id.editValor);
        produtos = (Spinner) findViewById(R.id.spinner);
        preencheAdaptador();

        ImageButton imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnInserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), novoHorario.class);
                startActivity(intent);
            }
        });
    }

}
