# e-commerce-api

---
Este projeto implementa a API de back-end para uma plataforma de e-commerce que suporta múltiplos vendedores.

## Instruções de Execução

### Pré-requisitos:

- Java 17 instalado
- Maven instalado
- PostgreSQL instalado e configurado com um banco de dados chamado **e_commerce_api_db**

### Passos:

1. Clone o repositório para o seu diretório local:

   ```Bash
   git clone https://github.com/cristian-95/ecommerce-api
   ```    

2. Acesse o diretório do projeto:
   
   ```Bash
   cd ecommerce-api/api
   ```

3. Instale as dependências do projeto:

   ```Bash
   mvn install
   ```

4. Execute a aplicação Spring Boot:

   ```Bash
   mvn spring-boot:run
   ```

5. Acesse a aplicação na URL: http://localhost:8080

   - Utilize a collection do postman para testar,

## Diagrama de classes UML

![diagrama](https://www.plantuml.com/plantuml/png/ZLNDSkCs3BxpAL2TqhewzDh9QDPZkzjqR2VZ9ESELaHiTabHIrBfj6dUlVmH4whCeHSA13u27tum-cGT6ZSfoRvp5r842erbzgiOHZIeG8g1t5yZWJRkejc8_LSy4pGRLCWHEC51SvAwWUlh4_KEXxEaheCNndg9riAZdT08xHK0BUprS0R1etplZF2toP8PK56f6_tBFxL9k4SFE6b9OFnHeaYXaALYq4itZYGzwM7vQE1agDTUjyhahhvdhvdlcYinUBAh99MclVu3lUNDrUPuzrjnvMHx8thKGRSNrbtV6SqdfpiOas3VBSG53uRm3VfLzEwZUUbP2mx8HI_qW2P1hzAdO9ESruMjgr7pyyfVqLuEE5uduuypg0CxKcyZOgkdBv9WJDNoYfl1qPaCV7ESTjRfRrFo7vY330U3dE1cByZ3E-Xb5EoxwSQHwa0u6agbmdDK-syoh7A6eIXLLHB-AdqAW26bdsbfBegsIMnUohhqurDvjVgBeRVQxNMFSekEWAF38zg-n7ggP3I3gk5F-AoDSDeM5K7ZtrpqbZPQZGQjhyr_L0Mw6X1OPV9rFUefgG6_p8okPevrpMh4Ayjr2ptparOEMflxawURbICouU04Hqv1MwLUPnvWYVoSCeDLZFhHIXI3iKmX6ugEfn5OK5-kI75rOjJ5TKHt-J3FrUorZ9F8rzkOkvONORanonbx48h0-SCwLECyQEWcEpktuVBH9gd5nZhjKCRsfdTfOBBLkq0f_aRp461b2dMRwcp-JzmFSqvzge9xOdNSMH_251iVXY4SXJ6rlJJvP6YOrDjFmsUmtXvkZWmEw-Fwy-x4uF7UdncNAfIgxB5tk-DsTtpOCTWTVhyxtNu-xUxlRnbiRe-R_UFDDeZhusQtNtlnDNZ9_xTsUlzJiVZhW9Qt_tGm0rZiUIlRKfVhrBQcLDUDQ7u2QDgsQtve8AREmX7Lu2qXUXSnULYpQmzf8BxB4_epDFH4RiBmi80bFpnWerYHI1b4OE0Is7XfuJYOqD70gSdevDER4ZRl4ij2oYo7ZP7JSZ6M_8QQBTh4Fla0avB_0W00)

- [Link para edição](https://www.plantuml.com/plantuml/uml/ZLNDSkCs3BxpAL2TqhewzDh9QDPZkzjqR2VZ9ESELaHiTabHIrBfj6dUlVmH4whCeHSA13u27tum-cGT6ZSfoRvp5r842erbzgiOHZIeG8g1t5yZWJRkejc8_LSy4pGRLCWHEC51SvAwWUlh4_KEXxEaheCNndg9riAZdT08xHK0BUprS0R1etplZF2toP8PK56f6_tBFxL9k4SFE6b9OFnHeaYXaALYq4itZYGzwM7vQE1agDTUjyhahhvdhvdlcYinUBAh99MclVu3lUNDrUPuzrjnvMHx8thKGRSNrbtV6SqdfpiOas3VBSG53uRm3VfLzEwZUUbP2mx8HI_qW2P1hzAdO9ESruMjgr7pyyfVqLuEE5uduuypg0CxKcyZOgkdBv9WJDNoYfl1qPaCV7ESTjRfRrFo7vY330U3dE1cByZ3E-Xb5EoxwSQHwa0u6agbmdDK-syoh7A6eIXLLHB-AdqAW26bdsbfBegsIMnUohhqurDvjVgBeRVQxNMFSekEWAF38zg-n7ggP3I3gk5F-AoDSDeM5K7ZtrpqbZPQZGQjhyr_L0Mw6X1OPV9rFUefgG6_p8okPevrpMh4Ayjr2ptparOEMflxawURbICouU04Hqv1MwLUPnvWYVoSCeDLZFhHIXI3iKmX6ugEfn5OK5-kI75rOjJ5TKHt-J3FrUorZ9F8rzkOkvONORanonbx48h0-SCwLECyQEWcEpktuVBH9gd5nZhjKCRsfdTfOBBLkq0f_aRp461b2dMRwcp-JzmFSqvzge9xOdNSMH_251iVXY4SXJ6rlJJvP6YOrDjFmsUmtXvkZWmEw-Fwy-x4uF7UdncNAfIgxB5tk-DsTtpOCTWTVhyxtNu-xUxlRnbiRe-R_UFDDeZhusQtNtlnDNZ9_xTsUlzJiVZhW9Qt_tGm0rZiUIlRKfVhrBQcLDUDQ7u2QDgsQtve8AREmX7Lu2qXUXSnULYpQmzf8BxB4_epDFH4RiBmi80bFpnWerYHI1b4OE0Is7XfuJYOqD70gSdevDER4ZRl4ij2oYo7ZP7JSZ6M_8QQBTh4Fla0avB_0W00)
