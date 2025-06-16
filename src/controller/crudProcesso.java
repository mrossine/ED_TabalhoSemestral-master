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
import model.Processo;

public class crudProcesso implements ActionListener {

	private int codProcesso;

	private JTextField tfProcessoIdDisciplina;

	private JTextArea taProcessoLista;

	public crudProcesso(JTextField tfProcessoIdDisciplina, JTextArea taProcessoLista) {
		this.tfProcessoIdDisciplina = tfProcessoIdDisciplina;
		this.taProcessoLista = taProcessoLista;
	}

	private void zeraCampos() {
		tfProcessoIdDisciplina.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Abrir")) {
			try {
				abreProcesso();
				zeraCampos();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Buscar")) {
			try {
				buscaProcesso();
				zeraCampos();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (command.equals("Fechar")) {
			try {
				fechaProcesso();
				zeraCampos();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void abreProcesso() throws IOException {
		Processo processo = new Processo();
		boolean blCdDisc = verificaCodDisciplina(tfProcessoIdDisciplina.getText());
		if (!blCdDisc) {
			taProcessoLista.setText(
					"Disciplina não cadastrada!\n\rVerifique o código digitado e insira um código de disciplina válido");
		} else {
			processo = consultaProcesso();
			if ((processo.getCodProcesso() == null || processo.getCodProcesso().equals(""))
					&& (processo.getIdDisciplina() == null || processo.getIdDisciplina().equals(""))) {
				codProcesso = defineCodProcessoAnteior();
				codProcesso++;
				processo.setCodProcesso("P" + codProcesso);
				processo.setIdDisciplina(tfProcessoIdDisciplina.getText());
				cadastraArquivoProcesso(processo.toString());
			} else {
				taProcessoLista.setText(
						"Já há um processo aberto para essa disciplina\r\nEspere esse processo acabar para cadastrar outro");
			}
		}
	}

	private void cadastraArquivoProcesso(String csvProcesso) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File arq = new File(path, "processos.csv");
		boolean existe = false;
		if (arq.equals(arq)) {
			existe = true;
		}
		FileWriter fileWriter = new FileWriter(arq, existe);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(csvProcesso + "\r\n");
		printWriter.flush();
		printWriter.close();
		fileWriter.close();
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

	private int defineCodProcessoAnteior() throws IOException {
		int cod = 0;
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "processos.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			String aux = null;
			while (linha != null) {
				aux = linha;
				linha = bufferedReader.readLine();
			}
			if (aux != null) {
				String[] vetorAux = aux.split(";");
				String[] vetor = vetorAux[0].split("");
				cod = Integer.parseInt(vetor[1]);
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return cod;
	}

	private void buscaProcesso() throws IOException {
		Processo processo = new Processo();
		if (tfProcessoIdDisciplina.getText().equals("")) {
			consultaTudo();
		} else {
			processo = consultaProcesso();
			if (processo.getCodProcesso() == null || processo.getCodProcesso().equals("")
					|| processo.getIdDisciplina() == null || processo.getIdDisciplina().equals("")) {
				taProcessoLista.setText("Não existe processo seletivo para essa disciplina");
			} else {
				taProcessoLista.setText("Cód. Processo\tCód. Disciplina\n\r");
				taProcessoLista.append(processo.getCodProcesso() + "\t" + processo.getIdDisciplina());
			}
		}	
	}

	private void consultaTudo() throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "processos.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			taProcessoLista.setText("Cód. Processo\tCód. Disciplina\n\r");
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				for (String i : vetorLinha) {
					taProcessoLista.append(i + "\t");
				}
				taProcessoLista.append("\n\r");
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
	}

	private Processo consultaProcesso() throws IOException {
		Processo processo = new Processo();
		String path = System.getProperty("user.home") + File.separator + "Sistema Contratação";
		File arq = new File(path, "processos.csv");
		if (arq.exists() && arq.isFile()) {
			FileInputStream fileInputStream = new FileInputStream(arq);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String linha = bufferedReader.readLine();
			while (linha != null) {
				String[] vetorLinha = linha.split(";");
				if (vetorLinha[1].equals(tfProcessoIdDisciplina.getText())) {
					processo.setCodProcesso(vetorLinha[0]);
					processo.setIdDisciplina(vetorLinha[1]);
					break;
				}
				linha = bufferedReader.readLine();
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		return processo;
	}

	private void fechaProcesso() throws Exception {
		Lista<String> lista = new Lista<>();
		if (tfProcessoIdDisciplina.getText().equals("")) {
			taProcessoLista.setText("Informe o código da disciplina que deseja encerrar o processo seletivo");
		} else {
			lista = alimentaLista();
			int tamanho = lista.size();
			for (int i = 0; i < tamanho; i++) {
				String[] vetor = lista.get(i).split(";");
				if (tfProcessoIdDisciplina.getText().equals(vetor[1])) {
					remove(lista, i);
					break;
				} else if (i == tamanho - 1) {
					taProcessoLista.setText("Disciplina não encontrada");
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
		File arq = new File(path, "processos.csv");
		lista.remove(posicao);
		int tamanho = lista.size();
		if (tamanho != 0) {
			for (int i = 0; i < tamanho; i++) {
				if (i == 0) {
					FileWriter fileWriter = new FileWriter(arq, false);
					PrintWriter printWriter = new PrintWriter(fileWriter);
					printWriter.write(lista.get(i) + "\r\n");
					printWriter.flush();
					printWriter.close();
					fileWriter.close();
				} else {
					FileWriter fileWriter = new FileWriter(arq, true);
					PrintWriter printWriter = new PrintWriter(fileWriter);
					printWriter.write(lista.get(i) + "\r\n");
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
		File arq = new File(path, "processos.csv");
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
