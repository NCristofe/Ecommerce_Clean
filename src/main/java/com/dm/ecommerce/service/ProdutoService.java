package com.dm.ecommerce.service;

import com.dm.ecommerce.DTOs.ProdutoRequestDTO;
import com.dm.ecommerce.DTOs.ProdutoResponseDTO;
import com.dm.ecommerce.entity.Produto;
import com.dm.ecommerce.repositories.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final FileUploadService fileUploadService;

    public ProdutoService(ProdutoRepository produtoRepository, FileUploadService fileUploadService) {
        this.produtoRepository = produtoRepository;
        this.fileUploadService = fileUploadService;
    }

    public String saveProduto(@Valid ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = new Produto(produtoRequestDTO.getNome(), produtoRequestDTO.getDescricao(),
                produtoRequestDTO.getPreco(), produtoRequestDTO.getImgUrl());
        produtoRepository.save(produto);
        return "Produto criado com sucesso.";
    }

    public String buscaPorId(UUID id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isPresent()) {
            ProdutoResponseDTO dto = new ProdutoResponseDTO(produto.get());
            return dto.toString();
        } else {
            return "Esse ID não é válido.";
        }
    }

    public List<ProdutoResponseDTO> mostrar() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream().map(ProdutoResponseDTO::new).toList();
    }

    public String atualizar(UUID id, Produto novoProduto) {
        Optional<Produto> ProdutoExistente = produtoRepository.findById(id);

        if (ProdutoExistente.isPresent()) {
            Produto Produto = ProdutoExistente.get();

            // Delete old image if a new one is provided
            if (novoProduto.getImgUrl() != null && !novoProduto.getImgUrl().isEmpty()
                    && !novoProduto.getImgUrl().equals(Produto.getImgUrl())) {
                try {
                    fileUploadService.deleteFile(Produto.getImgUrl());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar imagem anterior: " + e.getMessage());
                }
            }

            Produto.setPreco(novoProduto.getPreco());
            if (novoProduto.getNome() != null) {
                Produto.setNome(novoProduto.getNome());
            }
            if (novoProduto.getDescricao() != null) {
                Produto.setDescricao(novoProduto.getDescricao());
            }
            if (novoProduto.getImgUrl() != null) {
                Produto.setImgUrl(novoProduto.getImgUrl());
            }

            produtoRepository.save(Produto);
            return "O produto foi modificado com sucesso.";

        } else {
            return "Não foi achado o produto.";
        }
    }

    public String deleteProduto(UUID id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            // Delete associated image file
            String imagemUrl = produto.get().getImgUrl();
            if (imagemUrl != null && !imagemUrl.isEmpty()) {
                try {
                    fileUploadService.deleteFile(imagemUrl);
                } catch (IOException e) {
                    System.err.println("Erro ao deletar imagem: " + e.getMessage());
                }
            }

            produtoRepository.deleteById(id);
            return "Produto deletado com sucesso.";
        } else {
            return "ID inválido.";
        }
    }
}