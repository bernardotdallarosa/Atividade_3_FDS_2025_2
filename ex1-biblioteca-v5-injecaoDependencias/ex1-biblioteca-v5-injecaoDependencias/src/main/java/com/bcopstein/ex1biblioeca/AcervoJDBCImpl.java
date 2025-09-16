package com.bcopstein.ex1biblioeca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AcervoJDBCImpl implements IAcervoRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AcervoJDBCImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Livro> getAll() {
        return this.jdbcTemplate.query(
                "SELECT * FROM livros",
                (rs, rowNum)
                -> new Livro(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano")
                )
        );
    }

    @Override
    public List<String> getTitulos() {
        return this.jdbcTemplate.query(
                "SELECT titulo FROM livros",
                (rs, rowNum) -> rs.getString("titulo")
        );
    }

    @Override
    public List<String> getAutores() {
        return this.jdbcTemplate.query(
                "SELECT autor FROM livros",
                (rs, rowNum) -> rs.getString("autor")
        );
    }

    @Override
    public List<Livro> getLivrosDoAutor(String autor) {
        return this.jdbcTemplate.query(
                "SELECT * FROM livros WHERE autor = ?",
                (rs, rowNum)
                -> new Livro(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano")
                ),
                autor
        );
    }

    @Override
    public Livro getLivroTitulo(String titulo) {
        List<Livro> livros = this.jdbcTemplate.query(
                "SELECT * FROM livros WHERE titulo = ?",
                (rs, rowNum)
                -> new Livro(
                        rs.getInt("codigo"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano")
                ),
                titulo
        );
        return livros.isEmpty() ? null : livros.get(0);
    }

    @Override
    public boolean cadastraLivroNovo(Livro livro) {
        this.jdbcTemplate.update(
                "INSERT INTO livros(codigo, titulo, autor, ano) VALUES (?, ?, ?, ?)",
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAno()
        );
        return true;
    }

    @Override
    public boolean updateLivro(Livro livro) {
        int rows = this.jdbcTemplate.update(
                "UPDATE livros SET titulo = ?, autor = ?, ano = ? WHERE codigo = ?",
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAno(),
                livro.getId()
        );
        return rows > 0;
    }

    @Override
    public boolean removeLivro(long codigo) {
        int rows = this.jdbcTemplate.update(
                "DELETE FROM livros WHERE codigo = ?",
                codigo
        );
        return rows > 0;
    }
}
