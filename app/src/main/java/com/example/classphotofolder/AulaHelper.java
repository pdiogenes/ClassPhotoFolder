package com.example.classphotofolder;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Calendar;

public class AulaHelper {
    Cursor cur;
    Context ctx;
    Controller_Horario controller;

    AulaHelper(Context ctx){
        this.ctx = ctx;
        controller = new Controller_Horario(ctx);
    }

    String getAulaAtual(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int horas = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("horas ", String.valueOf(horas));
        int minutos = calendar.get(Calendar.MINUTE);
        Log.d("minutos ", String.valueOf(minutos));
        String aux = "";
        if(minutos < 10){
            aux = String.valueOf(horas)+"0"+String.valueOf(minutos);
        } else aux = String.valueOf(horas)+String.valueOf(minutos);
        long t = Long.parseLong(aux);
        Log.d("HoraAtualMain", String.valueOf(t));
        String dayOfWeek = getDia(day);
        Log.d("dia: ", dayOfWeek);
        cur = controller.acharHorarioAtual(t, dayOfWeek);
        if(cur.getCount() > 0){
            String aula = cur.getString
                    (cur.getColumnIndexOrThrow(DBHelper.COLUNA_NOME_DISCIPLINA));
            return aula;
        }
        else{
            return "";
        }
    }

    String getDia(int day){
        String dia = "";
        switch (day) {
            case Calendar.SUNDAY:
                dia = "Domingo";
                break;
            case Calendar.MONDAY:
                dia = "Segunda";
                break;
            case Calendar.TUESDAY:
                dia = "Terça";
                break;
            case Calendar.WEDNESDAY:
                dia = "Quarta";
                break;
            case Calendar.THURSDAY:
                dia = "Quinta";
                break;
            case Calendar.FRIDAY:
                dia = "Sexta";
                break;
            case Calendar.SATURDAY:
                dia = "Sábado";
                break;
        }
        return dia;
    }
}
