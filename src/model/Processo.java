package model;

public class Processo {

	private String codProcesso;
	private String idDisciplina;
	
	public String getCodProcesso() {
		return codProcesso;
	}
	public void setCodProcesso(String codProcesso) {
		this.codProcesso = codProcesso;
	}
	public String getIdDisciplina() {
		return idDisciplina;
	}
	public void setIdDisciplina(String idDisciplina) {
		this.idDisciplina = idDisciplina;
	}
	@Override
	public String toString() {
		return codProcesso + ";" + idDisciplina;
	}

}
