package com.commerce.api.service;

import com.commerce.api.controller.ProdutoController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Item;
import com.commerce.api.model.Produto;
import com.commerce.api.repository.CarrinhoDeComprasRepository;
import com.commerce.api.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CarrinhoDeComprasService {

    @Autowired
    private CarrinhoDeComprasRepository repository;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ItemRepository itemRepository;

    public void save(CarrinhoDeCompras carrinhoDeCompras) {
        this.repository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras getCarrinhoDeComprasById(Long id) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = repository.findById(id).orElseThrow();
        carrinhoDeCompras.getItens().forEach(i -> i.getProduto().add(linkTo(methodOn(ProdutoController.class).getById(i.getProduto().getId())).withSelfRel()));
        return carrinhoDeCompras;
    }

    public CarrinhoDeCompras adicionarItem(Long carrinhoId, Long produtoId)
            throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinhoDeComprasById(carrinhoId);
        Produto produto = produtoService.getProdutoById(produtoId);
        Item savedItem = carrinhoDeCompras.adicionarItem(produto);
        itemRepository.save(savedItem);
        return repository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras adicionarItem(CarrinhoDeCompras carrinhoDeCompras, Long produtoId)
            throws ResourceNotFoundException {
        Produto produto = produtoService.getProdutoById(produtoId);
        Item savedItem = carrinhoDeCompras.adicionarItem(produto);
        itemRepository.save(savedItem);
        return repository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras removerItem(Long carrinhoId, Long produtoId)
            throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinhoDeComprasById(carrinhoId);
        if (carrinhoDeCompras.containsProduto(produtoId)) {
            Item item = carrinhoDeCompras.getItemByProdutoId(produtoId);
            itemRepository.delete(item);
        }
        return repository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras removerItem(CarrinhoDeCompras carrinhoDeCompras, Long produtoId)
            throws ResourceNotFoundException {
        Produto produto = produtoService.getProdutoById(produtoId);
        if (carrinhoDeCompras.removerItem(produto)) {
            Item item = carrinhoDeCompras.getItemByProdutoId(produtoId);
            itemRepository.delete(item);
        }
        return repository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras create(CarrinhoDeCompras carrinhoDeCompras) {
        return repository.save(carrinhoDeCompras);
    }
}
