# e-commerce-api

## TO-DO:

- [X] Implementar classes da camada de modelo
    - [X]  Implementar relacionamentos e cardinalidade

- [X] Documentação com OpenApi seguindo o exemplo em `ProdutoController`:
    - [X] Ao final, criar uma Collection a partir da documentação, expórtar em arquivo .json e
      substituir `ecommerce-api.postman_collection`

- [x] Tratamento correto de exceções *[EM PROGRESSO]*
    - [x] Mapear exceptions não tratadas (geralmente erros com código 500, com um texto muito grande), e tratar criando
      exceptions customizadas como `ResourceNotFoundException` e tratar elas em  `GlobalExceptionHandler`

- [x] Implementar HATEOAS para que a API seja RESTful (camada de serviço)

- [x] Implementar Paginação

- [ ] Opcional: Implementar registro de multiplos produtos via upload de arquivo JSON

- [ ] Testes

- [x] Configurar permissões aos endpoints

- [ ] Atualizar diagram UML

- [ ] Caso encontrar outra tarefa adicione nesta lista.

## Diagrama de classes UML

- [link para edição](https://www.plantuml.com/plantuml/uml/TLJHJjmw47tFLupsYNk1XxuY524jHMq52D7o0OCz6rniJx2TRGlYW_eT_R5w4yTrabqUDidCcJDdPewuy05Tw8ok_BEoBJeqe9MbyAibO1UUk6fHF6D3i5gZGOaW2MvOakOLd9_VamXe6qrr3MzL9JHw3m--GwSu1W1EuPfj0qhsxr-3K_6hy-Gi6YfZRQpymMx0FKJ0FMi2ntg28eDAbm7BKvf0cZPifqLMaYF1CNOyYZlwhteVzQwr8Xi8w6U8qAp_hUJm_EXetcnzTvL65AWlCL9837YBNfIeXYmv7e3AX_Cxnx8BNCC6j-nKOD_dFg6_mNRCdi1d6wWXLuD05tcU-9BMR5g7K52HL38ojVtJzmEcQUA0kwyjAmaebL1iqLsbZaUfIvPGW1qPtj9XQB6uQ_wEQMlbrOi5tTvzgHPpjyEBh-PZR2CHnl1npnmdM0oGEBAeH9MHe_hs42vhJvd39fqVMv8NJWdayLfFktCDoPC4gi6t98PxNdBtg0dQvBpKuoL8-kW3ltG3URz2lVlfBp3ELmhjbxFKcMqh-kUCRkhBRyeGXFZZ0vesJOMXyudSzny3wynj7bGWCrXhG2TdnGhKwXNTm5HANdAybKjRUZpto6iB753tVHPagV_S4ocS3B1laTYW8Jk_UGwNnpOKMFBULDxLySYOUxUE1NcFRfuGR3VAcJq9j8BqDFw-apmUTmKVLd0Q_sfO_R_AhZq3b1AGypy7gF-Lv8h-TNcCCsZ8aHKAfrMxcjWadJd2DlWQ4RsdKdec8lFCKIKB1hAIHiXf5_Dft95ZLv_WINnzKBfW6_xyDb13jKod_bEHkQAwY48we_y2)

![diagrama](https://www.plantuml.com/plantuml/png/TLJHJjmw47tFLupsYNk1XxuY524jHMq52D7o0OCz6rniJx2TRGlYW_eT_R5w4yTrabqUDidCcJDdPewuy05Tw8ok_BEoBJeqe9MbyAibO1UUk6fHF6D3i5gZGOaW2MvOakOLd9_VamXe6qrr3MzL9JHw3m--GwSu1W1EuPfj0qhsxr-3K_6hy-Gi6YfZRQpymMx0FKJ0FMi2ntg28eDAbm7BKvf0cZPifqLMaYF1CNOyYZlwhteVzQwr8Xi8w6U8qAp_hUJm_EXetcnzTvL65AWlCL9837YBNfIeXYmv7e3AX_Cxnx8BNCC6j-nKOD_dFg6_mNRCdi1d6wWXLuD05tcU-9BMR5g7K52HL38ojVtJzmEcQUA0kwyjAmaebL1iqLsbZaUfIvPGW1qPtj9XQB6uQ_wEQMlbrOi5tTvzgHPpjyEBh-PZR2CHnl1npnmdM0oGEBAeH9MHe_hs42vhJvd39fqVMv8NJWdayLfFktCDoPC4gi6t98PxNdBtg0dQvBpKuoL8-kW3ltG3URz2lVlfBp3ELmhjbxFKcMqh-kUCRkhBRyeGXFZZ0vesJOMXyudSzny3wynj7bGWCrXhG2TdnGhKwXNTm5HANdAybKjRUZpto6iB753tVHPagV_S4ocS3B1laTYW8Jk_UGwNnpOKMFBULDxLySYOUxUE1NcFRfuGR3VAcJq9j8BqDFw-apmUTmKVLd0Q_sfO_R_AhZq3b1AGypy7gF-Lv8h-TNcCCsZ8aHKAfrMxcjWadJd2DlWQ4RsdKdec8lFCKIKB1hAIHiXf5_Dft95ZLv_WINnzKBfW6_xyDb13jKod_bEHkQAwY48we_y2)
