# ecommerce-api

## Como setar variáveis de ambiente no Visual Studio Code:

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
                "DB_USERNAME": ,    // NOME,
                "DB_PASSWORD":      // SENHA
            }
        }
    ]
}
```

- *DB_USERNAME* e *DB_PASSWORD* são acessiveis em application.properties utilizando `${}`
- Este arquivo está incluso no gitignore.

## TO-DO:

- [ ] Implementar classes do diagrama, com seus relacionamentos e cardinalidade
- [ ] Tratamento correto de exceções
- [ ] Documentação com OpenApi
- [ ] 
