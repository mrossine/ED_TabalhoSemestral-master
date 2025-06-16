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

import br.edu.fateczl.lista.Lista;
import model.Disciplina;

public class crudDisciplinas implements ActionListener {

	private int idDisciplina;
	private String idCurso;

	private JTextField tfDisciplinaNome;
	private JTextField tfDisciplinaDia;
	private JTextField tfDisciplinaHora;
	private JTextField tfDisciplinaQuantidadeHora;
	private JTextField tfDisciplinaCurso;
	private JTextArea taDisciplinaLista;

	public crudDisciplinas(JTextField tfDisciplinaNome, JTextField tfDisciplinaDia, JTextField tfDisciplinaHora,
			JTextField tfDisciplinaQuantidadeHora, JTextField tfDisciplinaCurso, JTextArea taDisciplinaLista) {
		this.tfDisciplinaNome = tfDisciplinaNome;
		this.tfDisciplinaDia = tfDisciplinaDia;
		this.tfDisciplinaHora = tfDisciplinaHora;
		this.tfDisciplinaQuantidadeHora = tfDisciplinaQuantidadeHora;
		this.taDisciplinaLista = taDisciplinaLista;
		this.tfDisciplinaCurso = tfDisciplinaCurso;
		try {
			this.idDisciplina = defineIdDisciplinaAtual();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void zeraCampos() {
		tfDisciplinaNome.setText("");
		tfDisciplinaDia.setText("");
		tfDisciplinaHora.setText("");
		tfDisciplinaQuantidadeHora.setText("");
		tfDisciplinaCurso.setText("");
	}

	private String determinaCurso(JTextField tfDisciplinaCurso) throws IOException {
		String curso = null;
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "cursos.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[1].contains(tfDisciplinaCurso.getText())) {
					curso = vetorLinha[0];
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return curso;
	}

	private int defineIdDisciplinaAtual() throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "disciplinas.csv");
		int id = 0;
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			String auxLinha = null;
			while (linha != null) {
				auxLinha = linha;
				linha = bufferedReader.readLine();
			}
			String[] vetorLinha = auxLinha.split(";");
			String[] aux = vetorLinha[0].split("");
			if (auxLinha != null) {
				id = Integer.parseInt(aux[1]);
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return id;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Cadastrar")) {
			try {
				insereDisciplina();
				zeraCampos();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Buscar")) {
			try {
				consultaDisciplina();
				zeraCampos();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Atualizar")) {
			try {
				atualizaDisciplina();
				zeraCampos();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Excluir")) {
			try {
				excluiDisciplina();
				zeraCampos();
			} catch (Exception e1) {
				System.err.println(e1.getMessage());
			}
		}
	}

	private void insereDisciplina() throws IOException {
		Disciplina auxDisciplina = new Disciplina();
		idDisciplina++;
		auxDisciplina.setIdDisciplina("D" + idDisciplina);
		auxDisciplina.setNome(tfDisciplinaNome.getText());
		auxDisciplina.setDiaSemanaDisciplina(tfDisciplinaDia.getText());
		auxDisciplina.setHoraInicialDisciplina(tfDisciplinaHora.getText());
		auxDisciplina.setQuantidadeHorasDias(tfDisciplinaQuantidadeHora.getText());
		auxDisciplina.setCodigoCurso(idCurso = determinaCurso(tfDisciplinaCurso));
		if (idCurso == null) {
			taDisciplinaLista.setText(
					"O curso informado não está cadastrado!\r\nVerifique o nome digitado, se estiver correto, será necessário cadastrar um novo curso");
		} else {
			Disciplina aux = consultaDisciplinaArquivo(auxDisciplina);
			if (aux.getDiaSemanaDisciplina() != null) {
				adicionaDisciplinaArquivo(auxDisciplina.toString());
			} else {
				taDisciplinaLista.setText("Disciplina ja foi cadastrada");
			}
		}
	}

	private void adicionaDisciplinaArquivo(String csvDisciplina) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "disciplinas.csv");
		boolean existe = false;
		if (arq.equals(arq)) {
			existe = true;
		}
		FileWriter fileWriter = new FileWriter(arq, existe);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(csvDisciplina + "\r\n");
		printWriter.flush();
		printWriter.close();
		fileWriter.close();
	}

	private void consultaDisciplina() throws IOException {
		Disciplina auxDisciplina = new Disciplina();
		if (tfDisciplinaNome.getText().equals("") && tfDisciplinaDia.getText().equals("")
				&& tfDisciplinaHora.getText().equals("") && tfDisciplinaQuantidadeHora.getText().equals("")
				&& tfDisciplinaCurso.getText().equals("")) {
			consultaTudo();
		} else if (!tfDisciplinaNome.getText().equals("")) {
			auxDisciplina.setNome(tfDisciplinaNome.getText());
			auxDisciplina = consultaDisciplinaArquivo(auxDisciplina);
			if (auxDisciplina.getDiaSemanaDisciplina() != null) {
				taDisciplinaLista.setText("Código\tNome\tDia da Sem.\tInicio\tTempo Total\tCod Curso\n\r");
				taDisciplinaLista.append(auxDisciplina.getIdDisciplina() + "\t" + auxDisciplina.getNome() + "\t"
						+ auxDisciplina.getDiaSemanaDisciplina() + "\t" + auxDisciplina.getHoraInicialDisciplina()
						+ "\t" + auxDisciplina.getQuantidadeHorasDias() + "\t" + auxDisciplina.getCodigoCurso());
			} else {
				taDisciplinaLista.setText("Disciplina nao encontrada");
			}
		} else {
			taDisciplinaLista
					.setText("A consulta é feita apenas pelo nome da disciplina\r\nDigite o nome da disciplina");
		}
	}

	private void consultaTudo() throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "disciplinas.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			taDisciplinaLista.setText("Código\tNome\tDia da Sem.\tInicio\tTempo Total\tCod Curso\n\r");
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				taDisciplinaLista.append(vetorLinha[0] + "\t" + vetorLinha[1] + "\t" + vetorLinha[2] + "\t"
						+ vetorLinha[3] + "\t" + vetorLinha[4] + "\t" + vetorLinha[5] + "\n\r");
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
	}

	private Disciplina consultaDisciplinaArquivo(Disciplina disciplina) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "disciplinas.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[1].equals(disciplina.getNome())) {
					disciplina.setIdDisciplina(vetorLinha[0]);
					disciplina.setDiaSemanaDisciplina(vetorLinha[2]);
					disciplina.setHoraInicialDisciplina(vetorLinha[3]);
					disciplina.setQuantidadeHorasDias(vetorLinha[4]);
					disciplina.setCodigoCurso(vetorLinha[5]);
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return disciplina;
	}

	private void atualizaDisciplina() throws IOException {
		Disciplina auxDisciplina = new Disciplina();
		auxDisciplina.setNome(tfDisciplinaNome.getText());

		auxDisciplina = consultaDisciplinaArquivoAtualiza(auxDisciplina);

		if (auxDisciplina.getDiaSemanaDisciplina() != null) {

		} else {
			taDisciplinaLista.setText("Disciplina nao encontrada");
		}
	}

	private Disciplina consultaDisciplinaArquivoAtualiza(Disciplina disciplina) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "disciplinas.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[1].equals(disciplina.getNome())) {
					disciplina.setIdDisciplina(vetorLinha[0]);
					disciplina.setDiaSemanaDisciplina(vetorLinha[2]);
					disciplina.setHoraInicialDisciplina(vetorLinha[3]);
					disciplina.setQuantidadeHorasDias(vetorLinha[4]);
					disciplina.setCodigoCurso(vetorLinha[5]);
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return disciplina;
	}

	public void excluiDisciplina() throws Exception {
		Lista<String> lista = new Lista<>();
		String verifica = tfDisciplinaNome.getText();
		if (verifica == null || verifica.equals("")) {
			taDisciplinaLista.setText("Digite o nome da disciplina que será excluída");
		} else {
			lista = alimentaLista();
			int tamanho = lista.size();
			for (int i = 0; i < tamanho; i++) {
				String[] vetor = lista.get(i).split(";");
				if (tfDisciplinaNome.getText().equals(vetor[1])) {
					remove(lista, i);
					break;
				} else if (i == tamanho - 1) {
					taDisciplinaLista.setText("Disciplina não encontrada");
				}
			}
		}
	}

	private void remove(Lista<String> lista, int posicao) throws Exception {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "disciplinas.csv");
		lista.remove(posicao);
		int tamanho = lista.size();
		if (tamanho != 0) {
			for (int i = 0; i < tamanho; i++) {
				if (i == 0) {
					FileWriter fileWriter = new FileWriter(arq, false);
					PrintWriter printWriter = new PrintWriter(fileWriter);
					String csv = lista.get(i);
					printWriter.write(csv + "\r\n");
					printWriter.flush();
					printWriter.close();
					fileWriter.close();
				} else {
					FileWriter fileWriter = new FileWriter(arq, true);
					PrintWriter printWriter = new PrintWriter(fileWriter);
					String csv = lista.get(i);
					printWriter.write(csv + "\r\n");
					printWriter.flush();
					printWriter.close();
					fileWriter.close();
				}
			}
		}
	}

	private Lista<String> alimentaLista() throws Exception {
		Lista<String> lista = new Lista<>();
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "disciplinas.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				lista.addLast(linha);
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return lista;
	}

}
