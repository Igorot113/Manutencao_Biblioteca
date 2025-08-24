package util;

import java.time.LocalDate;

public class Emprestimo {
	public int indiceLivro;
	public String aluno;
	public int funcao;
	public LocalDate dataEmprestimo; 

	public Emprestimo(int indiceLivro, String aluno, int funcao) {
		this.indiceLivro = indiceLivro;
		this.aluno = aluno;
		this.funcao = funcao;
		this.dataEmprestimo = LocalDate.now();
	}
}