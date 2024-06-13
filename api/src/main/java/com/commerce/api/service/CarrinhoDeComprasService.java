package com.commerce.api.service;

import com.commerce.api.controller.ProdutoController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Item;
import com.commerce.api.model.Produto;
import com.commerce.api.repository.CarrinhoDeComprasRepository;
import com.commerce.api.repository.ItemRepository;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CarrinhoDeComprasService {

    private final CarrinhoDeComprasRepository carrinhoDeComprasRepository;
    private final ProdutoService produtoService;
    private final ItemRepository itemRepository;

    public CarrinhoDeComprasService(CarrinhoDeComprasRepository carrinhoDeComprasRepository, ProdutoService produtoService, ItemRepository itemRepository) {
        this.carrinhoDeComprasRepository = carrinhoDeComprasRepository;
        this.produtoService = produtoService;
        this.itemRepository = itemRepository;
    }

    public void save(CarrinhoDeCompras carrinhoDeCompras) {
        this.carrinhoDeComprasRepository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras getCarrinhoDeComprasById(Long id) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = carrinhoDeComprasRepository.findById(id).orElseThrow();
        carrinhoDeCompras.getItens().forEach(i -> i.getProduto().add(linkTo(methodOn(ProdutoController.class).getById(i.getProduto().getId())).withSelfRel()));
        return carrinhoDeCompras;
    }

    public CarrinhoDeCompras adicionarItem(Long carrinhoId, Long produtoId)
            throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinhoDeComprasById(carrinhoId);
        Produto produto = produtoService.getProdutoById(produtoId);
        Item savedItem = carrinhoDeCompras.adicionarItem(produto);
        itemRepository.save(savedItem);
        return carrinhoDeComprasRepository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras adicionarItem(CarrinhoDeCompras carrinhoDeCompras, Long produtoId)
            throws ResourceNotFoundException {
        Produto produto = produtoService.getProdutoById(produtoId);
        Item savedItem = carrinhoDeCompras.adicionarItem(produto);
        itemRepository.save(savedItem);
        return carrinhoDeComprasRepository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras removerItem(Long carrinhoId, Long produtoId)
            throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinhoDeComprasById(carrinhoId);
        if (carrinhoDeCompras.containsProduto(produtoId)) {
            Item item = carrinhoDeCompras.getItemByProdutoId(produtoId);
            itemRepository.delete(item);
        }
        return carrinhoDeComprasRepository.save(carrinhoDeCompras);
    }

    public CarrinhoDeCompras removerItem(CarrinhoDeCompras carrinhoDeCompras, Long produtoId)
            throws ResourceNotFoundException {
        Produto produto = produtoService.getProdutoById(produtoId);
        if (carrinhoDeCompras.removerItem(produto)) {
            Item item = carrinhoDeCompras.getItemByProdutoId(produtoId);
            itemRepository.delete(item);
        }
        return carrinhoDeComprasRepository.save(carrinhoDeCompras);
    }

    public void remover(CarrinhoDeCompras carrinhoDeCompras) {
        this.carrinhoDeComprasRepository.delete(carrinhoDeCompras);
    }

    CarrinhoDeCompras create(CarrinhoDeCompras carrinhoDeCompras) {
        return carrinhoDeComprasRepository.save(carrinhoDeCompras);
    }
}
