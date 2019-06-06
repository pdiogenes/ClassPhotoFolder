package com.example.classphotofolder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.Date;

public class Controller_Horario {
    private SQLiteDatabase db;
    private DBHelper banco;
    public Controller_Horario(Context context){
        banco = new DBHelper(context);
    }


    public String insereHorario(String nomeDisciplina, Long horaInicio, Long horaFim, String diaSemana){
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(DBHelper.COLUNA_NOME_DISCIPLINA, nomeDisciplina);
        valores.put(DBHelper.COLUNA_HORA_INICIO , horaInicio);
        valores.put(DBHelper.COLUNA_HORA_FIM, horaFim);
        valores.put(DBHelper.COLUNA_DIA_SEMANA, diaSemana);
        resultado = db.insert(DBHelper.NOME_TABELA_AULAS, null, valores);
        db.close();
        if (resultado ==-1) return "Erro ao inserir registro";
        else return "Registro Inserido com sucesso";
    }

    public void alteraRegistro(int id , String nomeDisciplina, Long horaInicio, Long horaFim, String diaSemana){
        ContentValues valores; String where;
        db = banco.getWritableDatabase();
        where = DBHelper.COLUNA_AULA_ID + "=" + id;
        valores = new ContentValues();
        valores.put(DBHelper.COLUNA_NOME_DISCIPLINA, nomeDisciplina);
        valores.put(DBHelper.COLUNA_HORA_INICIO , horaInicio);
        valores.put(DBHelper.COLUNA_HORA_FIM, horaFim);
        valores.put(DBHelper.COLUNA_DIA_SEMANA, diaSemana);
        db.update(DBHelper.NOME_TABELA_AULAS,valores,where,null);
        db.close();
    }

    public void apagaRegistro(int id){
        String where = DBHelper.COLUNA_AULA_ID + "=" + id;
        db = banco.getReadableDatabase();
        db.delete(DBHelper.NOME_TABELA_AULAS,where,null);
        db.close();
    }

    public Cursor preencheSpinner(){
        Cursor cursor;
        String[] campos = new String[]{banco.COLUNA_AULA_ID, banco.COLUNA_NOME_DISCIPLINA,
        banco.COLUNA_HORA_INICIO, banco.COLUNA_HORA_FIM, banco.COLUNA_DIA_SEMANA};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.NOME_TABELA_AULAS, campos, null, null, null, null,
        banco.COLUNA_NOME_DISCIPLINA+" ASC", null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor carregaRegistroUnico(int id){
        Cursor cursor;
        String[] campos = {banco.COLUNA_AULA_ID, banco.COLUNA_NOME_DISCIPLINA,
        banco.COLUNA_HORA_INICIO, banco.COLUNA_HORA_FIM, banco.COLUNA_DIA_SEMANA};
        String where = DBHelper.COLUNA_AULA_ID + "=" + id;
        db = banco.getReadableDatabase();
        cursor = db.query(DBHelper.NOME_TABELA_AULAS,campos,where,
        null, null, null, null, null);
        if(cursor!=null){
        cursor.moveToFirst(); }
        db.close();
        return cursor;
    }
}
