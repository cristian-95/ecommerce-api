package com.commerce.api.service;

import com.commerce.api.controller.ClienteController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.ClienteUpdateDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClienteService {

    private final AuthenticationService authenticationService;
    private final PagedResourcesAssembler<Cliente> assembler;
    private final ClienteRepository clienteRepository;
    private final ProdutoService produtoService;
    private final CarrinhoDeComprasService carrinhoDeComprasService;

    public ClienteService(AuthenticationService authenticationService, PagedResourcesAssembler<Cliente> assembler, ClienteRepository clienteRepository, ProdutoService produtoService, CarrinhoDeComprasService carrinhoDeComprasService) {
        this.authenticationService = authenticationService;
        this.assembler = assembler;
        this.clienteRepository = clienteRepository;
        this.produtoService = produtoService;
        this.carrinhoDeComprasService = carrinhoDeComprasService;
    }

    public Cliente createNewClienteAccount(Cliente newCliente) {
        newCliente.setCarrinhoDeCompras(List.of(new CarrinhoDeCompras()));
        Cliente saved = this.clienteRepository.save(newCliente);
        saved.add(linkTo(methodOn(ClienteController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Cliente createCliente(String username, ClienteDTO dto) throws ResourceNotFoundException {
        var admin = authenticationService.loadUserByUsername(username);
        if (admin.getAuthorities().contains("ROLE_ADMIN")) {
            Cliente cliente = new Cliente(dto);
            String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
            cliente.setPassword(encriptedPassword);
            Cliente saved = this.clienteRepository.save(cliente);
            saved.add(linkTo(methodOn(ClienteController.class).getById(saved.getId())).withSelfRel());
            return saved;
        }
        return null;
    }

    public Cliente getProfile(String username) {
        return clienteRepository.findByUsername(username);
    }

    public PagedModel<EntityModel<Cliente>> getAllClientes(Pageable pageable) {
        Page<Cliente> clientes = this.clienteRepository.findAll(pageable);
        clientes.forEach(c -> c.add(linkTo(methodOn(ClienteController.class).getById(c.getId())).withSelfRel()));
        return this.assembler.toModel(clientes);
    }

    public Cliente getClienteById(Long id) throws ResourceNotFoundException {
        Cliente cliente = this.clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        cliente.add(linkTo(methodOn(ClienteController.class).getAll(0, 0, "")).withRel("listagem"));

        return cliente;
    }

    public Cliente updateProfile(String username, ClienteUpdateDTO dto) throws ResourceNotFoundException {
        Cliente cliente = getProfile(username);
        Cliente updated = this.clienteRepository.save(updateProperties(cliente, dto));
        updated.add(linkTo(methodOn(ClienteController.class).getById(updated.getId())).withSelfRel());
        return updated;
    }

    public void deleteAccount(String username) {
        if (this.clienteRepository.existsByUsername(username)) {
            Cliente cliente = this.clienteRepository.findByUsername(username);
            cliente.setAccountNonLocked(false);
            cliente.setEnabled(false);
            this.clienteRepository.save(cliente);
        }
    }

    public CarrinhoDeCompras getCarrinho(String username) throws ResourceNotFoundException {
        Cliente cliente = clienteRepository.findByUsername(username);
        CarrinhoDeCompras response;
        if (!cliente.getCarrinhoDeCompras().isEmpty()) {
            List<CarrinhoDeCompras> carrinhoDeComprasList = cliente.getCarrinhoDeCompras();
            Collections.reverse(carrinhoDeComprasList);
            Long carrinhoId = carrinhoDeComprasList.get(0).getId();
            response = this.carrinhoDeComprasService.getCarrinhoDeComprasById(carrinhoId);
        } else {
            CarrinhoDeCompras carrinhoDeCompras = new CarrinhoDeCompras(cliente);
            response = this.carrinhoDeComprasService.create(carrinhoDeCompras);
        }
        return response;
    }

    public CarrinhoDeCompras adicionarAoCarrinho(String username, RequestDTO requestDTO) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinho(username);
        carrinhoDeCompras = this.carrinhoDeComprasService.adicionarItem(carrinhoDeCompras, requestDTO.id());

        return carrinhoDeCompras;
    }

    public CarrinhoDeCompras removerDoCarrinho(String username, RequestDTO requestDTO) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = getCarrinho(username);
        carrinhoDeCompras = this.carrinhoDeComprasService.removerItem(carrinhoDeCompras, requestDTO.id());

        return carrinhoDeCompras;
    }

    public Produto adicionarFavorito(String username, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = clienteRepository.findByUsername(username);
        Produto produto = this.produtoService.getProdutoById(produtoId);
        cliente.adicionarFavorito(produto);
        this.clienteRepository.save(cliente);

        return produto;
    }

    public Produto removerFavorito(String username, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = clienteRepository.findByUsername(username);
        Produto produto = this.produtoService.getProdutoById(produtoId);
        cliente.removerFavorito(produto);
        this.clienteRepository.save(cliente);

        return produto;
    }

    public List<Produto> getAllFavoritos(String username) throws ResourceNotFoundException {
        Cliente cliente = clienteRepository.findByUsername(username);
        return cliente.getFavoritos();
    }

    void save(Cliente cliente) {
        this.clienteRepository.save(cliente);
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
