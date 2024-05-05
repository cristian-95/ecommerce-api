package com.commerce.api.service;

import com.commerce.api.exception.InvalidOperationException;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.CarrinhoDeComprasDTO;
import com.commerce.api.repository.CarrinhoDeComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoDeComprasRepository repository;
    @Autowired
    private ProdutoService produtoService;

    public CarrinhoDeCompras getCarrinhoDeComprasById(Long id) throws ResourceNotFoundException {
        try {
            return repository.findById(id).get();
        } catch (Exception e) {
            throw new ResourceNotFoundException("CarrinhoDeCompras (id = %d) não encontrado".formatted(id));
        }
    }

    public CarrinhoDeCompras createCarrinhoDeCompras(CarrinhoDeComprasDTO dto) {
        var carrinhoDeCompras = new CarrinhoDeCompras(dto);
        return repository.save(carrinhoDeCompras);
    }

    public void deleteCarrinhoDeCompras(Long id) throws Exception {
        try {
            CarrinhoDeCompras carrinhoDeCompras = repository.findById(id).get();
            repository.delete(carrinhoDeCompras);
        } catch (Exception e) {
            throw new ResourceNotFoundException("CarrinhoDeCompras (id = %d) não encontrado".formatted(id));
        }
    }

    public CarrinhoDeCompras adicionarItem(Long carrinhoId, Long produtoId)
            throws InvalidOperationException, ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinhoDeComprasById(carrinhoId);
        Produto produto = produtoService.getProdutoById(produtoId);
        carrinhoDeCompras.adicionarItem(produto);
        return repository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras removerItem(Long carrinhoId, Long produtoId)
            throws InvalidOperationException, ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinhoDeComprasById(carrinhoId);
        Produto produto = produtoService.getProdutoById(produtoId);
        carrinhoDeCompras.removerItem(produto);
        return repository.save(carrinhoDeCompras);
    }
}
