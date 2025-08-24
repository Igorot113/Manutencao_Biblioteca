package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {
	Data data = new Data();
	public ArrayList<Emprestimo> emprestimos = new ArrayList<>();
	public ArrayList<Livro> livros = new ArrayList<>();

	public void emprestarLivro(int indiceLivro, String aluno, int funcao) {
		for (Emprestimo emprestimo : emprestimos) {
			if (emprestimo.aluno.equals(aluno)) {
				System.out.println("Essa pessoa ja possui emprestimos");
				return;
			}
		}
		emprestimos.add(new Emprestimo(indiceLivro, aluno, funcao));
		System.out.println("Livro \"" + livros.get(indiceLivro).titulo + "\" emprestado para " + aluno);
	}

	public void listarEmprestimos() {
		System.out.println("Empréstimos realizados:");
		for (Emprestimo e : emprestimos) {
			if (e.funcao == 1) {
				System.out.println("Professor: " + e.aluno + " | Livro: " + livros.get(e.indiceLivro).titulo
						+ " | Data de entrega: " + data.dataEntregaProfessorFormatado);
			} else {
				System.out.println("Aluno: " + e.aluno + " | Livro: " + livros.get(e.indiceLivro).titulo
						+ " | Data de entrega: " + data.dataEntregaAlunoFormatado);
			}
		}
	}

	public void cadastrarLivro(String titulo, String autor, String ISBN) {
		livros.add(new Livro(titulo, autor, ISBN));
		System.out.println("Livro cadastrado com sucesso!");
	}

	public boolean verif_ISBN(String ISBN) {
		if (ISBN.length() == 10 || ISBN.length() == 13) {
			return true;
		} else {
			return false;
		}
	}

	public void listarLivros() {
		System.out.println("Lista de livros disponíveis:");
		for (int i = 0; i < livros.size(); i++) {
			System.out.println(i + " - " + livros.get(i).titulo + " | Autor: " + livros.get(i).autor + " | ISBN: "
					+ livros.get(i).ISBN);
		}
	}

	public void devolverLivro(int indiceLivro) {
		Emprestimo emprestimo = null;
		int indexEmprestimo = -1;
		for (int i = 0; i < emprestimos.size(); i++) {
			if (emprestimos.get(i).indiceLivro == indiceLivro) {
				emprestimo = emprestimos.get(i);
				indexEmprestimo = i;
				break;
			}
		}
		if (emprestimo == null) {
			System.out.println("Erro livro não encontrado");
			return;
		}
		LocalDate dataDevolucao = data.DataEntrada();
		if (dataDevolucao == null) {
			System.out.println("Devolução canceladada devido ao formato invalido");
			return;
		}
		LocalDate dataEntregaPrevista;
		if (emprestimo.funcao == 1) {
			dataEntregaPrevista = emprestimo.dataEmprestimo.plusDays(15);
		} else {
			dataEntregaPrevista = emprestimo.dataEmprestimo.plusDays(7);
		}
		long diasAtraso = ChronoUnit.DAYS.between(dataEntregaPrevista, dataDevolucao);
		if (diasAtraso > 0) {
			double multa = diasAtraso * 2;
			System.out.printf("O livro foi devolvido com %d dias de atraso.\n", diasAtraso);
			System.out.printf("Multa a ser paga: R$ %.2f\n", multa);
		} else {
			System.out.println("Livro devolvido no prazo.");
		}
		emprestimos.remove(indexEmprestimo);
	}

	public void gerarRelatorioLivrosMaisEmprestadosMes() {
		LocalDate dataAtual = LocalDate.now();
		int mesAtual = dataAtual.getMonthValue();
		int anoAtual = dataAtual.getYear();

		System.out.println("--- Relatório de Livros Mais Emprestados do Mês ---");

		Map<String, Integer> contagemEmprestimos = new HashMap<>();

		for (Emprestimo e : emprestimos) {
			if (e.dataEmprestimo.getMonthValue() == mesAtual && e.dataEmprestimo.getYear() == anoAtual) {
				String tituloLivro = livros.get(e.indiceLivro).titulo;
				contagemEmprestimos.put(tituloLivro, contagemEmprestimos.getOrDefault(tituloLivro, 0) + 1);
			}
		}

		if (contagemEmprestimos.isEmpty()) {
			System.out.println("Nenhum livro foi emprestado neste mês.");
			return;
		}

		List<Map.Entry<String, Integer>> listaOrdenada = contagemEmprestimos.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

		for (Map.Entry<String, Integer> entry : listaOrdenada) {
			System.out.println(entry.getKey() + ": " + entry.getValue() + " empréstimo(s)");
		}
	}
}