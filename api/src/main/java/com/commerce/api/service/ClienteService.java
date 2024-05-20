package com.commerce.api.service;

import com.commerce.api.controller.ClienteController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.ClienteUpdateDTO;
import com.commerce.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClienteService {

    @Autowired
    private PagedResourcesAssembler<Cliente> assembler;
    @Autowired
    private ClienteRepository repository;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private CarrinhoDeComprasService carrinhoDeComprasService;

    public void save(Cliente cliente) {
        this.repository.save(cliente);
    }

    public Cliente createNewClienteAccount(Cliente newCliente) {
        newCliente.setCarrinhoDeCompras(List.of(new CarrinhoDeCompras()));
        Cliente saved = this.repository.save(newCliente);
        saved.add(linkTo(methodOn(ClienteController.class).getById(saved.getId())).withSelfRel());

        return saved;
    }

    public Cliente createCliente(ClienteDTO dto) throws ResourceNotFoundException {
        Cliente cliente = new Cliente(dto);
        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        cliente.setPassword(encriptedPassword);
        Cliente saved = this.repository.save(cliente);
        saved.add(linkTo(methodOn(ClienteController.class).getById(saved.getId())).withSelfRel());

        return saved;
    }

    public PagedModel<EntityModel<Cliente>> getAllClientes(Pageable pageable) {
        Page<Cliente> clientes = this.repository.findAll(pageable);
        clientes.forEach(c -> c.add(linkTo(methodOn(ClienteController.class).getById(c.getId())).withSelfRel()));
        return this.assembler.toModel(clientes);
    }

    public Cliente getClienteById(Long id) throws ResourceNotFoundException {
        Cliente cliente = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        cliente.add(linkTo(methodOn(ClienteController.class).getAll(0, 0, "")).withRel("listagem"));

        return cliente;
    }

    public Cliente updateCliente(ClienteUpdateDTO dto) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(dto.id());
        Cliente updated = this.repository.save(updateProperties(cliente, dto));
        updated.add(linkTo(methodOn(ClienteController.class).getById(updated.getId())).withSelfRel());

        return updated;
    }

    public void deleteCliente(Long id) {
        if (this.repository.existsById(id)) {
            Cliente cliente = this.repository.findById(id).get();
            this.repository.delete(cliente);
        }
    }

    public CarrinhoDeCompras getCarrinho(Long clienteId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        CarrinhoDeCompras response;
        if (!cliente.getCarrinhoDeCompras().isEmpty()) {
            Long carrinhoId = cliente.getCarrinhoDeCompras().get(0).getId();
            response = this.carrinhoDeComprasService.getCarrinhoDeComprasById(carrinhoId);
        } else {
            CarrinhoDeCompras carrinhoDeCompras = new CarrinhoDeCompras(cliente);
            response = this.carrinhoDeComprasService.create(carrinhoDeCompras);
        }

        return response;
    }

    public CarrinhoDeCompras adicionarAoCarrinho(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinho(clienteId);
        carrinhoDeCompras = this.carrinhoDeComprasService.adicionarItem(carrinhoDeCompras, produtoId);

        return carrinhoDeCompras;
    }

    public CarrinhoDeCompras removerDoCarrinho(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinho(clienteId);
        carrinhoDeCompras = this.carrinhoDeComprasService.removerItem(carrinhoDeCompras, produtoId);

        return carrinhoDeCompras;
    }

    public Produto adicionarFavorito(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = this.produtoService.getProdutoById(produtoId);
        cliente.adicionarFavorito(produto);
        this.repository.save(cliente);

        return produto;
    }

    public Produto removerFavorito(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = this.produtoService.getProdutoById(produtoId);
        cliente.removerFavorito(produto);
        this.repository.save(cliente);

        return produto;
    }

    public List<Produto> getAllFavoritos(Long clienteId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        return cliente.getFavoritos();
    }

    public List<Pedido> getAllPedidos(Long clienteId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        return cliente.getPedidos();
    }

    private Cliente updateProperties(Cliente cliente, ClienteUpdateDTO dto) {
        cliente.setNome(dto.nome() != null ? dto.nome() : cliente.getNome());
        cliente.setSobrenome(dto.sobrenome() != null ? dto.sobrenome() : cliente.getSobrenome());
        cliente.setCpf(dto.CPF() != null ? dto.CPF() : cliente.getCpf());
        cliente.setEmail(dto.email() != null ? dto.email() : cliente.getEmail());
        cliente.setTelefone(dto.telefone() != null ? dto.telefone() : cliente.getTelefone());
        cliente.setEndereco(dto.endereco() != null ? dto.endereco() : cliente.getEndereco());
        cliente.setDataNasc(dto.dataNasc() != null ? dto.dataNasc() : cliente.getDataNasc());
        cliente.setGenero(dto.genero() != null ? dto.genero() : cliente.getGenero());

        return cliente;
    }
}
