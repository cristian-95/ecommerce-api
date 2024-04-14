# ecommerce-api

## TO-DO:

- [ ] Implementar classes da camada de modelo
  - [ ]  Implementar relacionamentos e cardinalidade

- [X] Implementar algum filtro na camada de serviço para que a listagem de Clientes não inclua Lojas, e vice versa.
  
- [X] Documentação com OpenApi seguindo o exemplo em `ProdutoController`:
  - [X] Ao final, criar uma Collection a partir da documentação, expórtar em arquivo .json e substituir `ecommerce-api.postman_collection` 
  
- [ ] Tratamento correto de exceções:
  - [ ] Mapear exceptions não tratadas (geralmente erros com código 500, com um texto muito grande), e tratar criando exceptions customizadas como `ResourceNotFoundException` e tratar elas em  `GlobalExceptionHandler`
  - [ ] Validar entrada, campos como CPF e CNPJ e email (camada de serviço)
  
- [ ] Implementar HATEOAS para que a API seja RESTful (camada de serviço)

- [ ] Caso encontrar outra tarefa adicione nesta lista.

## Variáveis de ambiente

### No Visual Studio Code:

- Criar um aquivo de nome *launch.json* dentro do diretório .vscode

```json
    .vscode/
    └── launch.json
```

- Conteúdo de launch.json:

```
    {
    "configurations": [
        {
            "name": "Ecommerce api",
            "type": "java",
            "request": "launch",
            "mainClass": "com.commerce.api.ApiApplication",
            "env": {
                "DB_USERNAME": (seu username aqui),    // NOME,
                "DB_PASSWORD": (sua senha aqui)        // SENHA 
            }
        }
    ]
}
```
- Este arquivo está incluso no gitignore.

## Como variável do Linux:

- Adicione estas linhas ao final do arquivo `.bashrc`:

-   `export DB_USERNAME=(seu username aqui)`

-   `export DB_PASSWORD=(sua senha aqui)`

Rode o comando:

    `$ source ~/.bashrc`

- caso não tenha um arquivo .bashrc, pesquise em qual arquivo ficam as variaves de ambiente

- *DB_USERNAME* e *DB_PASSWORD* são acessiveis em application.properties utilizando `${}`

## Diagrama de classes UML

- [link para edição](https://www.plantuml.com/plantuml/uml/TLJHJjmw47tFLupsYNk1XxuY524jHMq52D7o0OCz6rniJx2TRGlYW_eT_R5w4yTrabqUDidCcJDdPewuy05Tw8ok_BEoBJeqe9MbyAibO1UUk6fHF6D3i5gZGOaW2MvOakOLd9_VamXe6qrr3MzL9JHw3m--GwSu1W1EuPfj0qhsxr-3K_6hy-Gi6YfZRQpymMx0FKJ0FMi2ntg28eDAbm7BKvf0cZPifqLMaYF1CNOyYZlwhteVzQwr8Xi8w6U8qAp_hUJm_EXetcnzTvL65AWlCL9837YBNfIeXYmv7e3AX_Cxnx8BNCC6j-nKOD_dFg6_mNRCdi1d6wWXLuD05tcU-9BMR5g7K52HL38ojVtJzmEcQUA0kwyjAmaebL1iqLsbZaUfIvPGW1qPtj9XQB6uQ_wEQMlbrOi5tTvzgHPpjyEBh-PZR2CHnl1npnmdM0oGEBAeH9MHe_hs42vhJvd39fqVMv8NJWdayLfFktCDoPC4gi6t98PxNdBtg0dQvBpKuoL8-kW3ltG3URz2lVlfBp3ELmhjbxFKcMqh-kUCRkhBRyeGXFZZ0vesJOMXyudSzny3wynj7bGWCrXhG2TdnGhKwXNTm5HANdAybKjRUZpto6iB753tVHPagV_S4ocS3B1laTYW8Jk_UGwNnpOKMFBULDxLySYOUxUE1NcFRfuGR3VAcJq9j8BqDFw-apmUTmKVLd0Q_sfO_R_AhZq3b1AGypy7gF-Lv8h-TNcCCsZ8aHKAfrMxcjWadJd2DlWQ4RsdKdec8lFCKIKB1hAIHiXf5_Dft95ZLv_WINnzKBfW6_xyDb13jKod_bEHkQAwY48we_y2)
  

  ![diagrama](https://www.plantuml.com/plantuml/png/TLJHJjmw47tFLupsYNk1XxuY524jHMq52D7o0OCz6rniJx2TRGlYW_eT_R5w4yTrabqUDidCcJDdPewuy05Tw8ok_BEoBJeqe9MbyAibO1UUk6fHF6D3i5gZGOaW2MvOakOLd9_VamXe6qrr3MzL9JHw3m--GwSu1W1EuPfj0qhsxr-3K_6hy-Gi6YfZRQpymMx0FKJ0FMi2ntg28eDAbm7BKvf0cZPifqLMaYF1CNOyYZlwhteVzQwr8Xi8w6U8qAp_hUJm_EXetcnzTvL65AWlCL9837YBNfIeXYmv7e3AX_Cxnx8BNCC6j-nKOD_dFg6_mNRCdi1d6wWXLuD05tcU-9BMR5g7K52HL38ojVtJzmEcQUA0kwyjAmaebL1iqLsbZaUfIvPGW1qPtj9XQB6uQ_wEQMlbrOi5tTvzgHPpjyEBh-PZR2CHnl1npnmdM0oGEBAeH9MHe_hs42vhJvd39fqVMv8NJWdayLfFktCDoPC4gi6t98PxNdBtg0dQvBpKuoL8-kW3ltG3URz2lVlfBp3ELmhjbxFKcMqh-kUCRkhBRyeGXFZZ0vesJOMXyudSzny3wynj7bGWCrXhG2TdnGhKwXNTm5HANdAybKjRUZpto6iB753tVHPagV_S4ocS3B1laTYW8Jk_UGwNnpOKMFBULDxLySYOUxUE1NcFRfuGR3VAcJq9j8BqDFw-apmUTmKVLd0Q_sfO_R_AhZq3b1AGypy7gF-Lv8h-TNcCCsZ8aHKAfrMxcjWadJd2DlWQ4RsdKdec8lFCKIKB1hAIHiXf5_Dft95ZLv_WINnzKBfW6_xyDb13jKod_bEHkQAwY48we_y2)