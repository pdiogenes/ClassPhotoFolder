package com.example.classphotofolder;

public class Model_Horario {
    private int idAula;
    private String nomeDisciplina;
    private Long horaInicio;
    private Long horaFim;
    private String horaMostrarInicio;


    private String horaMostrarFim;
    private String diaSemana;

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public Long getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Long horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Long getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Long horaFim) {
        this.horaFim = horaFim;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraMostrarInicio() {
        return horaMostrarInicio;
    }

    public void setHoraMostrarInicio(String horaMostrarInicio) {
        this.horaMostrarInicio = horaMostrarInicio;
    }

    public String getHoraMostrarFim() {
        return horaMostrarFim;
    }

    public void setHoraMostrarFim(String horaMostrarFim) {
        this.horaMostrarFim = horaMostrarFim;
    }

}
