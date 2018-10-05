package br.com.quub.repository.palavrarestrita;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.quub.model.PalavraRestrita;

public interface PalavraRestritaRepository extends JpaRepository<PalavraRestrita, Long> {

}