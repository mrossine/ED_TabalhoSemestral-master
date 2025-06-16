package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Inscricao;

public class crudInscricao implements ActionListener {

	private JTextField tfInscricaoCpfProfessor;
	private JTextField tfInscricaoIdDisciplina;
	private JTextField tfInscricaoCodProcesso;

	private JTextArea taInscricaoLista;

	public crudInscricao(JTextField tfInscricaoCpfProfessor, JTextField tfInscricaoIdDisciplina,
			JTextField tfInscricaoCodProcesso, JTextArea taInscricaoLista) {
		this.tfInscricaoCpfProfessor = tfInscricaoCpfProfessor;
		this.tfInscricaoIdDisciplina = tfInscricaoIdDisciplina;
		this.tfInscricaoCodProcesso = tfInscricaoCodProcesso;
		this.taInscricaoLista = taInscricaoLista;	
	}

	private void zeraCampos() {
		tfInscricaoCpfProfessor.setText("");
		tfInscricaoIdDisciplina.setText("");
		tfInscricaoCodProcesso.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Cadastrar")) {
			try {
				insereInscricao();
				zeraCampos();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Buscar")) {
			try {
				consultaInscricao();
				zeraCampos();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Atualizar")) {
//			try {
//				atualizaInscricao();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
		} else if (command.equals("Excluir")) {
			try {
//				excluiCurso();
				zeraCampos();
			} catch (Exception e1) {
				System.err.println(e1.getMessage());
			}
		}
	}

	private void insereInscricao() throws IOException {
		Inscricao inscricao = new Inscricao();
		boolean blCdProcess = verificaProcesso(tfInscricaoCodProcesso.getText());
		boolean blProf = verificaProfessor(tfInscricaoCpfProfessor.getText());
		boolean blCdDisc = verificaCodDisciplina(tfInscricaoIdDisciplina.getText());
		if (!blProf || !blCdDisc || !blCdProcess) {
			if (!blProf) {
				taInscricaoLista.setText("Professor não cadastrado \n\rDigite o CPF de um professor que esteja cadastrado");
			}
			if (!blCdDisc) {
				taInscricaoLista.setText(
						"Código de disciplina inválido \n\rDigite o código de uma disciplina que esteja cadastrada");
			}
			if (!blCdProcess) {
				taInscricaoLista.setText(
						"Não existe processo aberto com esse código \n\rDigite o código de um processo que esteja aberto");
			}
		} else {
			inscricao.setCpfProfessor(tfInscricaoCpfProfessor.getText());
			inscricao.setIdDisciplina(tfInscricaoIdDisciplina.getText());
			inscricao.setCodigoProcesso(tfInscricaoCodProcesso.getText());
			escreveArquivo(inscricao.toString());
		}
	}

	private void escreveArquivo(String csvInscricao) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "inscricoes.csv");
		boolean existe = false;
		if (arq.equals(arq)) {
			existe = true;
		}
		FileWriter fileWriter = new FileWriter(arq, existe);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(csvInscricao + "\r\n");
		printWriter.flush();
		printWriter.close();
		fileWriter.close();
	}

	private boolean verificaProfessor(String cpf) throws IOException {
		boolean existe = false;
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "professores.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[1].equals(cpf)) {
					existe = true;
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return existe;
	}

	private boolean verificaCodDisciplina(String idDisciplina) throws IOException {
		boolean existe = false;
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "disciplinas.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[0].equals(idDisciplina)) {
					existe = true;
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return existe;
	}

	private boolean verificaProcesso(String codProcesso) throws IOException {
		boolean existe = false;
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "processos.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[0].equals(codProcesso)) {
					existe = true;
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return existe;
	}
	
	private void consultaInscricao() {
		
	}
}
