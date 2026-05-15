package com.dm.ecommerce.controllers;

import com.dm.ecommerce.DTOs.ProdutoRequestDTO;
import com.dm.ecommerce.DTOs.ProdutoResponseDTO;
import com.dm.ecommerce.entity.Produto;
import com.dm.ecommerce.service.FileUploadService;
import com.dm.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "produto")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final FileUploadService fileUploadService;

    public ProdutoController(ProdutoService produtoService, FileUploadService fileUploadService) {
        this.produtoService = produtoService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "cadastro")
    public ResponseEntity<?> saveProduto(@Valid @RequestBody ProdutoRequestDTO produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.saveProduto(produto));
    }

    @PostMapping(value = "upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileUploadService.uploadFile(file);
            return ResponseEntity.status(HttpStatus.OK).body("{\"fileName\": \"" + fileName + "\", \"message\": \"Arquivo enviado com sucesso.\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Erro ao fazer upload do arquivo.\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping(value = "download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = fileUploadService.downloadFile(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Arquivo não encontrado.\"}");
        }
    }

    @GetMapping(value = "view")
    public List<ProdutoResponseDTO> mostrar() {
        return produtoService.mostrar();
    }

    @GetMapping(value = "view/{id}")
    public ResponseEntity<?> searchById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscaPorId(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@Valid @PathVariable UUID id, @RequestBody Produto novoProduto) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.atualizar(id, novoProduto));
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.deleteProduto(id));
    }
}