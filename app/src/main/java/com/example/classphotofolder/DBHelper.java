package com.example.classphotofolder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    protected static final String NOME_BANCO = "horario.db";
    protected static final int VERSAO_BANCO = 1;
    protected final static String NOME_TABELA_AULAS = "Aula";
    protected final static String COLUNA_AULA_ID = "_id";
    protected final static String COLUNA_NOME_DISCIPLINA = "NomeDisciplina";
    protected final static String COLUNA_HORA_INICIO = "HoraInicio";
    protected final static String COLUNA_HORA_FIM = "HoraFim";
    protected final static String COLUNA_DIA_SEMANA = "DiaSemana";

    public DBHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CRIA_TABELA_AULAS = "CREATE TABLE " +
        NOME_TABELA_AULAS + "(" +
        COLUNA_AULA_ID + " INTEGER PRIMARY KEY," +
        COLUNA_NOME_DISCIPLINA + " TEXT," +
        COLUNA_HORA_INICIO + " DATETIME," +
        COLUNA_HORA_FIM + " DATETIME," +
        COLUNA_DIA_SEMANA + " TEXT" +
        ")";
        db.execSQL(CRIA_TABELA_AULAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOME_TABELA_AULAS);
        onCreate(db);
    }
}
